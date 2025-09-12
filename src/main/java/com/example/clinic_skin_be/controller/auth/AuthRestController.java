package com.example.clinic_skin_be.controller.auth;

import com.example.clinic_skin_be.dto.user.AccountRequest;
import com.example.clinic_skin_be.dto.auth.AuthResponse;
import com.example.clinic_skin_be.dto.auth.LoginRequest;
import com.example.clinic_skin_be.service.auth.IAuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Slf4j
public class AuthRestController {

    private final IAuthService authService;

    private static final Logger logger = LoggerFactory.getLogger(AuthRestController.class);

    // --- GOOGLE LOGIN ---
    @PostMapping("/login-google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody Map<String, String> body) {
        try {
            String token = body.get("token");
            AuthResponse authResponse = authService.loginWithGoogle(token);
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            logger.error("Google login failed", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Đăng nhập Google thất bại: " + e.getMessage());
        }
    }

    // --- REGISTER ---
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @ModelAttribute AccountRequest accountRequest) {
        try {
            return ResponseEntity.ok(authService.register(accountRequest));
        } catch (Exception e) {
            logger.error("Register failed", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // --- LOGIN ---
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            long start = System.currentTimeMillis();
            AuthResponse authResponse = authService.login(loginRequest);
            long end = System.currentTimeMillis();
            logger.info("Login success for {} in {} ms", loginRequest.getEmailOrPhone(), (end - start));
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            logger.error("Login failed for {}", loginRequest.getEmailOrPhone(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Đăng nhập thất bại: " + e.getMessage());
        }
    }

    // --- LOGIN OTP ---
    @PostMapping("/login-otp")
    public ResponseEntity<String> loginWithOtp(@RequestBody LoginRequest loginRequest) {
        try {
            authService.loginWithOtp(loginRequest);
            return ResponseEntity.ok("OTP sent successfully");
        } catch (Exception ex) {
            logger.error("Lỗi khi đăng nhập bằng OTP: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    // --- RESEND OTP ---
    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestBody LoginRequest loginRequest) {
        try {
            authService.resendOtp(loginRequest);
            return ResponseEntity.ok("OTP resent successfully");
        } catch (Exception e) {
            logger.error("Resend OTP failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to resend OTP: " + e.getMessage());
        }
    }

    // --- VERIFY OTP ---
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody LoginRequest loginRequest) {
        try {
            AuthResponse authResponse = authService.verifyOtp(loginRequest);
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            logger.error("OTP verification failed for email: {}", loginRequest.getEmailOrPhone(), e);
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "Invalid OTP. Please try again."));
        }
    }

    // --- LOGOUT ---
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        try {
            // Nếu backend có session/token invalidation thì xử lý ở đây
            return ResponseEntity.ok("Logged out successfully");
        } catch (Exception e) {
            logger.error("Logout failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Logout failed: " + e.getMessage());
        }
    }
}
