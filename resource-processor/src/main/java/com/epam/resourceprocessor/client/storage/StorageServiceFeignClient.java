package com.epam.resourceprocessor.client.storage;

import com.epam.resourceprocessor.domain.Storage;
import com.epam.resourceprocessor.domain.StorageType;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import static com.epam.resourceprocessor.client.storage.StorageServiceFeignClient.SERVICE_NAME;

@FeignClient(name = SERVICE_NAME, configuration = StorageServiceFeignConfiguration.class)
public interface StorageServiceFeignClient {
    String SERVICE_NAME = "STORAGE-SERVICE";
    String SERVICE_BASE_URL = "/v1/storages";
    String RE_UPLOAD_PERMANENT_BUCKET = "microservices-fundamentals";

    @CircuitBreaker(name = "storage-service-cb", fallbackMethod = "getAllStoragesFallback")
    @GetMapping(SERVICE_BASE_URL)
    List<Storage> getAllStorages();

    default List<Storage> getAllStoragesFallback(Throwable t) {
        return List.of(new Storage(null, StorageType.PERMANENT, RE_UPLOAD_PERMANENT_BUCKET));
    }
}
