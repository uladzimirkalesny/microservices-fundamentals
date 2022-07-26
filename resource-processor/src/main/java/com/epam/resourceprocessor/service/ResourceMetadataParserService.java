package com.epam.resourceprocessor.service;

import com.epam.resourceprocessor.client.resource.ResourceServiceFeignClient;
import com.epam.resourceprocessor.client.song.SongServiceFeignClient;
import com.epam.resourceprocessor.client.storage.StorageServiceFeignClient;
import com.epam.resourceprocessor.domain.Storage;
import com.epam.resourceprocessor.domain.StorageType;
import com.epam.resourceprocessor.dto.ResourceMessage;
import com.epam.resourceprocessor.exception.FeignCommunicationApiException;
import com.epam.resourceprocessor.mapper.MetadataMapper;
import com.epam.resourceprocessor.mapper.ResourceMetadataMapper;
import com.epam.resourceprocessor.parser.ResourceMetadataParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j

@Service
public class ResourceMetadataParserService {

    private final SongServiceFeignClient songServiceFeignClient;
    private final ResourceServiceFeignClient resourceServiceFeignClient;
    private final StorageServiceFeignClient storageServiceFeignClient;
    private final ResourceMetadataParser resourceMetadataParser;
    private final MetadataMapper metadataMapper;
    private final ResourceMetadataMapper resourceMetadataMapper;

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void processResourceMetadataByResourceId(ResourceMessage resourceMessage) {
        try {
            log.info("RabbitMQ Listener read message from queue : {}", resourceMessage);
            var resource = resourceServiceFeignClient.getResourceByResourceId(resourceMessage.getResourceId());
            log.info("Successfully received the binary resource from resource-service with id = {}", resourceMessage.getResourceId());

            log.info("Prepare to process metadata obtaining for resource with id = {}", resourceMessage.getResourceId());
            var metadata = resourceMetadataParser.extractTikaMetadataFromResource(resource);
            var resourceMetadata = metadataMapper.tikaMetadataToResourceMetadata(metadata);
            log.info("Resource Metadata successfully obtained : {} for binary resource with id = {}", resourceMetadata, resourceMessage.getResourceId());

            var createdSong = songServiceFeignClient.createSongMetadata(resourceMetadataMapper.toResourceMetadataDTO(resourceMetadata, resourceMessage.getResourceId()));
            log.info("Successfully saved resource metadata into song-service with song-id = {} and resource-id = {}", createdSong.getId(), resourceMessage.getResourceId());

            var permanentStorage = obtainRandomPermanentStorage();
            if (permanentStorage.getId() != null) {
                log.info("Obtain the permanent storage type with storage-id = {}", permanentStorage.getId());
            } else {
                log.info("Obtain predefined permanent storage = {}", permanentStorage);
            }
            var updatedResource = resourceServiceFeignClient
                    .updateResourceStorageType(resourceMessage.getResourceId(), permanentStorage);
            log.info("Successfully updated resource with resource-id = {}", updatedResource.getId());
        } catch (FeignCommunicationApiException feignException) {
            log.error("Error occurred in time communication with another service: " +
                    "Message : {} will be placed into dead letter queue", resourceMessage, feignException.getCause());
        } catch (Exception e) {
            log.error("Error occurred: {}. The message with resource-id = {} will be placed into dead letter queue", e.getMessage(), resourceMessage.getResourceId());
        }
    }

    private Storage obtainRandomPermanentStorage() {
        return storageServiceFeignClient.getAllStorages()
                .stream()
                .filter(s -> s.getStorageType() == StorageType.PERMANENT)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Add permanent storage before"));
    }
}
