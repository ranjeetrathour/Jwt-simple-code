package com.example.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GenericException extends RuntimeException {
    private int statusCode;
    private String message;

    public GenericException(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
