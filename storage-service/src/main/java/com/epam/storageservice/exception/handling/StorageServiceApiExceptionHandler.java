package com.epam.storageservice.exception.handling;

import com.epam.storageservice.exception.StorageServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class StorageServiceApiExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({ConstraintViolationException.class, IllegalArgumentException.class})
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        var apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler({StorageServiceException.class})
    public ResponseEntity<Object> handleStorageServiceException(StorageServiceException ex) {
        var apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
        var apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleException(Exception ex) {
        var apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
