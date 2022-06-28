package com.epam.resourceprocessor.client.resource;

import feign.Param;
import feign.RequestLine;

public interface ResourceServiceFeignClient {
    @RequestLine("GET /{id}")
    byte[] getResourceByResourceId(@Param("id") Integer id);
}
