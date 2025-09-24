package com.example.clinic_skin_be.controller.user;

import com.example.clinic_skin_be.dto.user.AccountRequest;
import com.example.clinic_skin_be.dto.user.AccountResponse;
import com.example.clinic_skin_be.dto.user.PasswordChangeRequest;
import com.example.clinic_skin_be.dto.user.PasswordResetRequest;
import com.example.clinic_skin_be.service.user.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import com.example.clinic_skin_be.model.manage_enum.AccountStatus;
import org.springframework.http.HttpStatus;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountRestController {

    private final AccountService accountService;

    /** ================= GET: danh sách tất cả account ================= */
    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAccounts(
            @RequestParam(required = false) String role) {
        List<AccountResponse> list = accountService.getAccounts(role);
        return ResponseEntity.ok(list);
    }

    /** ================= GET: chi tiết account theo ID ================= */
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable Long id) {
        AccountResponse account = accountService.getAccountById(id);
        return ResponseEntity.ok(account);
    }

    /** ================= POST: tạo account mới ================= */
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Validated @ModelAttribute AccountRequest request) throws IOException {
        AccountResponse account = accountService.createAccount(request);
        return ResponseEntity.ok(account);
    }

    /** ================= PUT: cập nhật account ================= */
    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> updateAccount(
            @PathVariable Long id,
            @Validated @ModelAttribute AccountRequest request
    ) throws IOException {
        System.out.println(request);
        AccountResponse updated = accountService.updateAccount(id, request);
        return ResponseEntity.ok(updated);
    }

    /** ================= DELETE: xóa account ================= */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

    /** ================= STATUS: block/open account ================= */
    @PutMapping ("/{id}/status")
    public ResponseEntity<Void> updateAccountStatus (@PathVariable Long id, @RequestParam String status) {
        try {
            AccountStatus newStatus = AccountStatus.valueOf(status);
            accountService.updateAccountStatus(id, newStatus);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /** ================= PASSWORD: reset ================= */
    @PutMapping ("/reset-password")
    public ResponseEntity<Void> resetPassword (@RequestBody PasswordResetRequest request) {
        accountService.resetPassword(request.getEmail(), request.getNewPassword());
        return ResponseEntity.ok().build();
    }

    /** ================= PASSWORD: change ================= */
    @PutMapping ("/change-password")
    public ResponseEntity<?> changePassword (@RequestBody PasswordChangeRequest request) {
        try {
            accountService.changePassword(request.getEmail(), request.getOldPassword(), request.getNewPassword());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
