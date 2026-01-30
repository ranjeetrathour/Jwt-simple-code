package com.example.crypto;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
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

    private static final String ALGORITHM = "AES"; // Algorithm you want (can change)
    private static final String SHA_256 = "SHA-256"; // To create fixed-size key
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding"; // AES mode & padding

    private Cipher encryptionCipher;
    private Cipher decryptionCipher;

    @PostConstruct
    private void configureCrypto() throws Exception {
        //Convert secret key string to bytes
        byte[] key = cryptoKey.getBytes(StandardCharsets.UTF_8);
        //Hash the key using SHA-256 to ensure fixed length (32 bytes for AES-256)
        MessageDigest sha = MessageDigest.getInstance(SHA_256);
        key = sha.digest(key);
        //Copy key to exactly 32 bytes (AES-256 requires 32 bytes)
        key = Arrays.copyOf(key, 32);
        // Create AES secret key
        SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
        // Initialize encryption cipher
        encryptionCipher = Cipher.getInstance(TRANSFORMATION);
        encryptionCipher.init(Cipher.ENCRYPT_MODE, secretKey);
        // Initialize decryption cipher
        decryptionCipher = Cipher.getInstance(TRANSFORMATION);
        decryptionCipher.init(Cipher.DECRYPT_MODE, secretKey);
        System.out.println("Crypto cipher configured successfully");
    }

    @Override
    public String encrypt(String rawData) {
        try {
            //Encrypt raw data and Return Base64 encoded string (easier to store/transmit)
            return Base64.getEncoder().encodeToString(encryptionCipher.doFinal(rawData.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    @Override
    public String decrypt(String encData) {
        try {
            //Decode Base64 and Decrypt bytes
            byte[] decryptedBytes = decryptionCipher.doFinal(Base64.getDecoder().decode(encData));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }
}
