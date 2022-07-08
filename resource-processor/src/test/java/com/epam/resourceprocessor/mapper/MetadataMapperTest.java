package com.epam.resourceprocessor.mapper;

import org.apache.tika.metadata.Metadata;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {MetadataMapper.class})
@ExtendWith(SpringExtension.class)
class MetadataMapperTest {

    @Autowired
    private MetadataMapper metadataMapper;

    @Test
    @DisplayName("Test tika metadata to resources metadata")
    void testTikaMetadataToResourceMetadata() {
        var metadata = new Metadata();
        metadata.set("dc:title", "We are the champions");
        metadata.set("xmpDM:artist", "Queen");
        metadata.set("xmpDM:album", "News of the world");
        metadata.set("xmpDM:duration", "2:59");
        metadata.set("xmpDM:releaseDate", "1977");

        var actual = metadataMapper.tikaMetadataToResourceMetadata(metadata);

        assertAll(() -> {
            assertEquals("We are the champions", actual.getName());
            assertEquals("Queen", actual.getArtist());
            assertEquals("News of the world", actual.getAlbum());
            assertEquals("2:59", actual.getLength());
            assertEquals(1977, actual.getYear().intValue());
        });
    }

    @Test
    @DisplayName("Test tika metadata to resources metadata with mocked data")
    void testTikaMetadataToResourceMetadata_withMockedData() {
        var mockedMetadata = mock(Metadata.class);

        when(mockedMetadata.get("xmpDM:artist")).thenReturn("Queen");
        when(mockedMetadata.get("xmpDM:releaseDate")).thenReturn("1977");

        var actual = metadataMapper.tikaMetadataToResourceMetadata(mockedMetadata);

        assertEquals("Queen", actual.getArtist());
        assertEquals(1977, actual.getYear());

        verify(mockedMetadata, atLeast(1)).get("xmpDM:artist");
        verify(mockedMetadata, atLeast(1)).get("xmpDM:releaseDate");
    }
}

