package com.epam.resourceprocessor.parser;

import com.epam.resourceprocessor.exception.ResourceProcessorException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

@Slf4j

@Component
public class ResourceMetadataParser {
    public Metadata extractTikaMetadataFromResource(byte[] resource) {
        try {
            var handler = new BodyContentHandler();
            var metadata = new Metadata();
            var inputStream = new ByteArrayInputStream(resource);
            var ctx = new ParseContext();

            var mp3Parser = new Mp3Parser();
            mp3Parser.parse(inputStream, handler, metadata, ctx);
            return metadata;
        } catch (Exception e) {
            log.error("Error occurred in time extracting metadata from binary resource : {}", e.getMessage());
            throw new ResourceProcessorException("Error occurred in time extracting metadata from binary resource");
        }
    }
}
