package com.example.clinic_skin_be.service.user;

import com.example.clinic_skin_be.dto.user.AccountUpdateRequest;
import com.example.clinic_skin_be.exception.FieldAlreadyExistsException;
import com.example.clinic_skin_be.mapper.AuthMapper;
import com.example.clinic_skin_be.model.manage_enum.AccountStatus;
import com.example.clinic_skin_be.model.user.Account;
import com.example.clinic_skin_be.repository.user.IAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService{
    private final IAccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthMapper authMapper;
    @Override
    public List<Account> getAllAccount() {
        return accountRepository.findAll();
    }

    @Override
    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    @Override
    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public void updateAccountStatus(Long id, AccountStatus status) {
        accountRepository.updateStatusById(id, status);
    }

    @Override
    public void resetPassword(String email, String newPassword) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));
        updatePassword(account, newPassword);
    }

    @Override
    public void changePassword(String email, String oldPassword, String newPassword) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));
        if (!passwordEncoder.matches(oldPassword, account.getPassword())) {
            throw new RuntimeException("Mật khẩu cũ không chính xác");
        }
        updatePassword(account, newPassword);
    }

    private void updatePassword(Account account, String newPassword) {
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
    }
}
