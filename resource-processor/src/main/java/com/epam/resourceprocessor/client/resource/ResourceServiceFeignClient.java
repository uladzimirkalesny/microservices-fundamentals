package com.epam.resourceprocessor.client.resource;

import com.epam.resourceprocessor.domain.Storage;
import com.epam.resourceprocessor.dto.CreateResourceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static com.epam.resourceprocessor.client.resource.ResourceServiceFeignClient.SERVICE_NAME;

@FeignClient(name = SERVICE_NAME, configuration = ResourceServiceFeignConfiguration.class)
public interface ResourceServiceFeignClient {
    String SERVICE_NAME = "RESOURCE-SERVICE";
    String SERVICE_BASE_URL = "/v1/resources/";

    @GetMapping(SERVICE_BASE_URL + "{id}")
    byte[] getResourceByResourceId(@PathVariable("id") Integer id);

    @PutMapping(SERVICE_BASE_URL + "{id}")
    CreateResourceResponse updateResourceStorageType(
            @PathVariable("id") Integer resourceId,
            @RequestBody Storage storage);
}
