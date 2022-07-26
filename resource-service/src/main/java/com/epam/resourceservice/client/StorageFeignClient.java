package com.epam.resourceservice.client;

import com.epam.resourceservice.dto.CreateStorageRequest;
import com.epam.resourceservice.dto.CreateStorageResponse;
import com.epam.resourceservice.model.Storage;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

import static com.epam.resourceservice.client.StorageFeignClient.SERVICE_NAME;

@FeignClient(name = SERVICE_NAME)
public interface StorageFeignClient {
    String SERVICE_NAME = "STORAGE-SERVICE";
    String SERVICE_BASE_URL = "/v1/storages";
    String CACHE_NAME = "storages";

    @GetMapping(SERVICE_BASE_URL)
    @Cacheable(value = CACHE_NAME, key = "#root.methodName")
    List<Storage> getAllStorages();

    @PostMapping(SERVICE_BASE_URL)
    @CachePut(value = CACHE_NAME)
    CreateStorageResponse createStorage(CreateStorageRequest createStorageRequest);
}
