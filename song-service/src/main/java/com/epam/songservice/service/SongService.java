package com.epam.songservice.service;

import com.epam.songservice.dto.SongMetadata;
import com.epam.songservice.entity.Song;

import java.util.List;

public interface SongService {
    Song createSong(SongMetadata songMetadata);

    Song findSongById(Integer id);

    void deleteAllSongById(List<Integer> ids);
}
