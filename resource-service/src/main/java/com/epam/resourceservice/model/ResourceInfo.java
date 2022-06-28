package com.epam.resourceservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.URL;

@AllArgsConstructor
@Data
public class ResourceInfo {
    private String key;
    private URL url;
}
