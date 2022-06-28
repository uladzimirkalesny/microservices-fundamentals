package com.epam.resourceprocessor.config;

import com.epam.resourceprocessor.exception.FeignCommunicationApiException;
import com.epam.resourceprocessor.exception.ResourceProcessorException;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static java.time.Duration.of;

@Configuration
public class Resilience4JConfiguration {
    @Bean
    public CircuitBreaker songServiceCircuitBreaker() {
        var circuitBreakerConfig = getCircuitBreakerConfig();
        var circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig);
        return circuitBreakerRegistry.circuitBreaker("song-service");
    }

    @Bean
    public Retry songServiceRetry() {
        var retryConfig = getRetryConfig();
        var retryRegistry = RetryRegistry.of(retryConfig);
        return retryRegistry.retry("song-service");
    }

    @Bean
    public CircuitBreaker resourceServiceCircuitBreaker() {
        var circuitBreakerConfig = getCircuitBreakerConfig();
        var circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig);
        return circuitBreakerRegistry.circuitBreaker("resource-service");
    }


    @Bean
    public Retry resourceServiceRetry() {
        var retryConfig = getRetryConfig();
        var retryRegistry = RetryRegistry.of(retryConfig);
        return retryRegistry.retry("resource-service");
    }

    @Bean
    public RateLimiter defaultRateLimiter() {
        var config = RateLimiterConfig.custom()
                .limitRefreshPeriod(Duration.ofMillis(1))
                .limitForPeriod(10)
                .timeoutDuration(Duration.ofMillis(25))
                .build();

        var rateLimiterRegistry = RateLimiterRegistry.of(config);

        return rateLimiterRegistry.rateLimiter("default");
    }

    private CircuitBreakerConfig getCircuitBreakerConfig() {
        return CircuitBreakerConfig.custom()
                .slidingWindowSize(100)
                .slowCallRateThreshold(65.0f)
                .slowCallDurationThreshold(Duration.ofSeconds(3))
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .minimumNumberOfCalls(5)
                .failureRateThreshold(50)
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .waitDurationInOpenState(of(5, ChronoUnit.SECONDS))
                .permittedNumberOfCallsInHalfOpenState(3)
                .recordExceptions(
                        HttpServerErrorException.class,
                        ResourceAccessException.class,
                        FeignCommunicationApiException.class,
                        FeignException.class,
                        IOException.class,
                        Exception.class)
                .ignoreExceptions(ResourceProcessorException.class)
                .build();
    }

    private RetryConfig getRetryConfig() {
        return RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofSeconds(5))
                .retryExceptions(
                        HttpServerErrorException.class,
                        ResourceAccessException.class,
                        FeignCommunicationApiException.class,
                        FeignException.class,
                        IOException.class,
                        Exception.class)
                .ignoreExceptions(ResourceProcessorException.class)
                .build();
    }
}
