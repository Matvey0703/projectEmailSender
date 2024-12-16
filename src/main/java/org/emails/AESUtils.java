package org.emails;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * Утилитный класс для работы с алгоритмом AES.
 * Включает методы для генерации ключа, шифрования и расшифрования данных.
 */
public class AESUtils {
    private static final String AES_ALGORITHM = "AES";

    /**
     * Генерирует случайный AES-ключ длиной 128 бит.
     * @return Закодированный в Base64 AES-ключ.
     * @throws Exception если возникла ошибка при генерации ключа.
     */
    public static String generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_ALGORITHM);
        keyGenerator.init(128); // Устанавливаем длину ключа (128 бит)
        SecretKey secretKey = keyGenerator.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    /**
     * Шифрует текст с использованием AES-алгоритма.
     * @param plainText Текст, который нужно зашифровать.
     * @param key Ключ для шифрования, закодированный в Base64.
     * @return Зашифрованный текст, закодированный в Base64.
     * @throws Exception если возникла ошибка при шифровании.
     */
    public static String encrypt(String plainText, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(key), AES_ALGORITHM);
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * Расшифровывает текст, зашифрованный с использованием AES-алгоритма.
     * @param encryptedText Зашифрованный текст в формате Base64.
     * @param key Ключ для расшифрования, закодированный в Base64.
     * @return Расшифрованный текст.
     * @throws Exception если возникла ошибка при расшифровании.
     */
    public static String decrypt(String encryptedText, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(key), AES_ALGORITHM);
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes);
    }
}