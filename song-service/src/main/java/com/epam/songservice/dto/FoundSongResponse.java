package com.epam.songservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class FoundSongResponse {
    private String name;
    private String artist;
    private String album;
    private String length;
    private Integer resourceId;
    private Integer year;
}
