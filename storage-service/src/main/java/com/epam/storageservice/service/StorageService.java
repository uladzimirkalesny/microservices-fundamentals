package com.epam.storageservice.service;

import com.epam.storageservice.dto.CreateStorageRequest;
import com.epam.storageservice.entity.Storage;
import com.epam.storageservice.mapper.StorageMapper;
import com.epam.storageservice.repository.StorageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor

@Service
public class StorageService {

    private final StorageMapper storageMapper;
    private final StorageRepository storageRepository;

    public Storage createStorage(CreateStorageRequest createStorageRequest) {
        var storage = storageMapper.fromDto(createStorageRequest);
        return storageRepository.save(storage);
    }

    public List<Storage> getAllStorages() {
        return StreamSupport.stream(storageRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public void deleteAllStoragesByIds(List<Integer> ids) {
        ids.forEach(storageRepository::deleteById);
    }
}
