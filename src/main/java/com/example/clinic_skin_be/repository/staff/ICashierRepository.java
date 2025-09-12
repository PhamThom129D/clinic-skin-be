package com.example.clinic_skin_be.repository.staff;

import com.example.clinic_skin_be.model.staff.cashier.Cashier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICashierRepository extends JpaRepository<Cashier, Long> {
}
