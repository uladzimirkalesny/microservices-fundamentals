package com.epam.songservice.controller;

import com.epam.songservice.dto.SongMetadata;
import com.epam.songservice.entity.Song;
import com.epam.songservice.service.SongService;
import com.epam.songservice.utils.RequestParamMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SongController.class)
class SongControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private SongService songService;

    @MockBean
    private RequestParamMapper requestParamMapper;

    @Test
    @DisplayName("Check started context")
    void check_context() {
        assertNotNull(mockMvc);
    }

    @Test
    @DisplayName("Check create song POST endpoint")
    void check_post_endpoint() throws Exception {
        var songMetadata = getSongMetadata();
        var song = getSong();

        given(songService.createSong(songMetadata)).willReturn(song);

        mockMvc.perform(post("/v1/songs/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(songMetadata)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("Check find song GET endpoint")
    void check_get_endpoint() throws Exception {
        var song = getSong();

        given(songService.findSongById(1)).willReturn(song);

        mockMvc.perform(get("/v1/songs/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("We are the champions"));
    }

    @Test
    @DisplayName("Check delete song DELETE endpoint")
    void check_delete_endpoint() throws Exception {
        var parsedList = List.of(1, 2);
        given(requestParamMapper.mapStringIdsToListOfIntegers("1,2")).willReturn(parsedList);
        doNothing().when(songService).deleteAllSongById(parsedList);

        mockMvc.perform(delete("/v1/songs?id=1,2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.ids[0]").value(1))
                .andExpect(jsonPath("$.ids[1]").value(2));
    }

    private Song getSong() {
        var song = new Song();
        song.setId(1);
        song.setName("We are the champions");
        song.setArtist("Queen");
        return song;
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
