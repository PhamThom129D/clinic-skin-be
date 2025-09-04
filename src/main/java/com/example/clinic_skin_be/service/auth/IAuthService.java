package com.example.clinic_skin_be.service.auth;

import com.example.clinic_skin_be.dto.user.AccountRequest;
import com.example.clinic_skin_be.dto.auth.AuthResponse;
import com.example.clinic_skin_be.dto.auth.LoginRequest;

public interface IAuthService {
    AuthResponse register(AccountRequest accountRequest);

    AuthResponse login(LoginRequest loginRequest);

    AuthResponse loginWithGoogle(String token);

    void loginWithOtp(LoginRequest loginRequest);

    void resendOtp(LoginRequest loginRequest);

    AuthResponse verifyOtp(LoginRequest loginRequest);

    void logout();
}
