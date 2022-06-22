package com.epam.songservice.utils;

import com.epam.songservice.dto.SongMetadata;
import com.epam.songservice.entity.Song;
import org.springframework.stereotype.Component;

@Component
public class SongMapper {

    public Song songMetadataToSong(SongMetadata songMetadata) {
        return new Song(
                songMetadata.getName(),
                songMetadata.getArtist(),
                songMetadata.getAlbum(),
                songMetadata.getLength(),
                songMetadata.getResourceId(),
                songMetadata.getYear()
        );
    }

}
