package org.emails;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

/**
 * Класс для отправки email-сообщений на несколько адресов.
 */
public class EmailSender {

    // Логгер для логирования ошибок и действий
    private static final Logger logger = LogManager.getLogger(EmailSender.class);

    /**
     * Отправляет персонализированные email-сообщения на несколько адресов.
     * @param emails Массив строк, каждая строка содержит email и имя через запятую.
     * @param messageText Текст сообщения, в котором подставляется имя {name}.
     * @param subject Тема письма.
     */
    public static void sendEmails(String[] emails, String messageText, String subject) {
        try {
            // Ваши данные (замените на свои)
            String username = "EXAMPLEmatvey@gmail.com";// Меняем на реально существующий email
            String aesKey = "EXAMPLE80cnMPx71tOqQ=="; // Ваш сгенерированный AES-ключ
            String encryptedPassword = "EXAMPLEafLiqQ0C/eaIkWT7d5aCWHLuPxZEEAnNUE="; // Ваш зашифрованный пароль

            // Расшифровываем пароль
            String password = org.emails.AESUtils.decrypt(encryptedPassword, aesKey);

            // Настройки почтового сервера
            Properties properties = new Properties();
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            // Настроить сессию с аутентификацией
            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            // Для каждого email-адреса
            for (String emailEntry : emails) {
                String[] parts = emailEntry.split(",");  // Разделяем email и имя
                if (parts.length == 2) {
                    String email = parts[0].trim();  // email
                    String name = parts[1].trim();  // имя (Ф\u0418О)

                    // Персонализируем сообщение
                    String personalizedMessage = messageText.replace("{name}", name);

                    // Создаем письмо
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(username));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
                    message.setSubject(subject);
                    message.setText(personalizedMessage);

                    // Отправляем письмо
                    Transport.send(message);
                    logger.info("Письмо успешно отправлено на: " + email);
                } else {
                    logger.warn("Неверный формат строки с email: " + emailEntry);
                }
            }
        } catch (Exception e) {
            logger.error("Ошибка при отправке письма", e);
        }
    }
}