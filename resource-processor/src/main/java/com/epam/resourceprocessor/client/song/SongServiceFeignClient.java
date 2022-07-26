package com.epam.resourceprocessor.client.song;

import com.epam.resourceprocessor.dto.CreateSongResponse;
import com.epam.resourceprocessor.dto.ResourceMetadataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.epam.resourceprocessor.client.song.SongServiceFeignClient.SERVICE_NAME;

@FeignClient(name = SERVICE_NAME, configuration = SongServiceFeignConfiguration.class)
public interface SongServiceFeignClient {
    String SERVICE_NAME = "SONG-SERVICE";
    String SERVICE_BASE_URL = "/v1/songs/";

    @PostMapping(SERVICE_BASE_URL)
    CreateSongResponse createSongMetadata(@RequestBody ResourceMetadataDTO resourceMetadata);
}
