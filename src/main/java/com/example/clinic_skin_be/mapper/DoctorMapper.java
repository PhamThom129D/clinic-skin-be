package com.example.clinic_skin_be.mapper;

import com.example.clinic_skin_be.dto.staff.CertificateDTO;
import com.example.clinic_skin_be.dto.staff.DoctorDTO;
import com.example.clinic_skin_be.model.staff.doctor.Certificate;
import com.example.clinic_skin_be.model.staff.doctor.Doctor;
import com.example.clinic_skin_be.model.user.Account;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DoctorMapper {

    // ================== Doctor ==================
    public DoctorDTO toDTO(Doctor doctor) {
        if (doctor == null) return null;
        return DoctorDTO.builder()
                .id(doctor.getId())
                .doctorName(doctor.getAccount() != null ? doctor.getAccount().getFullName() : null)
                .avtPath(doctor.getAccount() != null ? doctor.getAccount().getAvtPath() : null)
                .specialty(doctor.getSpecialty())
                .level(doctor.getLevel())
                .accountId(doctor.getAccount() != null ? doctor.getAccount().getId() : null)
                .certificates(
                        doctor.getCertificates() != null
                                ? doctor.getCertificates().stream()
                                .map(this::toCertificateDTO)
                                .collect(Collectors.toList())
                                : null
                )
                .build();
    }

    public Doctor toEntity(DoctorDTO dto, Account account, Set<Certificate> certificates) {
        if (dto == null) return null;
        return Doctor.builder()
                .id(dto.getId())
                .specialty(dto.getSpecialty())
                .level(dto.getLevel())
                .account(account)
                .certificates(certificates != null ? certificates : new java.util.HashSet<>())
                .build();
    }

    public List<DoctorDTO> toDTOList(List<Doctor> doctors) {
        return doctors.stream().map(this::toDTO).collect(Collectors.toList());
    }

    // ================== Certificate ==================
    public CertificateDTO toCertificateDTO(Certificate certificate) {
        if (certificate == null) return null;
        return CertificateDTO.builder()
                .id(certificate.getId())
                .name(certificate.getName())
                .certificateNumber(certificate.getCertificateNumber())
                .issuedDate(certificate.getIssuedDate())
                .expiryDate(certificate.getExpiryDate())
                .issuedBy(certificate.getIssuedBy())
                .description(certificate.getDescription())
                .imageUrl(certificate.getImageUrl())
                .doctorId(certificate.getDoctor() != null ? certificate.getDoctor().getId() : null)
                .build();
    }

    public Certificate toCertificateEntity(CertificateDTO dto, Doctor doctor) {
        if (dto == null) return null;
        return Certificate.builder()
                .id(dto.getId())
                .name(dto.getName())
                .certificateNumber(dto.getCertificateNumber())
                .issuedDate(dto.getIssuedDate())
                .expiryDate(dto.getExpiryDate())
                .issuedBy(dto.getIssuedBy())
                .description(dto.getDescription())
                .imageUrl(dto.getImageUrl())
                .doctor(doctor)
                .build();
    }
}
