package com.epam.songservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DeleteSongResponse {
    private Iterable<Integer> ids;
}
