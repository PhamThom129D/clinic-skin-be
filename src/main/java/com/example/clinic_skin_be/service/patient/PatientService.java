// File: com.example.clinic_skin_be.service.patient.PatientService.java

package com.example.clinic_skin_be.service.patient;

import com.example.clinic_skin_be.dto.patient.AppointmentHistorySummaryDTO;
import com.example.clinic_skin_be.dto.patient.appointmentdetail.*; // Giả định chứa tất cả DTO con
import com.example.clinic_skin_be.model.manage_enum.ConsultationStatus;
import com.example.clinic_skin_be.model.medical.MedicalRecord;
import com.example.clinic_skin_be.model.medical.VisitSession;
import com.example.clinic_skin_be.model.medical.treatment_plan.TreatmentPlan;
import com.example.clinic_skin_be.model.medical.treatment_plan.TreatmentStep;
import com.example.clinic_skin_be.model.staff.doctor.Doctor;
import com.example.clinic_skin_be.model.user.Account;
import com.example.clinic_skin_be.model.user.Appointment;
import com.example.clinic_skin_be.model.medical.lab_test.LabTest;
import com.example.clinic_skin_be.model.medical.procedure.Procedure;
import com.example.clinic_skin_be.repository.medical.IMedicalRecordRepository;
import com.example.clinic_skin_be.repository.medical.IVisitSessionRepository;
import com.example.clinic_skin_be.repository.medical.lab_test.ILabTestRepository;
import com.example.clinic_skin_be.repository.medical.procedure.IProcedureRepository;
import com.example.clinic_skin_be.repository.medical.treatment_plan.ITreatmentPlanRepository;
import com.example.clinic_skin_be.repository.medical.treatment_plan.ITreatmentStepRepository;
import com.example.clinic_skin_be.repository.staff.IDoctorRepository;
import com.example.clinic_skin_be.repository.staff.booking.IAppointmentRepository;
import com.example.clinic_skin_be.repository.user.IAccountRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {

    @PersistenceContext
    private EntityManager entityManager;
    @Transactional(readOnly = true)
    public List<AppointmentHistorySummaryDTO> getAppointmentHistoryForPatient(Long accountId) {
        Query query = entityManager.createNativeQuery(
                "{CALL GetPatientAppointmentHistoryByAccount(:p_account_id)}"
        );
        query.setParameter("p_account_id", accountId);

        List<Object[]> results = query.getResultList();

        return results.stream()
                .map(row -> {
                    // 0: appointment_id
                    Long appointmentId = row[0] != null ? ((Number) row[0]).longValue() : null;
                    // 1: status
                    String status = (String) row[1];
                    // 2: appointment_date_time
                    String appointmentDateTime = (String) row[2];
                    // 3: appointment_note
                    String appointmentNote = (String) row[3];
                    // 4: record_id
                    Long recordId = row[4] != null ? ((Number) row[4]).longValue() : null;
                    // 5: doctor_name
                    String doctorName = (String) row[5];

                    return new AppointmentHistorySummaryDTO(
                            appointmentId,
                            recordId,
                            status,
                            appointmentDateTime,
                            appointmentNote,
                            doctorName
                    );
                })
                .filter(dto -> dto.getAppointmentId() != null)
                .collect(Collectors.toList());
    }

    @Autowired
    private IMedicalRecordRepository medicalRecordRepo;
    @Autowired
    private IVisitSessionRepository visitSessionRepo;
    @Autowired
    private IAppointmentRepository appointmentRepo;
    @Autowired
    private IDoctorRepository doctorRepo;
    @Autowired
    private IAccountRepository accountRepo;
    @Autowired
    private ITreatmentPlanRepository treatmentPlanRepo;
    @Autowired
    private ITreatmentStepRepository treatmentStepRepo;
    @Autowired
    private ILabTestRepository labTestRepo;
    @Autowired
    private IProcedureRepository procedureRepo;

    // Định dạng ngày giờ cho DTO
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public MedicalRecordDetailDTO getMedicalRecordDetails(Long recordId) {

        // 1. LẤY RECORD_ID VÀ XÁC THỰC
        MedicalRecord record = medicalRecordRepo.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Hồ sơ y tế không tồn tại."));

        VisitSession session = visitSessionRepo.findFirstByMedicalRecord_RecordId(recordId)
                .orElseThrow(() -> new RuntimeException("Phiên khám không tồn tại cho hồ sơ này."));

        // --- 2. LẤY CÁC THÀNH PHẦN KHÁC ---

        // Mục 1: Thông tin Lịch hẹn
        List<ConsultationStatus> acceptedStatuses = List.of(
                ConsultationStatus.COMPLETED,
                ConsultationStatus.IN_PROGRESS
        );
        Appointment appointment = appointmentRepo.findFirstByPatient_AccountIdAndDateAndStatusIn(
                        record.getPatient().getAccount().getId(),
                        record.getVisitDate(),
                        acceptedStatuses
                ).orElse(null);

        // Mục 2: Thông tin Bác sĩ
        Doctor doctor = session.getDoctor();
        Account doctorAccount = doctor != null ? doctor.getAccount() : null;

        // Mục 4: Phác đồ Điều trị và Steps
        TreatmentPlan plan = session.getTreatmentPlan();
        List<TreatmentStep> steps = List.of();

        if (plan != null) {
            steps = treatmentStepRepo.findAllByTreatmentPlanId(plan.getId());
        }

        // --- 3. ÁNH XẠ (MAPPING) VÀ TỔNG HỢP DTO ---

        MedicalRecordDetailDTO detailDTO = new MedicalRecordDetailDTO();

        if (appointment != null) detailDTO.setAppointmentInfo(mapToAppointmentSummaryDTO(appointment));
        if (doctor != null && doctorAccount != null) detailDTO.setDoctorInfo(mapToDoctorSummaryDTO(doctor, doctorAccount));

        detailDTO.setClinicalDetails(mapToClinicalDetailsDTO(session));

        if (plan != null) {
            detailDTO.setTreatmentPlan(mapToTreatmentPlanSummaryDTO(plan));
            detailDTO.setSteps(mapToTreatmentStepDTOs(steps));
        }

        // Tùy chọn: Thêm dòng này nếu MedicalRecordDetailDTO có field prescriptions (List.of() rỗng)
        // detailDTO.setPrescriptions(List.of());

        return detailDTO;
    }

    // --- CÁC HÀM ÁNH XẠ HỖ TRỢ (Mapper Methods) ---

    // mapToAppointmentSummaryDTO, mapToDoctorSummaryDTO, mapToClinicalDetailsDTO, mapToTreatmentPlanSummaryDTO giữ nguyên...

    // ÁNH XẠ PHỨC TẠP CHO LIST CÁC BƯỚC ĐIỀU TRỊ
    private List<TreatmentStepDTO> mapToTreatmentStepDTOs(List<TreatmentStep> steps) {
        return steps.stream()
                .map(step -> {
                    String typeName = step.getStepType().getTypeName();
                    TreatmentStepDTO dto = new TreatmentStepDTO(
                            step.getStepNumber(),
                            typeName,
                            step.getNotes(),
                            step.getResults(),
                            resolveStepItemName(step, typeName)
                    );
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * TÌM TÊN ITEM (Chỉ còn Xét nghiệm và Thủ thuật)
     */
    private String resolveStepItemName(TreatmentStep step, String typeName) {
        Long itemId = step.getItemId();

        if (typeName.equals("LabTest")) {
            return labTestRepo.findById(itemId)
                    .map(LabTest::getName)
                    .orElse("Xét nghiệm không xác định");
        } else if (typeName.equals("Procedure")) {
            return procedureRepo.findById(itemId)
                    .map(Procedure::getName)
                    .orElse("Thủ thuật không xác định");
        }
        // Loại bỏ block if (typeName.equals("Medication"))

        // Nếu typeName là Medication hoặc không khớp
        return null;
    }

    // Bổ sung các hàm Mapper cơ bản (giữ nguyên logic đã có)

    private AppointmentSummaryDTO mapToAppointmentSummaryDTO(Appointment entity) {
        String dateTime = entity.getDate().atTime(entity.getTime()).format(DATE_TIME_FORMATTER);
        return new AppointmentSummaryDTO(
                entity.getId(),
                entity.getStatus().name(),
                dateTime,
                entity.getNote()
        );
    }

    private DoctorSummaryDTO mapToDoctorSummaryDTO(Doctor doctor, Account account) {
        return new DoctorSummaryDTO(
                doctor.getId(),
                account.getFullName(),
                doctor.getSpecialty()
        );
    }

    private ClinicalDetailsDTO mapToClinicalDetailsDTO(VisitSession entity) {
        return new ClinicalDetailsDTO(
                entity.getSymptoms(),
                entity.getDiagnosis(),
                entity.getClinicalNotes()
        );
    }

    private TreatmentPlanSummaryDTO mapToTreatmentPlanSummaryDTO(TreatmentPlan entity) {
        return new TreatmentPlanSummaryDTO(
                entity.getId(),
                entity.getTreatmentName(),
                entity.getDisease_name()
        );
    }
}