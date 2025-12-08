package com.example.clinic_skin_be.controller.medical;

import com.example.clinic_skin_be.dto.medical.MedicalRecordDTO;
import com.example.clinic_skin_be.dto.patient.AppointmentHistorySummaryDTO;
import com.example.clinic_skin_be.dto.patient.appointmentdetail.PatientMedicalHistoryDTO;
import com.example.clinic_skin_be.service.medical.MedicalRecordService;
import com.example.clinic_skin_be.service.patient.PatientService;
import com.example.clinic_skin_be.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-records")
@RequiredArgsConstructor
public class MedicalRecordRestController {

    private final MedicalRecordService medicalRecordService;
    private final JwtUtil jwtUtil;
    private final PatientService patientService;

    // Lấy danh sách hồ sơ khám
    @GetMapping("")
    public ResponseEntity<List<MedicalRecordDTO>> getAllRecords() {
        return ResponseEntity.ok(medicalRecordService.getAllMedicalRecords());
    }

    // Lấy hồ sơ khám theo ID
    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecordDTO> getRecordById(@PathVariable Long id) {
        return medicalRecordService.getMedicalRecordById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<MedicalRecordDTO> createRecord(@RequestBody MedicalRecordDTO dto) {
        return ResponseEntity.ok(medicalRecordService.createMedicalRecord(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecordDTO> updateRecord(
            @PathVariable Long id,
            @RequestBody MedicalRecordDTO dto) {
        dto.setRecordId(id);
        return ResponseEntity.ok(medicalRecordService.updateMedicalRecord(dto));
    }


    // Xóa hồ sơ khám
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        medicalRecordService.deleteMedicalRecord(id);
        return ResponseEntity.noContent().build();
    }

    // Lấy danh sách hồ sơ khám cơ bản
    @GetMapping("/summary")
    public ResponseEntity<List<PatientMedicalHistoryDTO>> getSummary(Authentication authentication, @RequestHeader("Authorization") String tokenHeader) {
        String token = tokenHeader.substring(7);
        Long userId = jwtUtil.getUserIdFromToken(token);
        List<PatientMedicalHistoryDTO> historyList = patientService.getMedicalHistorySummaryForPatient(userId);
        return ResponseEntity.ok(historyList);
    }

}
