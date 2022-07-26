package com.epam.resourceservice.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "RESOURCES")
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String resourceKey;

    private String resourceLocation;

    private Integer storageId;

    public Resource(String resourceKey, String resourceLocation, Integer storageId) {
        this.resourceKey = resourceKey;
        this.resourceLocation = resourceLocation;
        this.storageId = storageId;
    }
}
