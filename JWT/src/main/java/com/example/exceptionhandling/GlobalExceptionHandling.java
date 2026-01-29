package com.example.exceptionhandling;

import com.example.exceptions.GenericException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandling {

    public ResponseEntity<?> genericException(GenericException genericException){
        return ResponseEntity
                .status(genericException.getStatusCode())
                .body(genericException.getMessage());    }
}
