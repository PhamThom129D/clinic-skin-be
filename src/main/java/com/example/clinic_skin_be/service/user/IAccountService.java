package com.example.clinic_skin_be.service.user;

import com.example.clinic_skin_be.dto.user.AccountUpdateRequest;
import com.example.clinic_skin_be.model.manage_enum.AccountStatus;
import com.example.clinic_skin_be.model.user.Account;

import java.util.List;
import java.util.Optional;

public interface IAccountService {
    List<Account> getAllAccount();
    Optional<Account> getAccountById (Long id);
    Account saveAccount (Account account);
    void updateAccountStatus (Long id, AccountStatus status);
    void resetPassword (String email, String newPassword);
    void changePassword (String email, String oldPassword, String newPassword);
}
