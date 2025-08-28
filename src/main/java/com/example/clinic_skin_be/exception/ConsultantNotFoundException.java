package com.example.clinic_skin_be.exception;

public class ConsultantNotFoundException extends RuntimeException {
    public ConsultantNotFoundException(String message) {
        super(message);
    }
}
