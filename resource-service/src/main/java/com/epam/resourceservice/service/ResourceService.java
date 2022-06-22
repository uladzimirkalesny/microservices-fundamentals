package com.epam.resourceservice.service;

import com.epam.resourceservice.entity.Resource;

public interface ResourceService {
    Resource saveResource(Resource resource);

    Resource findResourceById(Integer id);

    String deleteResourceById(Integer id);
}
