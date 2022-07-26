package com.epam.storageservice.mapper;

import com.epam.storageservice.dto.CreateStorageRequest;
import com.epam.storageservice.entity.Storage;
import org.springframework.stereotype.Component;

@Component
public class StorageMapper {
    public Storage fromDto(CreateStorageRequest dto) {
        var storageCandidate = new Storage();
        storageCandidate.setStorageType(dto.getStorageType());
        storageCandidate.setBucket(dto.getBucket());

        return storageCandidate;
    }
}
