package com.example.clinic_skin_be.service.medical;

import com.example.clinic_skin_be.dto.medical.DoctorVisitSessionDTO;
import com.example.clinic_skin_be.dto.medical.VisitSessionDTO;
import com.example.clinic_skin_be.dto.medical.treatment_template.PrescriptionDTO;
import com.example.clinic_skin_be.dto.medical.treatment_template.PrescriptionDetailDTO;
import com.example.clinic_skin_be.dto.medical.treatment_template.TreatmentStepTemplateDTO;
import com.example.clinic_skin_be.mapper.PrescriptionMapper;
import com.example.clinic_skin_be.mapper.TreatmentPlanMapper;
import com.example.clinic_skin_be.mapper.VisitSessionMapper;
import com.example.clinic_skin_be.model.medical.MedicalRecord;
import com.example.clinic_skin_be.model.medical.VisitSession;
import com.example.clinic_skin_be.model.medical.medication.Medication;
import com.example.clinic_skin_be.model.medical.medication.Prescription;
import com.example.clinic_skin_be.model.medical.medication.PrescriptionDetail;
import com.example.clinic_skin_be.model.medical.treatment_plan.StepType;
import com.example.clinic_skin_be.model.medical.treatment_plan.TreatmentPlan;
import com.example.clinic_skin_be.model.medical.treatment_plan.TreatmentStep;
import com.example.clinic_skin_be.model.medical.treatment_template.TreatmentTemplate;
import com.example.clinic_skin_be.model.staff.doctor.Doctor;
import com.example.clinic_skin_be.repository.medical.IMedicalRecordRepository;
import com.example.clinic_skin_be.repository.medical.treatment_plan.ITreatmentPlanRepository;
import com.example.clinic_skin_be.repository.medical.IVisitSessionRepository;
import com.example.clinic_skin_be.repository.staff.IDoctorRepository;
import com.example.clinic_skin_be.service.medical.treatment_plan.TreatmentPlanService;
import com.example.clinic_skin_be.service.medical.treatment_template.PrescriptionService;
import com.example.clinic_skin_be.service.medical.treatment_template.TreatmentItemService;
import com.example.clinic_skin_be.service.medical.treatment_template.TreatmentTemplateService;
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
    private final IDoctorRepository doctorRepo;
    private final ITreatmentPlanRepository treatmentPlanRepo;
    private final VisitSessionMapper visitSessionMapper;
    private final PrescriptionService prescriptionService;
    private final TreatmentPlanService treatmentPlanService;
    private final TreatmentTemplateService treatmentTemplateServiceService;
    private final PrescriptionMapper mapper;
    private final TreatmentPlanMapper treatmentPlanMapper;
    private final StaffService staffService;
    private final MedicalRecordService medicalRecordService;
    private final TreatmentItemService treatmentItemService;
    private final PrescriptionMapper prescriptionMapper;


    public void addPrescriptionToSession(VisitSession session,
                                         List<PrescriptionDetailDTO> prescriptionDTOs,
                                         TreatmentStepTemplateDTO stepTemplateDTO) {
        if (prescriptionDTOs == null || prescriptionDTOs.isEmpty()) return;

        // 1. Tạo đơn thuốc mới
        Prescription prescription = new Prescription();
        prescription.setCreatedAt(LocalDateTime.now());

        // Lưu đơn thuốc để có ID
        PrescriptionDTO savedPrescription = prescriptionService.savePrescription(prescription);
        prescription = prescriptionMapper.fromDTO(savedPrescription);
        prescription.setId(savedPrescription.getId());

        // 2. Tạo chi tiết thuốc dựa trên DTO và lưu
        List<PrescriptionDetail> details = new ArrayList<>();
        for (PrescriptionDetailDTO dto : prescriptionDTOs) {
            Medication medication = treatmentItemService.getMedicationById(dto.getMedicationId());
            if (medication == null) {
                throw new RuntimeException("Medication not found: " + dto.getMedicationId());
            }

            PrescriptionDetail detail = mapper.fromDTO(dto, prescription, medication);
            PrescriptionDetailDTO savedDetail = prescriptionService.savePrescriptionDetail(detail);
            detail.setId(savedDetail.getId());
            detail = mapper.fromDTO(dto, prescription, medication);
            details.add(detail);
        }

        // 3. Gán chi tiết vào đơn thuốc
        savedPrescription.setDetails(prescriptionMapper.toDetailDTO(details));

        // 4. Gán đơn thuốc vào bước thực tế trong phác đồ
        TreatmentPlan treatmentPlan = session.getTreatmentPlan();
        if (treatmentPlan == null) {
            throw new RuntimeException("VisitSession chưa có TreatmentPlan");
        }

        // Lấy step thực tế tương ứng (nếu chưa có, tạo mới dựa trên stepTemplateDTO)
        TreatmentStep step;
        if (stepTemplateDTO.getId() != null) {
            step = treatmentPlanService.getStepEntityById(stepTemplateDTO.getId());
        } else {
            step = new TreatmentStep();
            step.setStepNumber(stepTemplateDTO.getStepNumber());
            step.setStepType(treatmentItemService.getStepTypeById(stepTemplateDTO.getStepTypeId()));
            step.setNotes(stepTemplateDTO.getNotes());
            step.setTreatmentPlan(treatmentPlan);
        }

        step.setItemId(savedPrescription.getId());

        TreatmentStepTemplateDTO savedStepDTO = treatmentPlanMapper.toStepDTO(step);
        step.setId(savedStepDTO.getId());
        treatmentPlanService.saveStep(savedStepDTO, step.getStepType());

        // Nếu step mới thì thêm vào phác đồ thực tế
        if (!treatmentPlan.getSteps().contains(step)) {
            treatmentPlan.getSteps().add(step);
            treatmentPlanRepo.save(treatmentPlan);
        }

    }


    public VisitSessionDTO updateVisitSession(
            DoctorVisitSessionDTO visitDTO,
            VisitSessionDTO dto,
            List<TreatmentStepTemplateDTO> stepDTO,
            List<PrescriptionDetailDTO> prescriptions
    ) {
        VisitSession session = visitSessionRepo.findById(visitDTO.getSessionId())
                .orElseThrow(() -> new RuntimeException("Visit session not found"));
        TreatmentTemplate template = treatmentTemplateServiceService.getTreatmentTemplateEntityById(dto.getTreatmentPlan().getId());
        assignDoctorToSession(visitDTO, dto, session);

        TreatmentPlan realPlan = cloneTemplate(dto, template);
        // 4. Cập nhật phác đồ điều trị nếu có
        if (realPlan != null && stepDTO != null) {
            List<TreatmentStep> steps = new ArrayList<>();
            for (TreatmentStepTemplateDTO step : stepDTO) {
                addPrescriptionToSession(session,prescriptions,step);
                StepType stepType = treatmentItemService.getStepTypeById(step.getStepTypeId());
                TreatmentStep newStep = treatmentPlanMapper.fromDTO(step,realPlan, stepType);
                newStep.setTreatmentPlan(realPlan);
                treatmentPlanService.saveStep(step , stepType);
                steps.add(newStep);
            }
            realPlan.setSteps(steps);
        }
        VisitSession saved = visitSessionRepo.save(session);
        return visitSessionMapper.toDTO(saved);
    }

    public void assignDoctorToSession(DoctorVisitSessionDTO visitDTO, VisitSessionDTO dto, VisitSession session) {
        // 2. Cập nhật doctor dựa vào tài khoản đăng nhập (nếu có)
        if (visitDTO.getDoctorId() != null) {
            Doctor doctor = staffService.getDoctorById(visitDTO.getDoctorId())
                    .orElseThrow(() -> new RuntimeException("Doctor not found"));
            session.setDoctor(doctor);
        }
        // 3. Cập nhật triệu chứng, chan doan
        if (dto.getSymptoms() != null) {
            session.setSymptoms(dto.getSymptoms());
        }
        if (dto.getDiagnosis() != null) {
            session.setDiagnosis(dto.getDiagnosis());
        }
        session.setUpdatedAt(LocalDateTime.now());

    }
    public TreatmentPlan cloneTemplate(VisitSessionDTO dto, TreatmentTemplate template){
        if (template == null) {
            throw new RuntimeException("Treatment template not found");
        }
        TreatmentPlan realPlan = new TreatmentPlan();
        realPlan.setTreatmentName(template.getName().concat("- Phác đò ").concat(dto.getPatientName()));
        realPlan.setDisease_name(template.getDiseaseName());

        treatmentPlanService.savePlanEntity(realPlan);
        return realPlan;
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

        if (dto.getDoctorId() != null) {
            staffService.getDoctorById(dto.getDoctorId())
                    .ifPresent(session::setDoctor);
        }

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
