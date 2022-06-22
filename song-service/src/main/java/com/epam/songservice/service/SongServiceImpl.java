package com.epam.songservice.service;

import com.epam.songservice.dto.SongMetadata;
import com.epam.songservice.entity.Song;
import com.epam.songservice.repository.SongRepository;
import com.epam.songservice.utils.SongMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor

@Service("songService")
public class SongServiceImpl implements SongService {

    private final SongRepository songRepository;
    private final SongMapper songMapper;

    @Override
    public Song createSong(SongMetadata songMetadata) {
        log.info("Prepare to save song metadata into database : {}", songMetadata);
        return songRepository.save(songMapper.songMetadataToSong(songMetadata));
    }

    @Override
    public Song findSongById(Integer id) {
        log.info("Prepare to obtain song from database by id = {}", id);
        return songRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity with id = " + id + " not found"));
    }

    @Override
    public void deleteAllSongById(List<Integer> ids) {
        log.info("Prepare to delete all songs from database by ids = {}", ids);
        songRepository.deleteAllById(ids);
    }
}
