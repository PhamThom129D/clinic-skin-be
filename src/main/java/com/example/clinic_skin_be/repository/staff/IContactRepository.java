package com.example.clinic_skin_be.repository.staff;

import com.example.clinic_skin_be.model.staff.Contacts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IContactRepository extends JpaRepository<Contacts, Long> {
}
