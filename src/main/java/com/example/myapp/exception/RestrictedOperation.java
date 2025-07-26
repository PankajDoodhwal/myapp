package com.example.myapp.exception;

public class RestrictedOperation extends RuntimeException{
    public RestrictedOperation(String message) {
        super(message);
    }
}
