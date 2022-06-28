package com.epam.resourceprocessor.exception;

public class FeignCommunicationApiException extends ResourceProcessorException {
    public FeignCommunicationApiException() {
        super();
    }

    public FeignCommunicationApiException(String message) {
        super(message);
    }
}
