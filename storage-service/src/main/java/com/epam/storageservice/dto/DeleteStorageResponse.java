package com.epam.storageservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DeleteStorageResponse {
    private Iterable<Integer> ids;
}
