package com.example.clinic_skin_be.service.user;

import com.example.clinic_skin_be.dto.user.AccountRequest;
import com.example.clinic_skin_be.dto.user.AccountResponse;
import com.example.clinic_skin_be.exception.FieldAlreadyExistsException;
import com.example.clinic_skin_be.mapper.AccountMapper;
import com.example.clinic_skin_be.mapper.AuthMapper;
import com.example.clinic_skin_be.model.user.Account;
import com.example.clinic_skin_be.model.user.Role;
import com.example.clinic_skin_be.repository.user.IAccountRepository;
import com.example.clinic_skin_be.repository.user.IRoleRepository;
import com.example.clinic_skin_be.service.CloudinaryService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccountService {

    private final IAccountRepository accountRepository;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthMapper authMapper;
    private final AccountMapper accountMapper;
    private final CloudinaryService cloudinaryService;

    private final String avt_default = "https://res.cloudinary.com/dk6vu2mlh/image/upload/v1754320302/x7hglgokkpmvsvjqm8xz.jpg";

    /** ================= CREATE ================= */
    @Transactional
    public AccountResponse createAccount(AccountRequest request) throws IOException {
        validateUnique(request.getEmail(), request.getPhoneNumber());

        Account account = authMapper.toEntity(request);
        account.setPassword(passwordEncoder.encode(request.getPassword()));

        // Avatar
        MultipartFile avatarFile = request.getAvatarFile();
        if (avatarFile != null && !avatarFile.isEmpty()) {
            account.setAvtPath((String) cloudinaryService.uploadImage(avatarFile, "avatars").get("secure_url"));
        } else {
            account.setAvtPath(avt_default);
        }

        // Role
        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> new RuntimeException("Role not found: " + request.getRole()));
        account.setRoles(Set.of(role));

        accountRepository.save(account);
        return accountMapper.toResponse(account);
    }

    /** ================= READ ================= */
    public List<AccountResponse> getAccounts(String roleName) {
        List<Account> accounts;

        if (roleName == null || roleName.isBlank()) {
            accounts = accountRepository.findAll();
        } else {
            accounts = accountRepository.findAll().stream()
                    .filter(acc -> acc.getRoles() != null &&
                            acc.getRoles().stream().anyMatch(r -> r.getName().equalsIgnoreCase(roleName)))
                    .collect(Collectors.toList());
        }

        return accounts.stream()
                .map(accountMapper::toResponse)
                .collect(Collectors.toList());
    }

    public AccountResponse getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        return accountMapper.toResponse(account);
    }

    /** ================= UPDATE ================= */
    @Transactional
    public AccountResponse updateAccount(Long id, AccountRequest request) throws IOException {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        validateUpdateUnique(account, request.getEmail(), request.getPhoneNumber());

        account.setFullName(request.getFullName());
        account.setEmail(request.getEmail());
        account.setPhoneNumber(request.getPhoneNumber());
        account.setAddress(request.getAddress());
        account.setDateOfBirth(request.getDateOfBirth());
        account.setGender(request.getGender());
        if (request.getStatus() != null) account.setStatus(request.getStatus());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            account.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        // Update role
        if (request.getRole() != null && !request.getRole().isBlank()) {
            Role role = roleRepository.findByName(request.getRole())
                    .orElseThrow(() -> new RuntimeException("Role not found: " + request.getRole()));
            account.setRoles(Set.of(role));
        }

        // Avatar
        MultipartFile avatarFile = request.getAvatarFile();
        if (avatarFile != null && !avatarFile.isEmpty()) {
            account.setAvtPath((String) cloudinaryService.uploadImage(avatarFile, "avatars").get("secure_url"));
        }

        accountRepository.save(account);
        return accountMapper.toResponse(account);
    }

    /** ================= DELETE ================= */
    @Transactional
    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }

    /** ================= HELPER ================= */
    private void validateUnique(String email, String phone) {
        if (email != null && accountRepository.existsByEmail(email)) {
            throw new FieldAlreadyExistsException("email", "Email đã tồn tại");
        }
        if (phone != null && accountRepository.existsByPhoneNumber(phone)) {
            throw new FieldAlreadyExistsException("phoneNumber", "Số điện thoại đã tồn tại");
        }
    }

    private void validateUpdateUnique(Account existing, String email, String phone) {
        if (email != null && !email.equals(existing.getEmail()) && accountRepository.existsByEmail(email)) {
            throw new FieldAlreadyExistsException("email", "Email đã tồn tại");
        }
        if (phone != null && !phone.equals(existing.getPhoneNumber()) && accountRepository.existsByPhoneNumber(phone)) {
            throw new FieldAlreadyExistsException("phoneNumber", "Số điện thoại đã tồn tại");
        }
    }
}
