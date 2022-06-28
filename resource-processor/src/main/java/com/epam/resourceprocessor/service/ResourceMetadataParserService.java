package com.epam.resourceprocessor.service;

import com.epam.resourceprocessor.client.resource.ResourceServiceFeignDecoratorClient;
import com.epam.resourceprocessor.client.song.SongServiceFeignDecoratorClient;
import com.epam.resourceprocessor.dto.ResourceMessage;
import com.epam.resourceprocessor.exception.FeignCommunicationApiException;
import com.epam.resourceprocessor.mapper.MetadataMapper;
import com.epam.resourceprocessor.mapper.ResourceMetadataMapper;
import com.epam.resourceprocessor.parser.ResourceMetadataParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j

@Service
public class ResourceMetadataParserService {

    private final SongServiceFeignDecoratorClient songServiceFeignDecoratorClient;
    private final ResourceServiceFeignDecoratorClient resourceServiceFeignDecoratorClient;
    private final ResourceMetadataParser resourceMetadataParser;
    private final MetadataMapper metadataMapper;
    private final ResourceMetadataMapper resourceMetadataMapper;

    @Value("${resource.service.target.uri}")
    private String resourceServiceUri;

    @Value("${song.service.target.uri}")
    private String songServiceUri;

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void processResourceMetadataByResourceId(ResourceMessage resourceMessage) {
        try {
            log.info("RabbitMQ Listener read message from queue : {}", resourceMessage);
            var resource = resourceServiceFeignDecoratorClient.forTarget(resourceServiceUri)
                    .getResourceByResourceId(resourceMessage.getResourceId());
            log.info("Successfully received the binary resource from resource-service with id = {}", resourceMessage.getResourceId());

            log.info("Prepare to process metadata obtaining for resource with id = {}", resourceMessage.getResourceId());
            var metadata = resourceMetadataParser.extractTikaMetadataFromResource(resource);
            var resourceMetadata = metadataMapper.tikaMetadataToResourceMetadata(metadata);
            log.info("Resource Metadata successfully obtained : {} for binary resource with id = {}", resourceMetadata, resourceMessage.getResourceId());

            var createdSong = songServiceFeignDecoratorClient.forTarget(songServiceUri)
                    .createSongMetadata(resourceMetadataMapper.toResourceMetadataDTO(resourceMetadata, resourceMessage.getResourceId()));
            log.info("Successfully saved resource metadata into song-service with song-id = {} and resource-id = {}", createdSong.getId(), resourceMessage.getResourceId());
        } catch (FeignCommunicationApiException feignException) {
            log.error("Error occurred in time communication with another service: " +
                    "Message : {} will be placed into dead letter queue", resourceMessage, feignException.getCause());
        } catch (Exception e) {
            log.error("Error occurred: {}. The message with resource-id = {} will be placed into dead letter queue", e.getMessage(), resourceMessage.getResourceId());
        }
    }
}
