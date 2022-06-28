package com.epam.resourceprocessor.mapper;

import com.epam.resourceprocessor.domain.ResourceMetadata;
import com.epam.resourceprocessor.dto.ResourceMetadataDTO;
import org.springframework.stereotype.Component;

@Component
public class ResourceMetadataMapper {
    public ResourceMetadataDTO toResourceMetadataDTO(ResourceMetadata resourceMetadata, Integer resourceId) {
        return ResourceMetadataDTO.builder()
                .artist(resourceMetadata.getArtist())
                .album(resourceMetadata.getAlbum())
                .name(resourceMetadata.getName())
                .length(resourceMetadata.getLength())
                .year(resourceMetadata.getYear())
                .resourceId(resourceId)
                .build();
    }
}
