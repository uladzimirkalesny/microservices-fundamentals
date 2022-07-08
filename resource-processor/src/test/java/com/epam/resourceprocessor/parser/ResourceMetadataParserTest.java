package com.epam.resourceprocessor.parser;

import com.epam.resourceprocessor.exception.ResourceProcessorException;
import org.apache.tika.metadata.Metadata;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {ResourceMetadataParser.class})
@ExtendWith(SpringExtension.class)
class ResourceMetadataParserTest {
    @Autowired
    private ResourceMetadataParser resourceMetadataParser;

    @Test
    @DisplayName("Test extract tika metadata from resource")
    void testExtractTikaMetadataFromResource() {
        var metadata = resourceMetadataParser.extractTikaMetadataFromResource(new byte[]{73, 68, 51, 3, 0, 0, 0, 7, 61, 69});
        assertAll(() -> {
            assertNotNull(metadata);
            assertNotNull(metadata.get("Content-Type"));
            assertEquals("audio/mpeg", metadata.get("Content-Type"));
        });
    }

    @Test
    @DisplayName("Test extract tika metadata from resource with mocked parser")
    void testExtractTikaMetadataFromResourceWithMockedParser() {
        var mockedParser = mock(ResourceMetadataParser.class);
        var tikaMetadata = new Metadata();

        when(mockedParser.extractTikaMetadataFromResource(any(byte[].class))).thenReturn(tikaMetadata);
        Metadata actual = mockedParser.extractTikaMetadataFromResource(new byte[]{});

        assertNotNull(actual);
        verify(mockedParser, atLeast(1)).extractTikaMetadataFromResource(any(byte[].class));
    }

    @Test
    @DisplayName("Test extract tika metadata from resource should throw instance of exception when processing invalid data")
    void testExtractTikaMetadataFromResource_shouldThrowAnException_WhenIncomingDataIsNullable() {
        assertThrows(ResourceProcessorException.class, () -> resourceMetadataParser.extractTikaMetadataFromResource(null));
    }
}

