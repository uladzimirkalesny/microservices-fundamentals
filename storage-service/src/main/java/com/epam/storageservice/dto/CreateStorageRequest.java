package com.epam.storageservice.dto;

import com.epam.storageservice.domain.StorageType;
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
