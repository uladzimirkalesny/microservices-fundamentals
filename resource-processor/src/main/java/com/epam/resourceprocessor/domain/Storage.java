package com.epam.resourceprocessor.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Storage {
    private Integer id;
    private StorageType storageType;
    private String bucket;
}
