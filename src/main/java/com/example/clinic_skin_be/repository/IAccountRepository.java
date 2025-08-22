package com.example.clinic_skin_be.repository;

import com.example.clinic_skin_be.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAccountRepository extends JpaRepository<Account, Long> {
    boolean existsByEmail(String email);
    boolean existsByPhonenumber(String phonenumber);

    Optional<Account> findByEmail(String email);
    Optional<Account> findByPhonenumber(String phonenumber);
}
