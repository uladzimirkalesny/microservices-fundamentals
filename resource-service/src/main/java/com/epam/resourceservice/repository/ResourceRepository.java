package com.epam.resourceservice.repository;

import com.epam.resourceservice.entity.Resource;
import org.springframework.data.repository.CrudRepository;

public interface ResourceRepository extends CrudRepository<Resource, Integer> {
}
