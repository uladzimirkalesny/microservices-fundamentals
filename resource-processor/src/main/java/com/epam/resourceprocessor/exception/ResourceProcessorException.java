package com.epam.resourceprocessor.exception;

public class ResourceProcessorException extends RuntimeException {
    public ResourceProcessorException() {
        super();
    }

    public ResourceProcessorException(String message) {
        super(message);
    }
}
