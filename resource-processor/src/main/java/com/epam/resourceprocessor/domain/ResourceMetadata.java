package com.epam.resourceprocessor.domain;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResourceMetadata {
    private String name;
    private String artist;
    private String album;
    private String length;
    private Integer year;
}

