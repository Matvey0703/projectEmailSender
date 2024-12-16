package org.emails;

/**
 * Класс, представляющий email-адрес и имя получателя.
 * Используется для хранения информации об email-адресах и соответствующих именах.
 */
public class Email {
    private String email;
    private String name;

    /**
     * Конструктор для создания объекта Email.
     * @param email Адрес электронной почты.
     * @param name Имя получателя.
     */
    public Email(String email, String name) {
        this.email = email;
        this.name = name;
    }

    /**
     * Получить email-адрес.
     * @return Адрес электронной почты.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Получить имя получателя.
     * @return Имя получателя.
     */
    public String getName() {
        return name;
    }

    /**
     * Установить новый email-адрес.
     * @param email Новый адрес электронной почты.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Установить новое имя получателя.
     * @param name Новое имя получателя.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Переопределение метода toString для отображения объекта в виде строки.
     * @return Строковое представление объекта Email.
     */
    @Override
    public String toString() {
        return "Email{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}