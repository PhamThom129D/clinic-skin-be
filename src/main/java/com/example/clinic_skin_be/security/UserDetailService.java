package com.example.clinic_skin_be.security;

import com.example.clinic_skin_be.model.user.Account;
import com.example.clinic_skin_be.repository.user.IAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Service
@AllArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final IAccountRepository accountRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account;
        boolean isEmail;

        if (username.contains("@")) {
            isEmail = true;
            account = accountRepo.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Email not found: " + username));
        } else {
            isEmail = false;
            account = accountRepo.findByPhoneNumber(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Phone number not found: " + username));
        }

        String principal = isEmail ? account.getEmail() : account.getPhoneNumber();

        // Load permissions từ role
        List<SimpleGrantedAuthority> authorities = account.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                .toList();

        return new User(
                principal,
                account.getPassword(),
                authorities
        );
    }
}
