package com.epam.songservice.controller;

import com.epam.songservice.dto.CreateSongResponse;
import com.epam.songservice.dto.DeleteSongResponse;
import com.epam.songservice.dto.FoundSongResponse;
import com.epam.songservice.dto.SongMetadata;
import com.epam.songservice.entity.Song;
import com.epam.songservice.service.SongService;
import com.epam.songservice.utils.RequestParamMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Size;

@Slf4j
@RequiredArgsConstructor

@RestController
@RequestMapping("/v1/songs")
public class SongController {

    private final SongService songService;
    private final RequestParamMapper requestParamMapper;

    @PostMapping
    public ResponseEntity<CreateSongResponse> createSong(@Valid
                                                         @RequestBody SongMetadata songMetadata) {
        log.info("Start to save song metadata into database : {}", songMetadata);
        var createdSong = songService.createSong(songMetadata);

        log.info("Song has been saved into database with id = {}", createdSong.getId());
        return ResponseEntity.ok(new CreateSongResponse(createdSong.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoundSongResponse> findSong(@PathVariable Integer id) {
        log.info("Start to obtain song by id = {}", id);
        Song foundSong = songService.findSongById(id);

        log.info("Song has been obtained from database with id = {}", foundSong.getId());
        return ResponseEntity.ok(new FoundSongResponse(
                foundSong.getName(), foundSong.getArtist(), foundSong.getAlbum(),
                foundSong.getLength(), foundSong.getResourceId(), foundSong.getYear()
        ));
    }

    @DeleteMapping
    public ResponseEntity<DeleteSongResponse> deleteSong(@RequestParam(value = "id")
                                                         @Size(max = 200) String id) {
        var ids = requestParamMapper.mapStringIdsToListOfIntegers(id);
        log.info("Start to delete all songs from database by ids = {}", ids);
        songService.deleteAllSongById(ids);

        log.info("Successfully deleted all songs from database by ids = {}", ids);
        return ResponseEntity.ok(new DeleteSongResponse(ids));
    }

}
