package com.example.clinic_skin_be.dto.user;

import lombok.Data;

import java.util.Set;

@Data
public class AccountResponse {
    private Long id;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String gender;
    private String avtPath;
    private Set<String> roles;
    private String status;
    private String createdAt;
    private String updatedAt;
}


