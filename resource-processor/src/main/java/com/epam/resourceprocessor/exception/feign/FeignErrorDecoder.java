package com.epam.resourceprocessor.exception.feign;

import com.epam.resourceprocessor.exception.FeignCommunicationApiException;
import feign.Response;
import feign.codec.ErrorDecoder;

import static feign.FeignException.errorStatus;

public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() >= 400 && response.status() <= 499) {
            return new FeignCommunicationApiException(response.reason());
        }
        if (response.status() >= 500 && response.status() <= 599) {
            return new FeignCommunicationApiException(response.reason());
        }
        return errorStatus(methodKey, response);
    }
}
