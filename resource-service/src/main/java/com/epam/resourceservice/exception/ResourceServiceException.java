package com.epam.resourceservice.exception;

public class ResourceServiceException extends RuntimeException {
    public ResourceServiceException() {
        super();
    }

    public ResourceServiceException(String message) {
        super(message);
    }
}
