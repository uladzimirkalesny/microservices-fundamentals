package com.epam.storageservice.exception;

public class StorageServiceException extends RuntimeException {
    public StorageServiceException() {
        super();
    }

    public StorageServiceException(String message) {
        super(message);
    }
}
