package com.example.clinic_skin_be.dto.staff;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificateDTO {
    private Long id;
    private String name;
    private String certificateNumber;
    private LocalDate issuedDate;
    private LocalDate expiryDate;
    private String issuedBy;
    private String description;
    private String imageUrl;
    private MultipartFile file;
    private Long doctorId;
}
