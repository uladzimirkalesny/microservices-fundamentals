package com.epam.resourceservice.service;

import com.epam.resourceservice.entity.Resource;
import com.epam.resourceservice.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@RequiredArgsConstructor

@Service("resourceService")
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository resourceRepository;

    @Override
    public Resource saveResource(Resource resource) {
        return resourceRepository.save(resource);
    }

    @Override
    public Resource findResourceById(Integer id) {
        return resourceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Resource with id " + id + " not found"));
    }

    @Override
    public String deleteResourceById(Integer id) {
        var foundResource = findResourceById(id);
        resourceRepository.deleteById(foundResource.getId());
        return foundResource.getResourceKey();
    }
}
