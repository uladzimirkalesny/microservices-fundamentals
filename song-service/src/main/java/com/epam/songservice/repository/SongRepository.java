package com.epam.songservice.repository;

import com.epam.songservice.entity.Song;
import org.springframework.data.repository.CrudRepository;

public interface SongRepository extends CrudRepository<Song, Integer> {
}
