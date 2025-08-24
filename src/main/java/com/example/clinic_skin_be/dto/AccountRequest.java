package com.example.clinic_skin_be.dto;

import com.example.clinic_skin_be.model.Account;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;


@Data
public class AccountRequest {
    private String fullName;
    private String phoneNumber;
    private String email;
    private String password;
    private String address;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;
    private Account.Gender gender;
    private Account.Status status;
    private MultipartFile avatarFile;

    private String role;
}
