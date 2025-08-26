package com.example.clinic_skin_be.service.auth.impl;

import com.example.clinic_skin_be.dto.user.AccountRequest;
import com.example.clinic_skin_be.dto.auth.AuthResponse;
import com.example.clinic_skin_be.dto.auth.LoginRequest;
import com.example.clinic_skin_be.mapper.AuthMapper;
import com.example.clinic_skin_be.model.user.Account;
import com.example.clinic_skin_be.model.user.Role;
import com.example.clinic_skin_be.repository.user.IAccountRepository;
import com.example.clinic_skin_be.repository.user.IRoleRepository;
import com.example.clinic_skin_be.service.CloudinaryService;
import com.example.clinic_skin_be.service.auth.IAuthService;
import com.example.clinic_skin_be.util.JwtUtil;
import com.example.clinic_skin_be.util.OtpCache;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Value;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService implements IAuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final IAccountRepository accountRepo;
    private final IRoleRepository roleRepo;
    private final CloudinaryService cloudinaryService;
    private final OtpService otpService;
    private final OtpCache otpCache;
    private final EmailService emailService;
    private final AuthMapper authMapper;

    private final String avt_default = "https://res.cloudinary.com/dk6vu2mlh/image/upload/v1754320302/x7hglgokkpmvsvjqm8xz.jpg";

    @Value("${GOOGLE_CLIENT_ID}")
    private String googleClientId;

    @Override
    public AuthResponse register(@Valid AccountRequest request) {
        validateUniqueEmailOrPhone(request);

        Account account = createAccount(request);
        String token = generateToken(account, request.getPassword());

        return mapToAuthResponse(account, token);
    }

    private void validateUniqueEmailOrPhone(AccountRequest request) {
        if ((request.getEmail() == null || request.getEmail().isBlank()) &&
                (request.getPhoneNumber() == null || request.getPhoneNumber().isBlank())) {
            throw new RuntimeException("Email hoặc số điện thoại không được để trống.");
        }
        if (request.getEmail() != null && accountRepo.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã tồn tại trong hệ thống.");
        }
        if (request.getPhoneNumber() != null && accountRepo.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new RuntimeException("Số điện thoại đã tồn tại trong hệ thống.");
        }
    }

    private Account createAccount(AccountRequest request) {
        Account account = authMapper.toEntity(request);
        account.setPassword(passwordEncoder.encode(request.getPassword()));

        // Avatar
        if (request.getAvatarFile() != null && !request.getAvatarFile().isEmpty()) {
            Map uploadResult = cloudinaryService.uploadImage(request.getAvatarFile(), "avatars");
            account.setAvtPath((String) uploadResult.get("secure_url"));
        } else {
            account.setAvtPath(avt_default);
        }

        // Role
        Role selectedRole = roleRepo.findByName(request.getRole())
                .orElseThrow(() -> new RuntimeException("Role not found"));
        account.setRoles(Set.of(selectedRole));

        return accountRepo.save(account);
    }

    private String generateToken(Account account, String rawPassword) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        account.getEmail() != null ? account.getEmail() : account.getPhoneNumber(),
                        rawPassword
                )
        );
        return jwtUtil.generateToken(auth);
    }

    private AuthResponse mapToAuthResponse(Account account, String token) {
        AuthResponse response = authMapper.toAuthResponse(account);
        response.setToken(token);
        return response;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmailOrPhone(), request.getPassword()
                )
        );

        String token = jwtUtil.generateToken(authentication);
        Account account = otpService.findAccountByIdentifier(request.getEmailOrPhone());
        return mapToAuthResponse(account, token);
    }

    @Override
    public AuthResponse loginWithGoogle(String token) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    JacksonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(token);
            if (idToken == null) throw new RuntimeException("Invalid Google Token");

            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String picture = (String) payload.get("picture");

            Account account = accountRepo.findByEmail(email).orElseGet(() -> {
                Account newUser = Account.builder()
                        .email(email)
                        .fullName(name)
                        .avtPath(picture)
                        .password("")
                        .phoneNumber("")
                        .roles(Set.of(roleRepo.findByName("ROLE_PATIENT")
                                .orElseThrow(() -> new RuntimeException("Role USER not found"))))
                        .build();
                return accountRepo.save(newUser);
            });

            String jwt = jwtUtil.generateToken(account.getEmail(),
                    account.getRoles().stream().map(Role::getName).toList());

            return mapToAuthResponse(account, jwt);

        } catch (Exception e) {
            throw new RuntimeException("Đăng nhập Google thất bại: " + e.getMessage(), e);
        }
    }

    @Override
    public void loginWithOtp(LoginRequest loginRequest) {
        String identifier = loginRequest.getEmailOrPhone();
        Account account = otpService.findAccountByIdentifier(identifier);
        if (account == null) throw new RuntimeException("Tài khoản không tồn tại.");

        if (identifier.contains("@")) {
            otpService.generateAndSendOtp(identifier);
        } else {
            throw new RuntimeException("Email không hợp lệ.");
        }
    }

    @Override
    public void resendOtp(LoginRequest loginRequest) {
        if (loginRequest.getEmailOrPhone() == null || loginRequest.getEmailOrPhone().isBlank()) {
            throw new RuntimeException("Email or phone must not be empty.");
        }

        Account account = otpService.findAccountByIdentifier(loginRequest.getEmailOrPhone());
        if (account == null) throw new RuntimeException("Account not found with email or phone: " + loginRequest.getEmailOrPhone());

        String key = account.getId().toString();
        String oldOtp = otpCache.get(key);
        if (oldOtp != null) throw new RuntimeException("OTP is still valid. Please wait before requesting a new one.");

        String newOtp = otpService.generateOtp();
        otpCache.put(key, newOtp, 180);
        emailService.sendOtpEmail(account.getEmail(), newOtp);
    }

    @Override
    public AuthResponse verifyOtp(LoginRequest loginRequest) {
        Account account = otpService.findAccountByIdentifier(loginRequest.getEmailOrPhone());
        boolean success = otpService.verifyOtp(account.getId(), loginRequest.getOtpCode());

        if (!success) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mã OTP không đúng.");
        }

        String token = jwtUtil.generateToken(
                account.getEmail(),
                account.getRoles().stream().map(Role::getName).toList()
        );

        return mapToAuthResponse(account, token);
    }

    @Override
    public void logout() {
        // Nếu cần blacklist JWT
    }
}
