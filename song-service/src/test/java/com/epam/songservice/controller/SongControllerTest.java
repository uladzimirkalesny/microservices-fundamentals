package com.epam.songservice.controller;

import com.epam.songservice.dto.SongMetadata;
import com.epam.songservice.entity.Song;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(
        properties = {"server.port:0", "eureka.client.enabled:false"},
        webEnvironment = RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
class SongControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeEach
    void save_shouldCreateSong() {
        var songMetadata = getSongMetadata();
        Map<String, Object> songs = testRestTemplate.postForObject("/v1/songs", songMetadata, Map.class);
        assertNotNull(songs);
    }

    @Test
    @DisplayName("Check the client interaction")
    void findAll_shouldReturnList() {
        var song = testRestTemplate.getForObject("/v1/songs/1", Song.class);
        assertNotNull(song);
    }

    private SongMetadata getSongMetadata() {
        return new SongMetadata(
                "We are the champions",
                "Queen",
                "News of the world",
                "2:59",
                1,
                1977);
    }
}
