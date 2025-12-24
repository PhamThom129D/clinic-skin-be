package com.example.clinic_skin_be.service.medical;

import com.example.clinic_skin_be.dto.medical.MedicalRecordDTO;
import com.example.clinic_skin_be.mapper.MedicalRecordMapper;
import com.example.clinic_skin_be.model.medical.MedicalRecord;
import com.example.clinic_skin_be.repository.medical.IMedicalRecordRepository;
import com.example.clinic_skin_be.repository.medical.IVisitSessionRepository;
import com.example.clinic_skin_be.repository.patient.IPatientRepository;
import com.example.clinic_skin_be.repository.staff.IDoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicalRecordService {

    private final IMedicalRecordRepository medicalRecordRepo;
    private final IPatientRepository patientRepo;
    private final MedicalRecordMapper medicalRecordMapper;

    // Lấy tất cả hồ sơ khám
    public List<MedicalRecordDTO> getAllMedicalRecords() {
        return medicalRecordRepo.findAll()
                .stream()
                .map(medicalRecordMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Lấy hồ sơ theo ID
    public Optional<MedicalRecordDTO> getMedicalRecordById(Long id) {
        return medicalRecordRepo.findById(id)
                .map(medicalRecordMapper::toDTO);
    }


    // Tạo mới
    public MedicalRecordDTO createMedicalRecord(MedicalRecordDTO dto) {
        var patient = patientRepo.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        MedicalRecord record = new MedicalRecord();
        record.setPatient(patient);
        record.setVisitDate(dto.getVisitDate());
        record.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : java.time.LocalDateTime.now());

        MedicalRecord saved = medicalRecordRepo.save(record);
        return medicalRecordMapper.toDTO(saved);
    }

    // Cập nhật
    public MedicalRecordDTO updateMedicalRecord(MedicalRecordDTO dto) {
        MedicalRecord record = medicalRecordRepo.findById(dto.getRecordId())
                .orElseThrow(() -> new RuntimeException("Medical record not found"));

        if (dto.getPatientId() != null) {
            var patient = patientRepo.findById(dto.getPatientId())
                    .orElseThrow(() -> new RuntimeException("Patient not found"));
            record.setPatient(patient);
        }

        record.setVisitDate(dto.getVisitDate());
        record.setUpdatedAt(java.time.LocalDateTime.now());

        MedicalRecord updated = medicalRecordRepo.save(record);
        return medicalRecordMapper.toDTO(updated);
    }


    // Xoá hồ sơ
    public void deleteMedicalRecord(Long id) {
        medicalRecordRepo.deleteById(id);
    }
}

