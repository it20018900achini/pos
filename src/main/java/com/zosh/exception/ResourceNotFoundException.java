package com.zosh.exception;

public class ResourceNotFoundException extends RuntimeException { // Unchecked
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
