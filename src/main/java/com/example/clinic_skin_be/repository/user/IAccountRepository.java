package com.example.clinic_skin_be.repository.user;

import com.example.clinic_skin_be.model.manage_enum.AccountStatus;
import com.example.clinic_skin_be.model.user.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface IAccountRepository extends JpaRepository<Account, Long> {
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);

    Optional<Account> findByEmail(String email);
    Optional<Account> findByPhoneNumber(String phoneNumber);
    Optional<Account> findByEmailOrPhoneNumber(String email, String phoneNumber);

    @Modifying
    @Transactional
    @Query ("update Account a set a.status = :status where a.id = :id")
    void updateStatusById (@Param("id") Long id, @Param("status") AccountStatus status);
}
