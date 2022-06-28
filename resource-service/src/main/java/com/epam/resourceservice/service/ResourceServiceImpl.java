package com.epam.resourceservice.service;

import com.epam.resourceservice.entity.Resource;
import com.epam.resourceservice.repository.ResourceRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@RequiredArgsConstructor

@Service("resourceService")
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository resourceRepository;

    @Override
    @CircuitBreaker(name = "resource-db")
    @Retry(name = "resource-db")
    public Resource saveResource(Resource resource) {
        return resourceRepository.save(resource);
    }

    @Override
    @CircuitBreaker(name = "resource-db")
    @Retry(name = "resource-db")
    public Resource findResourceById(Integer id) {
        return resourceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Resource with id " + id + " not found"));
    }

    @Override
    @CircuitBreaker(name = "resource-db")
    @Retry(name = "resource-db")
    public String deleteResourceById(Integer id) {
        var foundResource = findResourceById(id);
        resourceRepository.deleteById(foundResource.getId());
        return foundResource.getResourceKey();
    }
}
