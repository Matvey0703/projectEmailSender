package org.emails;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Класс для графического интерфейса приложения отправки email-сообщений.
 * Реализует загрузку, сохранение и отправку email-адресов и сообщений.
 */
public class EmailApp extends Application {

    // Логгер для логирования действий
    private static final Logger logger = LogManager.getLogger(EmailApp.class);

    private TextArea emailTextArea;
    private TextArea messageTextArea;

    private final String emailFilePath = "src/main/resources/emails.txt";
    private final String messageFilePath = "src/main/resources/message.txt";

    @Override
    public void start(Stage primaryStage) {
        // Инициализация текстовых полей
        emailTextArea = new TextArea();
        messageTextArea = new TextArea();

        // Кнопки
        Button saveEmailsButton = new Button("Сохранить email адреса");
        Button saveMessageButton = new Button("Сохранить текст сообщения");
        Button sendEmailsButton = new Button("Отправить письма");

        // Загрузка данных при запуске
        loadFileToTextArea(emailFilePath, emailTextArea);
        loadFileToTextArea(messageFilePath, messageTextArea);

        // Обработчики кнопок
        saveEmailsButton.setOnAction(e -> saveTextAreaToFile(emailTextArea, emailFilePath));
        saveMessageButton.setOnAction(e -> saveTextAreaToFile(messageTextArea, messageFilePath));
        sendEmailsButton.setOnAction(e -> sendEmails());

        // Размещение элементов
        VBox layout = new VBox(10);
        layout.getChildren().addAll(
                new Label("Email адреса:"),
                emailTextArea,
                saveEmailsButton,
                new Label("Текст сообщения:"),
                messageTextArea,
                saveMessageButton,
                sendEmailsButton
        );

        // Настройка окна
        Scene scene = new Scene(layout, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Email Sender Application");
        primaryStage.show();
    }

    /**
     * Загружает содержимое файла в текстовое поле.
     * @param filePath Путь к файлу.
     * @param textArea Текстовое поле для загрузки данных.
     */
    private void loadFileToTextArea(String filePath, TextArea textArea) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            textArea.setText(content);
            logger.info("Файл загружен: " + filePath);
        } catch (IOException e) {
            showAlert("Ошибка", "Не удалось загрузить файл: " + filePath);
            logger.error("Ошибка при загрузке файла: " + filePath, e);
        }
    }

    /**
     * Сохраняет содержимое текстового поля в файл.
     * @param textArea Текстовое поле для сохранения данных.
     * @param filePath Путь к файлу.
     */
    private void saveTextAreaToFile(TextArea textArea, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(textArea.getText());
            showAlert("Успех", "Файл успешно сохранен: " + filePath);
            logger.info("Файл сохранен: " + filePath);
        } catch (IOException e) {
            showAlert("Ошибка", "Не удалось сохранить файл: " + filePath);
            logger.error("Ошибка при сохранении файла: " + filePath, e);
        }
    }

    /**
     * Отправляет email-сообщения на указанные адреса.
     */
    private void sendEmails() {
        String[] emails = emailTextArea.getText().split("\n");
        String message = messageTextArea.getText();
        String subject = "Новая платформа";

        if (emails.length == 0 || message.isEmpty()) {
            showAlert("Ошибка", "Email адреса или текст сообщения не заполнены.");
            logger.warn("Не указаны email адреса или текст сообщения.");
            return;
        }

        // Отправка писем
        EmailSender.sendEmails(emails, message, subject);
        showAlert("Отправление", "Письма отправлены!");
        logger.info("Письма отправлены.");
    }

    /**
     * Показывает окно с сообщением.
     * @param title Заголовок окна.
     * @param message Сообщение, которое будет отображаться.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}