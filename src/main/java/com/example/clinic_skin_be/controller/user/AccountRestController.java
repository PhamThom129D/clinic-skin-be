package com.example.clinic_skin_be.controller.user;

import com.example.clinic_skin_be.dto.user.AccountUpdateRequest;
import com.example.clinic_skin_be.dto.user.PasswordChangeRequest;
import com.example.clinic_skin_be.dto.user.PasswordResetRequest;
import com.example.clinic_skin_be.model.manage_enum.AccountStatus;
import com.example.clinic_skin_be.model.user.Account;
import com.example.clinic_skin_be.repository.user.IAccountRepository;
import com.example.clinic_skin_be.service.auth.IAuthService;
import com.example.clinic_skin_be.service.auth.impl.AuthService;
import com.example.clinic_skin_be.service.user.AccountService;
import com.example.clinic_skin_be.service.user.IAccountService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.eclipse.angus.mail.iap.Response;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountRestController {
    private final IAccountService accountService;
    private final IAuthService authService;

    @GetMapping ("/list")
    public ResponseEntity<List<Account>> getAllAccounts () {
        return ResponseEntity.ok(accountService.getAllAccount());
    }

    @GetMapping ("/{id}")
    public ResponseEntity<Account> getAccountById (@PathVariable Long id) {
        return ResponseEntity.of(accountService.getAccountById(id));
    }

    @PostMapping ("save")
    public ResponseEntity<Account> saveAccount (@RequestBody Account account) {
        return ResponseEntity.ok(accountService.saveAccount(account));
    }

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

    @PostMapping ("/reset-password")
    public ResponseEntity<Void> resetPassword (@RequestBody PasswordResetRequest resetRequest) {
        accountService.resetPassword(resetRequest.getEmail(), resetRequest.getNewPassword());
        return ResponseEntity.ok().build();
    }

    @PostMapping ("/change-password")
    public ResponseEntity<?> changePassword (@RequestBody PasswordChangeRequest request) {
        try {
            accountService.changePassword(request.getEmail(), request.getOldPassword(), request.getNewPassword());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
