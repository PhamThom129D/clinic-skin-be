package com.example.clinic_skin_be.dto.staff;

import com.example.clinic_skin_be.model.manage_enum.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorInfoDTO {
    private Long doctorId;
    private String level, specialty;
    private Long accountId;
    private String fullName, phoneNumber, email, password;
    private LocalDate dateOfBirth;
    private String address;
    private Gender gender;
    private String avtPath;

    public DoctorInfoDTO(Long doctorId, String fullName, String specialty, String avtPath) {
        this.doctorId = doctorId;
        this.fullName = fullName;
        this.specialty = specialty;
        this.avtPath = avtPath;
    }
}
