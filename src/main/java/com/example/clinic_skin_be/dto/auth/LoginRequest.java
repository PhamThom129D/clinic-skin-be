package com.example.clinic_skin_be.dto.auth;


import lombok.Data;

@Data
public class LoginRequest {
        private String emailOrPhone;
        private String password;
        private String otpCode;
        private String googleToken;
}
