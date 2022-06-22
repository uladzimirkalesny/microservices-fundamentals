package com.epam.songservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongMetadata {
    @NotNull
    private String name;
    @NotNull
    private String artist;
    @NotNull
    private String album;
    @NotNull
    private String length;
    @Positive
    private Integer resourceId;
    @Positive
    private Integer year;
}
