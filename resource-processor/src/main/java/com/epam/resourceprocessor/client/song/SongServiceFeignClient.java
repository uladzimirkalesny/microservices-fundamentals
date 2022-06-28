package com.epam.resourceprocessor.client.song;

import com.epam.resourceprocessor.dto.CreateSongResponse;
import com.epam.resourceprocessor.dto.ResourceMetadataDTO;
import feign.Headers;
import feign.RequestLine;

public interface SongServiceFeignClient {
    @RequestLine("POST")
    @Headers({"Content-Type: application/json"})
    CreateSongResponse createSongMetadata(ResourceMetadataDTO resourceMetadata);
}
