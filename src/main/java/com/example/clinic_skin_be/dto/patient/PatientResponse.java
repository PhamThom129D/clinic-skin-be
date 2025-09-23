package com.example.clinic_skin_be.dto.patient;

import com.example.clinic_skin_be.dto.user.AccountResponse;
import lombok.Data;

@Data
public class PatientResponse {
    private Long id;
    private String passportNumber;
    private String occupation;
    private AccountResponse account;
}
