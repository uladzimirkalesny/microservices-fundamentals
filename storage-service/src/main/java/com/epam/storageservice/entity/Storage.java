package com.epam.storageservice.entity;

import com.epam.storageservice.domain.StorageType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter

@Entity
@Table(name = "STORAGES")
public class Storage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StorageType storageType;

    @Column(nullable = false)
    private String bucket;
}
