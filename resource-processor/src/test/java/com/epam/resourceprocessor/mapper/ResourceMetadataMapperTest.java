package com.epam.resourceprocessor.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.resourceprocessor.domain.ResourceMetadata;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ResourceMetadataMapper.class})
@ExtendWith(SpringExtension.class)
class ResourceMetadataMapperTest {

    @Autowired
    private ResourceMetadataMapper resourceMetadataMapper;

    @Test
    @DisplayName("Check the resource metadata mapper")
    void testToResourceMetadataDTO() {
        var resourceMetadata = mock(ResourceMetadata.class);

        when(resourceMetadata.getName()).thenReturn("We are the champions");
        when(resourceMetadata.getArtist()).thenReturn("Queen");
        when(resourceMetadata.getAlbum()).thenReturn("News of the world");
        when(resourceMetadata.getLength()).thenReturn("2:59");
        when(resourceMetadata.getYear()).thenReturn(1977);

        var actual = resourceMetadataMapper.toResourceMetadataDTO(resourceMetadata, 123);

        assertEquals("We are the champions", actual.getName());
        assertEquals("Queen", actual.getArtist());
        assertEquals("News of the world", actual.getAlbum());
        assertEquals("2:59", actual.getLength());
        assertEquals(123, actual.getResourceId().intValue());
        assertEquals(1977, actual.getYear().intValue());

        verify(resourceMetadata).getYear();
        verify(resourceMetadata).getAlbum();
        verify(resourceMetadata).getArtist();
        verify(resourceMetadata).getLength();
        verify(resourceMetadata).getName();
    }
}

