package com.epam.resourceservice.facade;

import com.epam.resourceservice.dto.CreateStorageRequest;
import com.epam.resourceservice.dto.message.ResourceMessage;
import com.epam.resourceservice.entity.Resource;
import com.epam.resourceservice.model.Storage;
import com.epam.resourceservice.service.ResourceService;
import com.epam.resourceservice.service.storage.CloudProviderSimpleStorageService;
import com.epam.resourceservice.service.storage.StorageService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@RequiredArgsConstructor

@Service
@Transactional
public class ResourceFacade {

    private final CloudProviderSimpleStorageService awsSimpleStorageService;
    private final ResourceService resourceService;
    private final RabbitTemplate rabbitTemplate;
    private final FanoutExchange fanoutExchange;
    private final StorageService storageService;

    @CircuitBreaker(name = "resource-processor")
    @Retry(name = "resource-processor")
    public Resource saveResource(byte[] resource) {
        log.info("Obtain staging storage from appropriate service");
        var stagingStorage = storageService.obtainStagingStorage();
        log.info("STAGING STORAGE = {}", stagingStorage);

        log.info("Prepare to save the binary resource into cloud storage");
        var resourceInfo = awsSimpleStorageService.uploadResourceToS3(resource, stagingStorage.getBucket());

        log.info("A binary resource has been saved into cloud storage");
        log.info("Prepare to save the binary resource into database");

        var savedResource =
                resourceService.saveResource(
                        new Resource(resourceInfo.getKey(), String.valueOf(resourceInfo.getUrl()), stagingStorage.getId()));

        log.info("Prepare to sending into RabbitMQ the binary resource with id = {}", savedResource.getId());
        rabbitTemplate.convertAndSend(fanoutExchange.getName(), new ResourceMessage(savedResource.getId()));

        return savedResource;
    }

    @CircuitBreaker(name = "resource-processor")
    @Retry(name = "resource-processor")
    public byte[] findResourceById(Integer id) {
        log.info("Prepare to find the binary resource by id = {} into database", id);
        var foundResource = resourceService.findResourceById(id);

        var storageId = foundResource.getStorageId();
        var foundStorage = storageService.obtainStorageById(storageId);

        var key = foundResource.getResourceKey();
        log.info("Prepare to obtain the binary resource from cloud storage by uuid = {}", key);
        return awsSimpleStorageService.downloadResourceFromS3(key, foundStorage.getBucket());
    }

    @CircuitBreaker(name = "resource-processor")
    @Retry(name = "resource-processor")
    public byte[] findRangedResourceById(Integer id, long startRange, long endRange) {
        log.info("Prepare to find the binary resource by id = {} into database", id);
        var foundResource = resourceService.findResourceById(id);

        var storageId = foundResource.getStorageId();
        var foundStorage = storageService.obtainStorageById(storageId);

        var key = foundResource.getResourceKey();
        log.info("Prepare to obtain the binary resource from cloud storage by uuid = {} range start with = {} end with = {}", key, startRange, endRange);
        return awsSimpleStorageService.downloadRangedResourceFromS3(key, foundStorage.getBucket(), startRange, endRange);
    }

    @CircuitBreaker(name = "resource-processor")
    @Retry(name = "resource-processor")
    public void deleteAllResourcesByIds(List<Integer> ids) {
        log.info("Prepare to delete all binary resources by ids = {}", ids);
        ids.forEach(id -> {
            var foundResource = resourceService.findResourceById(id);

            var storageId = foundResource.getStorageId();
            var foundStorage = storageService.obtainStorageById(storageId);

            var key = resourceService.deleteResourceById(id);
            awsSimpleStorageService.deleteResourceFromS3(key, foundStorage.getBucket());
        });
    }

    public Resource updateResourceStorageType(Integer resourceId, Storage permanentStorage) {
        if (permanentStorage.getId() == null) {
            var permanentStorageId = storageService.createStorage(
                    new CreateStorageRequest(permanentStorage.getStorageType(), permanentStorage.getBucket()));
            permanentStorage.setId(permanentStorageId);
        }

        var foundResource = resourceService.findResourceById(resourceId);
        var storageId = foundResource.getStorageId();
        log.info("Transfer resource with resource-id = {} from staging storage with storage-id = {} to permanent storage with storage-id = {}",
                resourceId, storageId, permanentStorage.getId());

        var foundStorage = storageService.obtainStorageById(storageId);
        log.info("Move from staging storage = {} and bucket = {}", foundStorage.getStorageType(), foundStorage.getBucket());
        log.info("Move to permanent storage = {} and bucket = {}", permanentStorage.getStorageType(), permanentStorage.getBucket());

        var updatedResourceInfo = awsSimpleStorageService.transferResourceToPermanentStorage(
                foundResource.getResourceKey(),
                foundStorage.getBucket(),
                permanentStorage.getBucket());

        log.info("Update and save Resource Info to values = {}", updatedResourceInfo.toString());
        foundResource.setStorageId(permanentStorage.getId());
        foundResource.setResourceKey(updatedResourceInfo.getKey());
        foundResource.setResourceLocation(String.valueOf(updatedResourceInfo.getUrl()));
        return resourceService.saveResource(foundResource);
    }
}
