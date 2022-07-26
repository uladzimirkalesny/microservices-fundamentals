package com.epam.resourceservice.service.storage;

import com.epam.resourceservice.model.ResourceInfo;

public interface CloudProviderSimpleStorageService {
    ResourceInfo uploadResourceToS3(byte[] resource, String bucketName);

    byte[] downloadResourceFromS3(String key, String bucketName);

    byte[] downloadRangedResourceFromS3(String key, String bucketName, long startRange, long endRange);

    void deleteResourceFromS3(String key, String bucketName);

    ResourceInfo transferResourceToPermanentStorage(String key, String fromBucket, String toBucket);
}
