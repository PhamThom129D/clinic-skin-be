package com.example.clinic_skin_be.service.medical;

import com.example.clinic_skin_be.dto.medical.VisitSessionDTO;
import com.example.clinic_skin_be.dto.medical.treatment_template.PrescriptionDTO;
import com.example.clinic_skin_be.dto.medical.treatment_template.PrescriptionDetailDTO;
import com.example.clinic_skin_be.dto.medical.treatment_template.TreatmentStepTemplateDTO;
import com.example.clinic_skin_be.dto.staff.DoctorDTO;
import com.example.clinic_skin_be.mapper.DoctorMapper;
import com.example.clinic_skin_be.mapper.PrescriptionMapper;
import com.example.clinic_skin_be.mapper.TreatmentPlanMapper;
import com.example.clinic_skin_be.mapper.VisitSessionMapper;
import com.example.clinic_skin_be.model.medical.MedicalRecord;
import com.example.clinic_skin_be.model.medical.VisitSession;
import com.example.clinic_skin_be.model.medical.lab_test.LabTest;
import com.example.clinic_skin_be.model.medical.medication.Medication;
import com.example.clinic_skin_be.model.medical.medication.Prescription;
import com.example.clinic_skin_be.model.medical.medication.PrescriptionDetail;
import com.example.clinic_skin_be.model.medical.treatment_plan.StepType;
import com.example.clinic_skin_be.model.medical.treatment_plan.TreatmentPlan;
import com.example.clinic_skin_be.model.medical.treatment_plan.TreatmentStep;
import com.example.clinic_skin_be.model.medical.treatment_template.TreatmentTemplate;
import com.example.clinic_skin_be.model.staff.doctor.Doctor;
import com.example.clinic_skin_be.repository.medical.IMedicalRecordRepository;
import com.example.clinic_skin_be.repository.medical.medication.IPrescriptionDetailRepository;
import com.example.clinic_skin_be.repository.medical.medication.IPrescriptionRepository;
import com.example.clinic_skin_be.repository.medical.treatment_plan.ITreatmentPlanRepository;
import com.example.clinic_skin_be.repository.medical.IVisitSessionRepository;
import com.example.clinic_skin_be.repository.medical.treatment_plan.ITreatmentStepRepository;
import com.example.clinic_skin_be.repository.staff.IDoctorRepository;
import com.example.clinic_skin_be.service.medical.treatment_plan.TreatmentPlanService;
import com.example.clinic_skin_be.service.medical.treatment_template.PrescriptionService;
import com.example.clinic_skin_be.service.medical.treatment_template.TreatmentItemService;
import com.example.clinic_skin_be.service.medical.treatment_template.TreatmentTemplateService;
import com.example.clinic_skin_be.service.staff.DoctorService;
import com.example.clinic_skin_be.service.staff.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class VisitSessionService {

    private final IVisitSessionRepository visitSessionRepo;
    private final IMedicalRecordRepository medicalRecordRepo;
    private final ITreatmentPlanRepository treatmentPlanRepo;
    private final VisitSessionMapper visitSessionMapper;
    private final TreatmentTemplateService treatmentTemplateServiceService;
    private final PrescriptionMapper mapper;
    private final TreatmentItemService treatmentItemService;
    private final ITreatmentStepRepository treatmentStepRepo;
    private final IPrescriptionRepository prescriptionRepo;
    private final IPrescriptionDetailRepository prescriptionDetailRepo;
    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;

        // Gán bác sĩ và cập nhật triệu chứng/chẩn đoán
        public void assignDoctorToSession(VisitSessionDTO dto, VisitSession session) {
            if (dto.getDoctorId() != null) {
                DoctorDTO doctorDTO = doctorService.getDoctorDTOById(dto.getDoctorId());
                Doctor doctor = doctorMapper.toEntity(doctorDTO, null, null);
                session.setDoctor(doctor);
            }
            if (dto.getSymptoms() != null) {
                session.setSymptoms(dto.getSymptoms());
            }
            if (dto.getDiagnosis() != null) {
                session.setDiagnosis(dto.getDiagnosis());
            }
            session.setUpdatedAt(LocalDateTime.now());
        }

        // Clone treatment template thành plan thực tế
        public TreatmentPlan cloneTemplate(VisitSessionDTO dto, TreatmentTemplate template) {
            if (template == null) {
                throw new RuntimeException("Treatment template not found");
            }
            TreatmentPlan realPlan = new TreatmentPlan();
            realPlan.setTreatmentName(template.getName().concat(" - Phác đồ ").concat(dto.getPatientName()));
            realPlan.setDisease_name(template.getDiseaseName());
            return realPlan;
        }

        @Transactional
        public VisitSessionDTO updateVisitSession(
                VisitSessionDTO dto,
                List<TreatmentStepTemplateDTO> stepDTOs,
                List<PrescriptionDetailDTO> prescriptions,
                List<LabTest> labTests
        ) {
            // 1. Lấy session hiện tại
            VisitSession session = visitSessionRepo.findById(dto.getSessionId())
                    .orElseThrow(() -> new RuntimeException("Visit session not found"));

            // 2. Gán bác sĩ + chẩn đoán
            assignDoctorToSession(dto, session);

            // 3. Tạo và lưu phác đồ thực tế
            TreatmentTemplate template = treatmentTemplateServiceService
                    .getTreatmentTemplateEntityById(dto.getTreatmentPlan().getId());
            TreatmentPlan realPlan = cloneTemplate(dto, template);
            realPlan = treatmentPlanRepo.save(realPlan);
            session.setTreatmentPlan(realPlan);

            // 4. Xử lý các bước điều trị
            if (stepDTOs != null && !stepDTOs.isEmpty()) {
                List<TreatmentStep> realSteps = new ArrayList<>();

                for (TreatmentStepTemplateDTO stepDTO : stepDTOs) {
                    StepType stepType = treatmentItemService.getStepTypeById(stepDTO.getStepTypeId());

                    TreatmentStep step = new TreatmentStep();
                    step.setStepNumber(stepDTO.getStepNumber());
                    step.setStepType(stepType);
                    step.setNotes(stepDTO.getNotes());
                    step.setTreatmentPlan(realPlan);

                    // Nếu là LAB_TEST
                    if ("LABTEST".equalsIgnoreCase(stepType.getTypeName())) {
                        if (labTests != null && !labTests.isEmpty()) {
                            LabTest labTest = labTests.get(0); // tạm: lấy cái đầu
                            step.setItemId(labTest.getId());
                            step.setResults(stepDTO.getResults());
                        }
                    }

                    // Nếu là PRESCRIPTION
                    else if ("Medication".equalsIgnoreCase(stepType.getTypeName())) {
                        Prescription prescription = createPrescriptionFromDTOs(prescriptions);
                        step.setItemId(prescription.getId());
                    }

                    treatmentStepRepo.save(step);
                    realSteps.add(step);
                }

                realPlan.setSteps(realSteps);
                treatmentPlanRepo.save(realPlan);
            }

            // 5. Lưu lại phiên khám
            session.setUpdatedAt(LocalDateTime.now());
            VisitSession saved = visitSessionRepo.save(session);

            return visitSessionMapper.toDTO(saved);
        }

        // Helper: tạo đơn thuốc từ danh sách DTO
        private Prescription createPrescriptionFromDTOs(List<PrescriptionDetailDTO> prescriptionDTOs) {
            if (prescriptionDTOs == null || prescriptionDTOs.isEmpty()) {
                throw new RuntimeException("Prescription details cannot be empty");
            }

            Prescription prescription = new Prescription();
            prescription.setCreatedAt(LocalDateTime.now());
            prescription = prescriptionRepo.save(prescription); // ✅ save entity để có ID

            List<PrescriptionDetail> details = new ArrayList<>();
            for (PrescriptionDetailDTO dto : prescriptionDTOs) {
                Medication medication = treatmentItemService.getMedicationById(dto.getMedicationId());
                if (medication == null) {
                    throw new RuntimeException("Medication not found: " + dto.getMedicationId());
                }

                PrescriptionDetail detail = mapper.fromDTO(dto, prescription, medication);
                detail = prescriptionDetailRepo.save(detail); // ✅ save detail
                details.add(detail);
            }

            prescription.setDetails(details);
            return prescription;
        }



    public List<VisitSessionDTO> getSessionsByRecord(Long recordId) {
        return visitSessionRepo.findByMedicalRecord_RecordId(recordId)
                .stream()
                .map(visitSessionMapper::toDTO)
                .collect(Collectors.toList());
    }
    public VisitSessionDTO getSessionById(Long id) {
        return visitSessionRepo.findById(id)
                .map(visitSessionMapper::toDTO)
                .orElse(null);
    }
    public VisitSessionDTO createVisitSession(Long recordId, VisitSessionDTO dto) {
        MedicalRecord record = medicalRecordRepo.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Medical record not found"));

        VisitSession session = visitSessionMapper.toEntity(dto);
        session.setMedicalRecord(record);

//        if (dto.getDoctorId() != null) {
//            doctorService.getDoctorDTOById(dto.getDoctorId())
//                    .ifPresent(session::setDoctor);
//        }

        if (dto.getTreatmentPlan() != null && dto.getTreatmentPlan().getId() != null) {
            treatmentPlanRepo.findById(dto.getTreatmentPlan().getId())
                    .ifPresent(session::setTreatmentPlan);
        }

        VisitSession saved = visitSessionRepo.save(session);

        if (!record.getVisitSessions().contains(saved)) {
            record.getVisitSessions().add(saved);
        }

        return visitSessionMapper.toDTO(saved);
    }
    public List<VisitSessionDTO> getSessionByDate(LocalDate localDate) {
        LocalDateTime startOfDay = localDate.atStartOfDay();
        LocalDateTime endOfDay = localDate.atTime(23, 59, 59);

        return visitSessionRepo.findBySessionDateBetween(startOfDay, endOfDay)
                .stream()
                .map(visitSessionMapper::toDTO)
                .collect(Collectors.toList());
    }
    public void deleteVisitSession(Long id) {
        visitSessionRepo.deleteById(id);
    }
}
