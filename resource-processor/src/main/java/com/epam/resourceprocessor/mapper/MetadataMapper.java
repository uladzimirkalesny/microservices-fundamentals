package com.epam.resourceprocessor.mapper;

import com.epam.resourceprocessor.domain.ResourceMetadata;
import org.apache.tika.metadata.Metadata;
import org.springframework.stereotype.Component;

@Component
public class MetadataMapper {
    private static final String NAME = "dc:title";
    private static final String ARTIST = "xmpDM:artist";
    private static final String ALBUM = "xmpDM:album";
    private static final String LENGTH = "xmpDM:duration";
    private static final String YEAR = "xmpDM:releaseDate";

    public ResourceMetadata tikaMetadataToResourceMetadata(Metadata metadata) {
        return ResourceMetadata.builder()
                .name(metadata.get(NAME))
                .artist(metadata.get(ARTIST))
                .album(metadata.get(ALBUM))
                .length(metadata.get(LENGTH))
                .year(Integer.valueOf(metadata.get(YEAR)))
                .build();
    }
}
