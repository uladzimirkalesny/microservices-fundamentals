package com.epam.resourceprocessor.service;

import com.epam.resourceprocessor.domain.ResourceMetadata;
import com.epam.resourceprocessor.exception.ResourceProcessorException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Slf4j

@Service
public class ResourceMetadataParserService {
    private static final String NAME = "dc:title";
    private static final String ARTIST = "xmpDM:artist";
    private static final String ALBUM = "xmpDM:album";
    private static final String LENGTH = "xmpDM:duration";
    private static final String YEAR = "xmpDM:releaseDate";

    public ResourceMetadata extractResourceMetadata(byte[] resource) {
        try {
            var handler = new BodyContentHandler();
            var metadata = new Metadata();
            var inputStream = new ByteArrayInputStream(resource);
            var ctx = new ParseContext();

            var mp3Parser = new Mp3Parser();
            mp3Parser.parse(inputStream, handler, metadata, ctx);
            return mapMp3MetadataToResourceMetadata(metadata);
        } catch (Exception e) {
            log.error("Error occurred in time extracting metadata from binary resource : {}", e.getMessage());
            throw new ResourceProcessorException("Error occurred in time extracting metadata from binary resource");
        }
    }

    private ResourceMetadata mapMp3MetadataToResourceMetadata(Metadata mp3Metadata) {
        return ResourceMetadata.builder()
                .name(mp3Metadata.get(NAME))
                .artist(mp3Metadata.get(ARTIST))
                .album(mp3Metadata.get(ALBUM))
                .length(mp3Metadata.get(LENGTH))
                .year(Integer.valueOf(mp3Metadata.get(YEAR)))
                .build();
    }
}
