package org.emails;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Класс для работы с файлами: чтение email-адресов и сообщений, а также их открытие в текстовом редакторе.
 */
public class FileReader {

    // Логгер для логирования ошибок и действий
    private static final Logger logger = LogManager.getLogger(FileReader.class);

    // Регулярное выражение для проверки email
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

    /**
     * Открывает файл в программе по умолчанию.
     * @param filePath Путь к файлу, который нужно открыть.
     */
    public void openFile(String filePath) {
        File file = new File(filePath);

        if (!file.exists()) {
            logger.error("Ошибка: файл не найден по пути: " + filePath);
            return;
        }

        if (!Desktop.isDesktopSupported()) {
            logger.error("Ошибка: Desktop API не поддерживается.");
            return;
        }

        Desktop desktop = Desktop.getDesktop();
        if (desktop.isSupported(Desktop.Action.OPEN)) {
            try {
                desktop.open(file); // Открываем файл в программе по умолчанию
                logger.info("Файл успешно открыт: " + filePath);
            } catch (IOException e) {
                logger.error("Ошибка при открытии файла: " + e.getMessage(), e);
            }
        } else {
            logger.error("Ошибка: Открытие файлов не поддерживается.");
        }
    }

    /**
     * Читает email-адреса и имена из файла.
     * Каждый адрес должен быть разделен запятой, а имя - после запятой.
     * @param filePath Путь к файлу с email-адресами.
     * @return Массив объектов Email.
     */
    public Email[] readEmails(String filePath) {
        List<Email> emailList = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            logger.error("Ошибка: файл с email-адресами не найден: " + filePath);
            return new Email[0];
        }

        try (BufferedReader reader = new BufferedReader(new java.io.FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String email = parts[0].trim();
                    String name = parts[1].trim();

                    // Проверяем корректность email
                    if (EMAIL_PATTERN.matcher(email).matches()) {
                        emailList.add(new Email(email, name));
                        logger.info("Email добавлен: " + email);
                    } else {
                        logger.error("Ошибка: некорректный email: " + email);
                    }
                } else {
                    logger.error("Ошибка: строка не содержит корректные данные: " + line);
                }
            }
        } catch (IOException e) {
            logger.error("Ошибка при чтении файла с email-адресами: " + filePath, e);
        }

        return emailList.toArray(new Email[0]);
    }

    /**
     * Читает текст сообщения из файла.
     * @param filePath Путь к файлу с текстом сообщения.
     * @return Строка с текстом сообщения.
     */
    public String readMessage(String filePath) {
        File file = new File(filePath);

        if (!file.exists()) {
            logger.error("Ошибка: файл с сообщением не найден: " + filePath);
            return null;
        }

        StringBuilder message = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new java.io.FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                message.append(line).append("\n");
            }
        } catch (IOException e) {
            logger.error("Ошибка при чтении файла с сообщением: " + filePath, e);
        }return message.toString().trim();
    }
}