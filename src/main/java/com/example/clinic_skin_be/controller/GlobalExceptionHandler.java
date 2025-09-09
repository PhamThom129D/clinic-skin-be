package com.example.clinic_skin_be.controller;

import com.example.clinic_skin_be.exception.FieldAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 1. Xử lý lỗi validation (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    // 2. Xử lý lỗi field đã tồn tại
    @ExceptionHandler(FieldAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleFieldAlreadyExists(FieldAlreadyExistsException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("field", ex.getField());
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // 3. Xử lý các lỗi khác
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleOtherExceptions(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage() != null ? ex.getMessage() : "Đã xảy ra lỗi");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
