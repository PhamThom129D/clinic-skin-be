package com.example.clinic_skin_be.dto.staff;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorDTO {
    private Long id;
    private String specialty;
    private String doctorName;
    private String avtPath;
    private String level;
    private Long accountId;

    private List<CertificateDTO> certificates;
}
