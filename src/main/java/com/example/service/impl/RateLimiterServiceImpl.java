package com.example.service.impl;

import com.example.constant.Constants;
import com.example.properties.RateLimiterProperties;
import com.example.service.RateLimiterService;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class RateLimiterServiceImpl implements RateLimiterService {
    private final StringRedisTemplate redisTemplate;
    private final RateLimiterProperties rateLimiterProperties;

    @Override
    public boolean isAllowed(String clientId) {
        String tokenKey = Constants.USER_TOKEN_PREFIX + clientId;
        String lastRefillKey = Constants.USER_LAST_REFILL_PREFIX + clientId;
        long now = Instant.now().toEpochMilli();
        Object lastRefillObj = redisTemplate.opsForValue().get(lastRefillKey);
        Object tokenObj = redisTemplate.opsForValue().get(tokenKey);
        long lastRefillTime = lastRefillObj == null ? now : Long.parseLong(lastRefillObj.toString());
        int availableToken = tokenObj == null ? rateLimiterProperties.getBucketCapacity() : Integer.parseInt(tokenObj.toString());

        // refill logic
        long refillIntervalMs = rateLimiterProperties.getRefillInterval().toMillis();
        long intervalTime = now - lastRefillTime;
        if (intervalTime >= refillIntervalMs || intervalTime == 0) {
            int refillAmount = rateLimiterProperties.getRefillTokens();
            availableToken = Math.min(rateLimiterProperties.getBucketCapacity(), availableToken + refillAmount);
            lastRefillTime = now;
            redisTemplate.opsForValue().set(tokenKey, String.valueOf(availableToken));
            redisTemplate.opsForValue().set(lastRefillKey, String.valueOf(lastRefillTime));
            System.out.println("redis value {}" + redisTemplate.opsForValue().get(tokenKey));
        }

        // Consume token if available
        if (availableToken > 0) {
            Object obj = redisTemplate.opsForValue().get(tokenKey);
            int i = obj == null ? 0 : Integer.parseInt(obj.toString());
            redisTemplate.opsForValue().set(tokenKey, String.valueOf(i - 1));
            return true;
        } else {
            return false;
        }
    }

}
