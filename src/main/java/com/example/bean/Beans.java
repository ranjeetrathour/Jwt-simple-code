package com.example.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;

@Configuration
public class Beans {

    private final String cryptoKey = "fhgjhkjlk;llkjghhjlkjkhjfdgfhjhkjg";
    private static final String ALGORITHM = "AES";
    private static final String SHA_256 = "SHA-256";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

    @Bean
    public Cipher encryptionCipher() throws Exception {
        byte[] key = cryptoKey.getBytes(StandardCharsets.UTF_8);
        MessageDigest sha = MessageDigest.getInstance(SHA_256);
        key = sha.digest(key);
        key = Arrays.copyOf(key, 32);
        SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher;
    }

    @Bean
    public Cipher decryptionCipher() throws Exception {
        byte[] key = cryptoKey.getBytes(StandardCharsets.UTF_8);
        MessageDigest sha = MessageDigest.getInstance(SHA_256);
        key = sha.digest(key);
        key = Arrays.copyOf(key, 32);
        SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher;
    }
}
