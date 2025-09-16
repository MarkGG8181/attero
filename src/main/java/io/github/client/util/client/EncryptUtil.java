package io.github.client.util.client;

import io.github.client.Attero;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class EncryptUtil {
    private static SecretKeySpec secretKey;

    public static void prepareKey(String myKey) {
        MessageDigest digest;
        try {
            byte[] key = myKey.getBytes(StandardCharsets.UTF_8);
            digest = MessageDigest.getInstance("SHA-1");
            key = digest.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            Attero.LOGGER.error("Failed to prepare secret key", e);
        }
    }

    public static String encrypt(String strToEncrypt, String secret) {
        try {
            prepareKey(secret);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            Attero.LOGGER.error("Failed to encrypt", e);
        }
        return null;
    }

    public static String decrypt(String strToDecrypt, String secret) {
        try {
            prepareKey(secret);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            Attero.LOGGER.error("Failed to decrypt", e);
        }
        return null;
    }
}