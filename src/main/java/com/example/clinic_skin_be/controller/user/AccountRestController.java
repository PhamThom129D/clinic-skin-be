package com.example.clinic_skin_be.controller.user;

import com.example.clinic_skin_be.dto.user.AccountRequest;
import com.example.clinic_skin_be.dto.user.AccountResponse;
import com.example.clinic_skin_be.service.user.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
        AccountResponse updated = accountService.updateAccount(id, request);
        return ResponseEntity.ok(updated);
    }

    /** ================= DELETE: xóa account ================= */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}
