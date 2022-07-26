package com.epam.resourceservice.dto;

import com.epam.resourceservice.model.StorageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateStorageRequest {
    @NotBlank
    private StorageType storageType;
    @NotBlank
    private String bucket;
}
