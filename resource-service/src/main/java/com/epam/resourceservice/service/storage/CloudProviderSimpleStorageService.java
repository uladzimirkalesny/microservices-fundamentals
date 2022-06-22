package com.epam.resourceservice.service.storage;

import com.epam.resourceservice.model.ResourceInfo;

public interface CloudProviderSimpleStorageService {
    ResourceInfo uploadResourceToS3(byte[] resource);

    byte[] downloadResourceFromS3(String key);

    byte[] downloadRangedResourceFromS3(String key, long startRange, long endRange);

    void deleteResourceFromS3(String key);
}
