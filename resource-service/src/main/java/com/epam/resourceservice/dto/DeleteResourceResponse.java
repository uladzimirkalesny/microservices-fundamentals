package com.epam.resourceservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DeleteResourceResponse {
    private Iterable<Integer> ids;
}
