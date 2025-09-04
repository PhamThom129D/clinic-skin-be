package com.example.clinic_skin_be.controller.medical;

import com.example.clinic_skin_be.dto.medical.MedicalRecordDTO;
import com.example.clinic_skin_be.service.medical.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-records")
@RequiredArgsConstructor
public class MedicalRecordRestController {

    private final MedicalRecordService medicalRecordService;

    // Lấy danh sách hồ sơ khám
    @GetMapping("")
    public ResponseEntity<List<MedicalRecordDTO>> getAllRecords() {
        return ResponseEntity.ok(medicalRecordService.getAllMedicalRecords());
    }

    // Lấy hồ sơ khám theo ID
    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecordDTO> getRecordById(@PathVariable Long id) {
        MedicalRecordDTO dto = medicalRecordService.getMedicalRecordById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
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
}
