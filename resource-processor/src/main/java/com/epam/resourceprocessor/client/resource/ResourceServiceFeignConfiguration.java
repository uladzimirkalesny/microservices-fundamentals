package com.epam.resourceprocessor.client.resource;

import com.epam.resourceprocessor.exception.feign.FeignErrorDecoder;
import feign.Feign;
import feign.Logger;
import feign.slf4j.Slf4jLogger;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.feign.FeignDecorators;
import io.github.resilience4j.feign.Resilience4jFeign;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.retry.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor

@Configuration
public class ResourceServiceFeignConfiguration {
    private final CircuitBreaker resourceServiceCircuitBreaker;
    private final Retry resourceServiceRetry;
    private final RateLimiter defaultRateLimiter;

    @Bean
    public Feign.Builder resourceServiceFeignBuilder() {
        var decorators = FeignDecorators.builder()
                .withRateLimiter(defaultRateLimiter)
                .withCircuitBreaker(resourceServiceCircuitBreaker)
                .withRetry(resourceServiceRetry)
                .build();

        return Resilience4jFeign.builder(decorators)
                .logger(new Slf4jLogger(ResourceServiceFeignClient.class))
                .logLevel(Logger.Level.FULL)
                .errorDecoder(new FeignErrorDecoder());
    }
}
