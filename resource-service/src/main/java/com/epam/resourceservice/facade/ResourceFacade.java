package com.epam.resourceservice.facade;

import com.epam.resourceservice.dto.message.ResourceMessage;
import com.epam.resourceservice.entity.Resource;
import com.epam.resourceservice.service.ResourceService;
import com.epam.resourceservice.service.storage.CloudProviderSimpleStorageService;
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

    @CircuitBreaker(name = "resource-processor")
    @Retry(name = "resource-processor")
    public Resource saveResource(byte[] resource) {
        log.info("Prepare to save the binary resource into cloud storage");
        var resourceInfo = awsSimpleStorageService.uploadResourceToS3(resource);

        log.info("A binary resource has been saved into cloud storage");
        log.info("Prepare to save the binary resource into database");

        var savedResource = resourceService.saveResource(new Resource(resourceInfo.getKey(), String.valueOf(resourceInfo.getUrl())));

        log.info("Prepare to sending into RabbitMQ the binary resource with id = {}", savedResource.getId());
        rabbitTemplate.convertAndSend(fanoutExchange.getName(), new ResourceMessage(savedResource.getId()));

        return savedResource;
    }

    @CircuitBreaker(name = "resource-processor")
    @Retry(name = "resource-processor")
    public byte[] findResourceById(Integer id) {
        log.info("Prepare to find the binary resource by id = {} into database", id);
        var foundResource = resourceService.findResourceById(id);

        var key = foundResource.getResourceKey();
        log.info("Prepare to obtain the binary resource from cloud storage by uuid = {}", key);
        return awsSimpleStorageService.downloadResourceFromS3(key);
    }

    @CircuitBreaker(name = "resource-processor")
    @Retry(name = "resource-processor")
    public byte[] findRangedResourceById(Integer id, long startRange, long endRange) {
        log.info("Prepare to find the binary resource by id = {} into database", id);
        var foundResource = resourceService.findResourceById(id);

        var key = foundResource.getResourceKey();
        log.info("Prepare to obtain the binary resource from cloud storage by uuid = {} range start with = {} end with = {}", key, startRange, endRange);
        return awsSimpleStorageService.downloadRangedResourceFromS3(key, startRange, endRange);
    }

    @CircuitBreaker(name = "resource-processor")
    @Retry(name = "resource-processor")
    public void deleteAllResourcesByIds(List<Integer> ids) {
        log.info("Prepare to delete all binary resources by ids = {}", ids);
        ids.forEach(id -> {
            var key = resourceService.deleteResourceById(id);
            awsSimpleStorageService.deleteResourceFromS3(key);
        });
    }
}
