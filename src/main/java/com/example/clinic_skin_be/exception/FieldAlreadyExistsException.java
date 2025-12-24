package com.example.clinic_skin_be.exception;

public class FieldAlreadyExistsException extends RuntimeException {
    private final String field;
    public FieldAlreadyExistsException(String field, String message) {
        super(message);
        this.field = field;
    }
    public String getField() { return field; }
}
