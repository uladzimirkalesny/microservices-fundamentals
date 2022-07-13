package com.epam.resourceprocessor.contract;

import com.epam.resourceprocessor.client.song.SongServiceFeignDecoratorClient;
import com.epam.resourceprocessor.dto.ResourceMetadataDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureStubRunner(ids = {"com.epam:song-service:1.0.0:stubs:9999"},
        stubsMode = StubRunnerProperties.StubsMode.LOCAL)
@DirtiesContext
class SongServiceClientTest {

    @Autowired
    private SongServiceFeignDecoratorClient client;

    @Test
    @DisplayName("Should create song metadata")
    void shouldCreateSongMetadata() {
        var resourceDto = ResourceMetadataDTO.builder()
                .name("We are the champions")
                .artist("Queen")
                .album("News of the world")
                .length("2:59")
                .year(1977)
                .resourceId(1)
                .build();

        var songServiceFeignClient = client.forTarget("http://localhost:9999/v1/songs");
        var songMetadata = songServiceFeignClient.createSongMetadata(resourceDto);

        assertAll(() -> {
            assertNotNull(songMetadata);
            assertEquals(1, songMetadata.getId());
        });
    }
}
