package com.example.service;

public interface RateLimiterService {
    boolean isAllowed(String clientId);
}
