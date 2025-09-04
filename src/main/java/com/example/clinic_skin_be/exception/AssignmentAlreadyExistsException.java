package com.example.clinic_skin_be.exception;

public class AssignmentAlreadyExistsException extends RuntimeException {
    public AssignmentAlreadyExistsException(String message) {
        super(message);
    }
}