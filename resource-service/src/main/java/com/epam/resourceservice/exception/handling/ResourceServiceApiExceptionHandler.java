package com.epam.resourceservice.exception.handling;

import com.epam.resourceservice.exception.ResourceServiceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ResourceServiceApiExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String VALIDATION_ERROR_MESSAGE = "Validation error or request body is an invalid mp3";
    private static final String ENTITY_NOT_FOUND_ERROR_MESSAGE = "Resource doesn't exist with given id";
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal server error occurred";

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                     HttpHeaders headers,
                                                                     HttpStatus status,
                                                                     WebRequest request) {
        var apiError = new ApiError(HttpStatus.BAD_REQUEST, VALIDATION_ERROR_MESSAGE);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler({ResourceServiceException.class, ConstraintViolationException.class})
    public ResponseEntity<Object> handleResourceServiceException() {
        var apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MESSAGE);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<Object> handleEntityNotFoundException() {
        var apiError = new ApiError(HttpStatus.NOT_FOUND, ENTITY_NOT_FOUND_ERROR_MESSAGE);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleException() {
        var apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MESSAGE);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
