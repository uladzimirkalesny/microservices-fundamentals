package com.epam.resourceservice.service.storage;

import com.amazonaws.services.pi.model.InvalidArgumentException;
import com.epam.resourceservice.client.StorageFeignClient;
import com.epam.resourceservice.dto.CreateStorageRequest;
import com.epam.resourceservice.model.Storage;
import com.epam.resourceservice.model.StorageType;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

import static com.epam.resourceservice.client.StorageFeignClient.CACHE_NAME;

@RequiredArgsConstructor
@Slf4j

@Service
public class StorageService {
    private final StorageFeignClient storageFeignClient;
    private final CacheManager cacheManager;

    @CircuitBreaker(name = "storage-service-cb")
    public Storage obtainStagingStorage() {
        return storageFeignClient.getAllStorages()
                .stream()
                .filter(s -> s.getStorageType() == StorageType.STAGING)
                .findFirst()
                .orElseThrow(() -> new InvalidArgumentException("Prepare staging storage before."));
    }

    public Storage obtainStorageById(Integer storageId) {
        return storageFeignClient.getAllStorages()
                .stream()
                .filter(storage -> storage.getId().equals(storageId))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    @CircuitBreaker(name = "storage-service-cb", fallbackMethod = "createStorageFallback")
    public Integer createStorage(CreateStorageRequest createStorageRequest) {
        return storageFeignClient.getAllStorages()
                .stream()
                .filter(storage ->
                        createStorageRequest.getBucket().equals(storage.getBucket()) &&
                                createStorageRequest.getStorageType() == storage.getStorageType())
                .map(Storage::getId)
                .findFirst()
                .orElseGet(() -> storageFeignClient.createStorage(createStorageRequest).getId());
    }

    private Integer createStorageFallback(CreateStorageRequest createStorageRequest, Throwable t) {
        log.warn("Possible issue = {}. Look like storage service not available.", t.getMessage());
        log.info("Caching data does not contain information about storage = {}, try to save it", createStorageRequest);
        var list =
                Optional.ofNullable(cacheManager.getCache(CACHE_NAME).get("getAllStorages", ArrayList.class))
                        .orElseGet(ArrayList::new);
        int index = list.size();
        list.add(new Storage(++index, createStorageRequest.getStorageType(), createStorageRequest.getBucket()));
        return index;
    }

}
