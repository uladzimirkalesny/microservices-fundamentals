package com.epam.songservice.service;

import com.epam.songservice.dto.SongMetadata;
import com.epam.songservice.repository.SongRepository;
import com.epam.songservice.utils.SongMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.OracleContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest({"server.port:0", "eureka.client.enabled:false"})
class SongServiceTestContainersTest {

    @Container
    private static final OracleContainer DATABASE_CONTAINER = new OracleContainer("gvenzl/oracle-xe");

    @DynamicPropertySource
    private static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", DATABASE_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", DATABASE_CONTAINER::getUsername);
        registry.add("spring.datasource.password", DATABASE_CONTAINER::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private SongServiceImpl songServiceImpl;

    @Autowired
    private SongMapper songMapper;

    @Autowired
    private SongRepository songRepository;

    @Test
    @DisplayName("Check start context")
    void check_contextStarts() {
        assertThat(songServiceImpl).isNotNull();
    }

    @Test
    @DisplayName("Check integration between song service and oracle test container")
    void check_oracle_test_container_integration() {
        var songMetadata = new SongMetadata();
        songMetadata.setName("We are the champions");
        songMetadata.setArtist("Queen");
        songMetadata.setAlbum("News of the world");
        songMetadata.setLength("2:59");
        songMetadata.setResourceId(1);
        songMetadata.setYear(1977);

        var actual = songServiceImpl.createSong(songMetadata);
        assertThat(actual).isNotNull();

        var result = songServiceImpl.findSongById(actual.getId());
        assertThat(result).isNotNull();
    }
}
