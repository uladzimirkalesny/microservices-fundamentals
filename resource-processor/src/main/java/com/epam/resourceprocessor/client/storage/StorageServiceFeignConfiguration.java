package com.epam.resourceprocessor.client.storage;

import com.epam.resourceprocessor.exception.feign.FeignErrorDecoder;
import feign.Feign;
import feign.Logger;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
import io.github.resilience4j.feign.FeignDecorators;
import io.github.resilience4j.feign.Resilience4jFeign;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor

@Configuration
public class StorageServiceFeignConfiguration {
    @Bean
    public Feign.Builder storageServiceFeignBuilder() {
        var decorators = FeignDecorators.builder().build();

        return Resilience4jFeign.builder(decorators)
                .logger(new Slf4jLogger(StorageServiceFeignClient.class))
                .logLevel(Logger.Level.FULL)
                .errorDecoder(new FeignErrorDecoder())
                .decoder(new JacksonDecoder())
                .encoder(new JacksonEncoder());
    }
}
