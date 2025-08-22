package com.example.clinic_skin_be.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class AccountRequest {
    private String fullname;
    private String phonenumber;
    private String email;
    private String password;
    private String gender;
    private MultipartFile avatarFile;
    private String role;
}
