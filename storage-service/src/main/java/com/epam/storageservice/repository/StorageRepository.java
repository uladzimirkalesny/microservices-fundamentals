package com.epam.storageservice.repository;

import com.epam.storageservice.entity.Storage;
import org.springframework.data.repository.CrudRepository;

public interface StorageRepository extends CrudRepository<Storage, Integer> {
}
