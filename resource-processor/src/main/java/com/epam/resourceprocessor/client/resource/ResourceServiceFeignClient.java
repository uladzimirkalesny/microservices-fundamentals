package com.epam.resourceprocessor.client.resource;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = ResourceServiceFeignClient.SERVICE_NAME, configuration = ResourceServiceFeignConfiguration.class)
public interface ResourceServiceFeignClient {
    String SERVICE_NAME = "RESOURCE-SERVICE";

    @GetMapping("/v1/resources/{id}")
    byte[] getResourceByResourceId(@PathVariable("id") Integer id);
}
