package com.epam.resourceprocessor.client.song;

import com.epam.resourceprocessor.exception.feign.FeignErrorDecoder;
import feign.Logger;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.feign.FeignDecorators;
import io.github.resilience4j.feign.Resilience4jFeign;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.retry.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor

@Component
public class SongServiceFeignDecoratorClient {
    private final CircuitBreaker songServiceCircuitBreaker;
    private final Retry songServiceRetry;
    private final RateLimiter defaultRateLimiter;

    public SongServiceFeignClient forTarget(String uri) {
        var decorators = FeignDecorators.builder()
                .withRateLimiter(defaultRateLimiter)
                .withCircuitBreaker(songServiceCircuitBreaker)
                .withRetry(songServiceRetry)
                .build();

        return Resilience4jFeign.builder(decorators)
                .logger(new Slf4jLogger(SongServiceFeignClient.class))
                .logLevel(Logger.Level.FULL)
                .errorDecoder(new FeignErrorDecoder())
                .decoder(new JacksonDecoder())
                .encoder(new JacksonEncoder())
                .target(SongServiceFeignClient.class, uri);
    }
}
