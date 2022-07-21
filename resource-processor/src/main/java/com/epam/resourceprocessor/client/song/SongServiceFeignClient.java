package com.epam.resourceprocessor.client.song;

import com.epam.resourceprocessor.dto.CreateSongResponse;
import com.epam.resourceprocessor.dto.ResourceMetadataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = SongServiceFeignClient.SERVICE_NAME, configuration = SongServiceFeignConfiguration.class)
public interface SongServiceFeignClient {

    String SERVICE_NAME = "SONG-SERVICE";

    @PostMapping("/v1/songs/")
    CreateSongResponse createSongMetadata(@RequestBody ResourceMetadataDTO resourceMetadata);
}
