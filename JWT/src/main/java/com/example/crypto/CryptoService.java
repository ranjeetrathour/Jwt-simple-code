package com.example.crypto;

public interface CryptoService {
    String encrypt(String rawData);
    String decrypt(String encData);
}
