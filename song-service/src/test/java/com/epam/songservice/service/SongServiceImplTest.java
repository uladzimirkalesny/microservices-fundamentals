package com.epam.songservice.service;

import com.epam.songservice.dto.SongMetadata;
import com.epam.songservice.entity.Song;
import com.epam.songservice.repository.SongRepository;
import com.epam.songservice.utils.SongMapper;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {SongServiceImpl.class})
@ExtendWith(SpringExtension.class)
class SongServiceImplTest {
    @MockBean
    private SongMapper songMapper;

    @MockBean
    private SongRepository songRepository;

    @Autowired
    private SongServiceImpl songServiceImpl;

    @Test
    @DisplayName("Check create song method from Song service")
    void check_createSong_method() {
        var song = getSongEntity();

        var mappedSong = new Song();
        mappedSong.setId(1);
        mappedSong.setName("We are the champions");
        mappedSong.setArtist("Queen");
        mappedSong.setAlbum("News of the world");
        mappedSong.setLength("2:59");
        mappedSong.setResourceId(1);
        mappedSong.setYear(1977);

        when(songRepository.save(any(Song.class))).thenReturn(song);
        when(songMapper.songMetadataToSong(any(SongMetadata.class))).thenReturn(mappedSong);

        assertSame(song, songServiceImpl.createSong(new SongMetadata()));
        verify(songRepository).save(any(Song.class));
        verify(songMapper).songMetadataToSong(any(SongMetadata.class));
    }

    @Test
    @DisplayName("Check create song method from Song service that throws an EntityNotFoundException")
    void check_createSong_throw_EntityNotFoundException() {
        var song = getSongEntity();

        when(songRepository.save(any(Song.class))).thenReturn(song);
        when(songMapper.songMetadataToSong(any(SongMetadata.class)))
                .thenThrow(new EntityNotFoundException("Entity song not found"));

        assertThrows(EntityNotFoundException.class, () -> songServiceImpl.createSong(new SongMetadata()));
        verify(songMapper).songMetadataToSong(any(SongMetadata.class));
    }

    @Test
    @DisplayName("Check find song by id method from Song service")
    void check_findById_success() {
        var song = getSongEntity();
        var foundSong = Optional.of(song);

        when(songRepository.findById(any(Integer.class))).thenReturn(foundSong);
        assertSame(song, songServiceImpl.findSongById(1));
        verify(songRepository).findById(any(Integer.class));
    }

    @Test
    @DisplayName("Check find song by id method from Song service that throws an EntityNotFoundException")
    void check_findById_throws_EntityNotFoundException() {
        when(songRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> songServiceImpl.findSongById(1));
        verify(songRepository).findById(any(Integer.class));
    }

    @Test
    @DisplayName("Check method find song by id from Song service that throws an EntityNotFoundException")
    void check_findById_throws_EntityNotFoundException2() {
        when(songRepository.findById(any(Integer.class)))
                .thenThrow(new EntityNotFoundException("An error occurred"));
        assertThrows(EntityNotFoundException.class, () -> songServiceImpl.findSongById(1));
        verify(songRepository).findById(any(Integer.class));
    }

    @Test
    @DisplayName("Check delete all song by id method from Song service")
    void check_deleteAllSongById() {
        doNothing().when(songRepository).deleteAllById(any());
        songServiceImpl.deleteAllSongById(new ArrayList<>());
        verify(songRepository).deleteAllById(any());
    }

    @Test
    @DisplayName("Check method from Song service")
    void check_deleteAllSongById_throws_EntityNotFoundException() {
        doThrow(new EntityNotFoundException("An error occurred"))
                .when(songRepository).deleteAllById(any());
        assertThrows(EntityNotFoundException.class, () -> songServiceImpl.deleteAllSongById(new ArrayList<>()));
        verify(songRepository).deleteAllById(any());
    }

    @NotNull
    private Song getSongEntity() {
        Song song = new Song();
        song.setId(1);
        song.setName("We are the champions");
        song.setArtist("Queen");
        song.setAlbum("News of the world");
        song.setLength("2:59");
        song.setResourceId(1);
        song.setYear(1977);
        return song;
    }
}

