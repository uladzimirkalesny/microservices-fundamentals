package com.epam.songservice.utils;

import com.epam.songservice.dto.SongMetadata;
import com.epam.songservice.entity.Song;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {SongMapper.class})
@ExtendWith(SpringExtension.class)
class SongMapperTest {

    @Autowired
    private SongMapper songMapper;

    @Test
    @DisplayName("Check song mapper that should map song metadata to song")
    void test_SongMetadataToSong() {
        var songMetadata = new SongMetadata();
        songMetadata.setName("We are the champions");
        songMetadata.setArtist("Queen");
        songMetadata.setAlbum("News of the world");
        songMetadata.setLength("2:59");
        songMetadata.setResourceId(1);
        songMetadata.setYear(1977);

        Song song = songMapper.songMetadataToSong(songMetadata);
        assertEquals(songMetadata.getName(), song.getName());
        assertEquals(songMetadata.getArtist(), song.getArtist());
        assertEquals(songMetadata.getAlbum(), song.getAlbum());
        assertEquals(songMetadata.getLength(), song.getLength());
        assertEquals(songMetadata.getResourceId(), song.getResourceId());
        assertEquals(songMetadata.getYear(), song.getYear());
    }

    @Test
    @DisplayName("Check song mapper that should map song metadata to song with verifying")
    void test_SongMetadataToSong_with_verify() {
        var songMetadata = mock(SongMetadata.class);

        when(songMetadata.getName()).thenReturn("We are the champions");
        when(songMetadata.getArtist()).thenReturn("Queen");
        when(songMetadata.getAlbum()).thenReturn("News of the world");
        when(songMetadata.getLength()).thenReturn("2:59");
        when(songMetadata.getResourceId()).thenReturn(1);
        when(songMetadata.getYear()).thenReturn(1977);

        var song = songMapper.songMetadataToSong(songMetadata);

        assertEquals("We are the champions", song.getName());
        assertEquals("Queen", song.getArtist());
        assertEquals("News of the world", song.getAlbum());
        assertEquals("2:59", song.getLength());
        assertEquals(1, song.getResourceId().intValue());
        assertEquals(1977, song.getYear().intValue());

        verify(songMetadata).getName();
        verify(songMetadata).getArtist();
        verify(songMetadata).getAlbum();
        verify(songMetadata).getLength();
        verify(songMetadata).getResourceId();
        verify(songMetadata).getYear();
    }
}

