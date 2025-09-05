package com.example.clinic_skin_be.service.auth.impl;

import com.example.clinic_skin_be.dto.user.AccountRequest;
import com.example.clinic_skin_be.dto.auth.AuthResponse;
import com.example.clinic_skin_be.dto.auth.LoginRequest;
import com.example.clinic_skin_be.exception.FieldAlreadyExistsException;
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

    private String avt_default = "https://res.cloudinary.com/dk6vu2mlh/image/upload/v1754320302/x7hglgokkpmvsvjqm8xz.jpg";

    @Override
    public AuthResponse register(AccountRequest request) {
        if ((request.getEmail() == null || request.getEmail().isBlank()) &&
                (request.getPhoneNumber() == null || request.getPhoneNumber().isBlank())) {
            throw new RuntimeException("Email hoặc số điện thoại không được để trống.");
        }

        if (request.getEmail() != null && accountRepo.existsByEmail(request.getEmail())) {
            throw new FieldAlreadyExistsException("email", "Email đã tồn tại trong hệ thống");
        }

        if (request.getPhoneNumber() != null && accountRepo.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new FieldAlreadyExistsException("phoneNumber", "Số điện thoại đã tồn tại trong hệ thống");
        }


        Account account = authMapper.toEntity(request);
        account.setPassword(passwordEncoder.encode(request.getPassword()));

        // Xử lý avatar upload
        if (request.getAvatarFile() != null && !request.getAvatarFile().isEmpty()) {
            Map uploadResult = cloudinaryService.uploadImage(request.getAvatarFile(), "avatars");
            account.setAvtPath((String) uploadResult.get("secure_url"));
        } else {
            account.setAvtPath(avt_default);
        }

        // Set role
        Role selectedRole = roleRepo.findByName(request.getRole())
                .orElseThrow(() -> new RuntimeException("Role not found"));
        account.setRoles(Set.of(selectedRole));

        accountRepo.save(account);

        // Xác thực lại để sinh JWT
        String identifier = request.getPhoneNumber() != null ? request.getPhoneNumber() : request.getEmail();
        Authentication auth = performAuthentication(identifier, request.getPassword());
        String token = jwtUtil.generateToken(auth);

        // Map Entity -> DTO response
        AuthResponse response = authMapper.toAuthResponse(account);
        response.setToken(token);
        emailService.sendRegisterSuccessEmail(account);
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

        AuthResponse response = authMapper.toAuthResponse(account);
        response.setToken(token);
        emailService.sendLoginSuccessEmail(account);
        return response;
    }

    @Value("${GOOGLE_CLIENT_ID}")
    private String googleClientId;

    @Override
    public AuthResponse loginWithGoogle(String token) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    JacksonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList("660467970675-fep06eap4m3m5hgi3kuovhmtdi28l43e.apps.googleusercontent.com"))
                    .build();

            GoogleIdToken idToken = verifier.verify(token);

            if (idToken == null) {
                throw new RuntimeException("Invalid Google Token");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String picture = (String) payload.get("picture");

            Account account = accountRepo.findByEmail(email).orElseGet(() -> {
                String defaultPassword = passwordEncoder.encode("Abc@1234");

                Account newUser = Account.builder()
                        .email(email)
                        .fullName(name)
                        .avtPath(picture)
                        .password(defaultPassword)
                        .phoneNumber("")
                        .roles(Set.of(roleRepo.findByName("ROLE_PATIENT")
                                .orElseThrow(() -> new RuntimeException("Role USER not found"))))
                        .build();
                return accountRepo.save(newUser);
            });

            String jwt = jwtUtil.generateToken(account.getEmail(),
                    account.getRoles().stream().map(Role::getName).toList());

            AuthResponse response = authMapper.toAuthResponse(account);
            response.setToken(jwt);
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Đăng nhập Google thất bại: " + e.getMessage());
        }
    }


    @Override
    public void loginWithOtp(LoginRequest loginRequest) {
        String identifier = loginRequest.getEmailOrPhone();
        log.info("Đăng nhập bằng OTP với identifier: {}", identifier);

        Account account = otpService.findAccountByIdentifier(identifier);
        if (account == null) {
            throw new RuntimeException("Tài khoản không tồn tại trong hệ thống.");
        }

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
        if (account == null) {
            throw new RuntimeException("Account not found with email or phone: " + loginRequest.getEmailOrPhone());
        }

        String key = account.getId().toString();
        String oldOtp = otpCache.get(key);

        if (oldOtp != null) {
            throw new RuntimeException("OTP is still valid. Please wait before requesting a new one.");
        }

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

        AuthResponse response = authMapper.toAuthResponse(account);
        response.setToken(token);
        return response;
    }


    private Authentication performAuthentication(String identifier, String rawPassword) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(identifier, rawPassword)
        );
    }
}