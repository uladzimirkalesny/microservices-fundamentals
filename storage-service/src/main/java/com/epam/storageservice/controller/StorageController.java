package com.epam.storageservice.controller;

import com.epam.storageservice.dto.CreateStorageRequest;
import com.epam.storageservice.dto.CreatedStorageResponse;
import com.epam.storageservice.dto.DeleteStorageResponse;
import com.epam.storageservice.entity.Storage;
import com.epam.storageservice.service.StorageService;
import com.epam.storageservice.utils.RequestParamMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

@Validated

@RequiredArgsConstructor
@Slf4j

@RestController
@RequestMapping("/v1/storages")
public class StorageController {

    private final StorageService storageService;
    private final RequestParamMapper requestParamMapper;

    @PostMapping
    public ResponseEntity<CreatedStorageResponse> createStorage(@Valid @RequestBody CreateStorageRequest createStorageRequest) {
        log.info("Start to create storage = {}", createStorageRequest.toString());
        var storage = storageService.createStorage(createStorageRequest);
        log.info("Successfully save storage = {}", storage.toString());
        return ResponseEntity.ok(new CreatedStorageResponse(storage.getId()));
    }

    @GetMapping
    public ResponseEntity<List<Storage>> getAllStorages() {
        log.info("Get all storages");
        var storages = storageService.getAllStorages();
        log.info("Successfully obtained storages = {}", storages.size());
        return ResponseEntity.ok(storages);
    }

    @DeleteMapping
    public ResponseEntity<DeleteStorageResponse> deleteStorage(@RequestParam(value = "id")
                                                               @Size(max = 200) String id) {
        log.info("Delete all storages with ids = {}", id);
        var ids = requestParamMapper.mapStringIdsToListOfIntegers(id);
        storageService.deleteAllStoragesByIds(ids);

        log.info("Successfully deleted all storages with ids = {}", ids);
        return ResponseEntity.ok(new DeleteStorageResponse(ids));
    }
}
