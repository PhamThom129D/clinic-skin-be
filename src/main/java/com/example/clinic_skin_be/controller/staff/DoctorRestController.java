package com.example.clinic_skin_be.controller.staff;

import com.example.clinic_skin_be.dto.staff.CertificateDTO;
import com.example.clinic_skin_be.dto.staff.DoctorDTO;
import com.example.clinic_skin_be.dto.user.AccountRequest;
import com.example.clinic_skin_be.service.staff.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorRestController {

    private final DoctorService doctorService;

    // ==================== Doctor ====================

    @PostMapping
    public ResponseEntity<DoctorDTO> createDoctor(
            @ModelAttribute DoctorDTO doctorDTO,
            @ModelAttribute AccountRequest accountRequest
    ) {
        return ResponseEntity.ok(doctorService.createDoctor(doctorDTO, accountRequest));
    }

    @GetMapping
    public ResponseEntity<List<DoctorDTO>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctorDTOs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorDTOById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorDTO> updateDoctor(
            @PathVariable Long id,
            @RequestBody DoctorDTO dto
    ) {
        return ResponseEntity.ok(doctorService.updateDoctor(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== Certificate ====================

    @PostMapping("/{doctorId}/certificates")
    public ResponseEntity<CertificateDTO> addCertificate(
            @PathVariable Long doctorId,
            @ModelAttribute CertificateDTO dto,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        dto.setFile(file);
        return ResponseEntity.ok(doctorService.addCertificateToDoctor(doctorId, dto));
    }

    @GetMapping("/{doctorId}/certificates")
    public ResponseEntity<List<CertificateDTO>> getCertificates(@PathVariable Long doctorId) {
        return ResponseEntity.ok(doctorService.getCertificatesByDoctor(doctorId));
    }

    @PutMapping("/certificates/{certificateId}")
    public ResponseEntity<CertificateDTO> updateCertificate(
            @PathVariable Long certificateId,
            @ModelAttribute CertificateDTO dto,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        dto.setFile(file);
        return ResponseEntity.ok(doctorService.updateCertificate(certificateId, dto));
    }

    @DeleteMapping("/certificates/{certificateId}")
    public ResponseEntity<Void> deleteCertificate(@PathVariable Long certificateId) {
        doctorService.deleteCertificate(certificateId);
        return ResponseEntity.noContent().build();
    }
}
