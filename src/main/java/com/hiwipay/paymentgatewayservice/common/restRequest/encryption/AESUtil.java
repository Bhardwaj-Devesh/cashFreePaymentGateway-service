package com.hiwipay.paymentgatewayservice.common.restRequest.encryption;

import lombok.SneakyThrows;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class AESUtil {
    private AESUtil(){}

    public static SecretKey getKeyFromUserHashId(String userHashId, String salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(userHashId.toCharArray(), salt.getBytes(), 65536, 256);

        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }

    @SneakyThrows
    public static String encrypt(String algorithm, String input, SecretKey key) {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] cipherText = cipher.doFinal(input.getBytes());

        return Base64.getEncoder().encodeToString(cipherText);
    }

    @SneakyThrows
    public static String encryptInputString(String input) {

        SecretKey key = AESUtil.getKeyFromUserHashId("example", "salt");
        String algorithm = "AES/ECB/PKCS5Padding";

        return AESUtil.encrypt(algorithm, input, key);
    }

    public static String decrypt(String algorithm, String cipherText, SecretKey key)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            BadPaddingException, IllegalBlockSizeException, InvalidKeyException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        return new String(plainText);
    }

    @SneakyThrows
    public static String decryptString(String input) {
        SecretKey key = AESUtil.getKeyFromUserHashId("example", "salt");
        String algorithm = "AES/ECB/PKCS5Padding";
        return AESUtil.decrypt(algorithm, input, key);
    }
}
