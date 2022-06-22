package com.epam.songservice.exception;

public class SongServiceException extends RuntimeException {
    public SongServiceException() {
        super();
    }

    public SongServiceException(String message) {
        super(message);
    }
}
