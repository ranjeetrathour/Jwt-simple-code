package com.example.crypto;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

@Service
public class CryptoServiceImpl implements CryptoService {

    private final String cryptoKey = "fhgjhkjlk;llkjghhjlkjkhjfdgfhjhkjg";

    private static final String ALGORITHM = "AES";
    private static final String SHA_256 = "SHA-256";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

    private Cipher encryptionCipher;
    private Cipher decryptionCipher;

    @PostConstruct
    private void configureCrypto() throws Exception {
        byte[] key = cryptoKey.getBytes(StandardCharsets.UTF_8);
        MessageDigest sha = MessageDigest.getInstance(SHA_256);
        key = sha.digest(key);
        key = Arrays.copyOf(key, 32);
        SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);

        encryptionCipher = Cipher.getInstance(TRANSFORMATION);
        encryptionCipher.init(Cipher.ENCRYPT_MODE, secretKey);

        decryptionCipher = Cipher.getInstance(TRANSFORMATION);
        decryptionCipher.init(Cipher.DECRYPT_MODE, secretKey);

        System.out.println("Crypto cipher configured successfully");
    }

    @Override
    public String encrypt(String rawData) {
        try {
            return Base64.getEncoder().encodeToString(encryptionCipher.doFinal(rawData.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    @Override
    public String decrypt(String encData) {
        try {
            byte[] decryptedBytes = decryptionCipher.doFinal(Base64.getDecoder().decode(encData));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }
}
