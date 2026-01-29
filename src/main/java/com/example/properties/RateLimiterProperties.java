package com.example.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@ConfigurationProperties(prefix = "rate.limiter")
@Data
public class RateLimiterProperties {
    private int bucketCapacity;
    private int refillTokens;
    private int refillIntervalMinutes;
    public Duration getRefillInterval() {
        return Duration.ofMinutes(refillIntervalMinutes);
    }}

