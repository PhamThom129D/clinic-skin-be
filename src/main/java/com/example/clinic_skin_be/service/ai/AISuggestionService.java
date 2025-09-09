package com.example.clinic_skin_be.service.ai;

import com.example.clinic_skin_be.model.medical.MedicalRecord;
import com.example.clinic_skin_be.model.medical.VisitSession;
import com.example.clinic_skin_be.model.medical.treatment_plan.TreatmentStep;
import com.example.clinic_skin_be.model.medical.treatment_template.TreatmentTemplate;
import com.example.clinic_skin_be.repository.medical.IMedicalRecordRepository;
import com.example.clinic_skin_be.repository.medical.IVisitSessionRepository;
import com.example.clinic_skin_be.repository.medical.lab_test.ILabTestRepository;
import com.example.clinic_skin_be.repository.medical.medication.IMedicationRepository;
import com.example.clinic_skin_be.repository.medical.procedure.IProcedureRepository;
import com.example.clinic_skin_be.repository.medical.treatmnet_template.ITreatmentTemplateRepository;
import com.example.clinic_skin_be.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AISuggestionService {

    @Autowired
    private IVisitSessionRepository visitSessionRepo;

    @Autowired
    private ILabTestRepository labTestRepo;

    @Autowired
    private ITreatmentTemplateRepository treatmentTemplateRepo;

    @Autowired
    private IMedicalRecordRepository medicalRecordRepo;

    @Autowired
    private GeminiService aiClient;
    @Autowired
    private IMedicationRepository medicationRepo;

    @Autowired
    private IProcedureRepository procedureRepo;


    public String buildPromptLabTest(String symptoms, List<String> labTests, List<String> diseases) {
        return "Bệnh nhân có triệu chứng: " + symptoms +
                ". Chỉ sử dụng các xét nghiệm có sẵn: " + String.join(", ", labTests) +
                ". Chỉ sử dụng các bệnh có sẵn: " + String.join(", ", diseases) +
                ". Hãy phân tích triệu chứng này và gợi ý:\n" +
                "1) Các xét nghiệm nên làm trước chẩn đoán (possibleLabTests)\n" +
                "2) Các bệnh có thể gặp phải phù hợp (possibleDiseases)\n" +
                "Nếu không có xét nghiệm hoặc bệnh nào phù hợp, trả về mảng rỗng [] tương ứng.\n" +
                "Trả về JSON hợp lệ, ví dụ: " +
                "{\"possibleLabTests\": [\"<xét nghiệm>\", ...], " +
                "\"possibleDiseases\": [\"<bệnh>\", ...]}";
    }

    public Map<String, Object> suggestLabTestAndTreatment(String symptoms) {
        // Lấy danh sách tất cả xét nghiệm
        List<String> labTests = labTestRepo.findAll().stream()
                .map(p -> p.getName())
                .collect(Collectors.toList());

        // Lấy danh sách bệnh từ treatment template
        List<String> disease = treatmentTemplateRepo.findAll().stream()
                .map(TreatmentTemplate::getDisease_name)
                .collect(Collectors.toList());

        // Tạo prompt
        String prompt = buildPromptLabTest(symptoms, labTests, disease);

        // Gọi AI client
        return aiClient.getAISuggestions(prompt, Arrays.asList("possibleLabTests", "possibleDiseases"));
    }


    /** Tạo prompt tóm tắt lịch sử khám */
    public String buildPromptVisitHistory(String symptoms, List<String> medications, List<String> procedures,
                                          List<String> labTests, List<String> results) {
        return "Bệnh nhân có triệu chứng: " + symptoms + ".\n" +
                "Tiền sử điều trị trước đó:\n" +
                "- Thuốc đã sử dụng: " + String.join(", ", medications) + "\n" +
                "- Thủ thuật đã thực hiện: " + String.join(", ", procedures) + "\n" +
                "- Xét nghiệm đã làm: " + String.join(", ", labTests) + "\n" +
                "- Kết quả từng bước / kết quả tổng quan: " + String.join("; ", results) + "\n\n" +
                "Yêu cầu: Hãy tóm tắt **ngắn gọn 1-2 dòng**, " +
                "chỉ gồm **thuốc + xét nghiệm + thủ thuật + kết quả tổng quan**, " +
                "nhưng vẫn đủ thông tin để bác sĩ nắm rõ tiền sử và kết quả điều trị trước đó. " +
                "Trả về **JSON hợp lệ** với key \"superShort\". Ví dụ:\n" +
                "{\n" +
                "  \"superShort\": \"Thuốc: Acid Salicylic, Hydrocortisone; Xét nghiệm: Test dị ứng - Không dị ứng; Thủ thuật: Laser CO2; Kết quả: Nám mờ nhẹ, vùng nám sáng 20%, tiến triển tốt\"\n" +
                "}";
    }


    public Map<String, Object> suggestVisitSummaryByRecord(Long recordId) {
        MedicalRecord record = medicalRecordRepo.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Medical record not found"));

        List<VisitSession> sessions = record.getVisitSessions() != null ? record.getVisitSessions() : List.of();

        // Thuốc
        List<String> medications = sessions.stream()
                .flatMap(session -> session.getTreatmentPlan() != null && session.getTreatmentPlan().getSteps() != null
                        ? session.getTreatmentPlan().getSteps().stream()
                        .filter(s -> s.getStepType().getTypeName().equalsIgnoreCase("Medication"))
                        .map(s -> {
                            if(s.getItemId() != null) {
                                return medicationRepo.findById(s.getItemId())
                                        .map(m -> m.getName() + " (" + m.getName() + ", " )
                                        .orElse("Thuốc không rõ");
                            } else {
                                return "Thuốc không rõ";
                            }
                        })
                        : List.<String>of().stream())
                .toList();

        // Thủ thuật
        List<String> procedures = sessions.stream()
                .flatMap(session -> session.getTreatmentPlan() != null && session.getTreatmentPlan().getSteps() != null
                        ? session.getTreatmentPlan().getSteps().stream()
                        .filter(s -> s.getStepType().getTypeName().equalsIgnoreCase("Procedure"))
                        .map(s -> {
                            if(s.getItemId() != null) {
                                return procedureRepo.findById(s.getItemId())
                                        .map(p -> p.getName() + " (" + p.getDescription() + ")")
                                        .orElse("Thủ thuật không rõ");
                            } else {
                                return "Thủ thuật không rõ";
                            }
                        })
                        : List.<String>of().stream())
                .toList();

        // Xét nghiệm
        List<String> labTests = sessions.stream()
                .flatMap(session -> session.getTreatmentPlan() != null && session.getTreatmentPlan().getSteps() != null
                        ? session.getTreatmentPlan().getSteps().stream()
                        .filter(s -> s.getStepType().getTypeName().equalsIgnoreCase("LabTest"))
                        .map(s -> {
                            if(s.getItemId() != null) {
                                return labTestRepo.findById(s.getItemId())
                                        .map(l -> l.getName())
                                        .orElse("Xét nghiệm không rõ");
                            } else {
                                return "Xét nghiệm không rõ";
                            }
                        })
                        : List.<String>of().stream())
                .toList();

        // Kết quả từng bước
        List<String> results = sessions.stream()
                .flatMap(session -> session.getTreatmentPlan() != null && session.getTreatmentPlan().getSteps() != null
                        ? session.getTreatmentPlan().getSteps().stream()
                        .map(s -> {
                            String stepResult = s.getResults() != null ? s.getResults() : "Chưa có kết quả";
                            String type = s.getStepType().getTypeName().toLowerCase();
                            String name = switch (type) {
                                case "medication" -> s.getItemId() != null ? medicationRepo.findById(s.getItemId()).map(m -> m.getName()).orElse("Thuốc không rõ") : "Thuốc không rõ";
                                case "procedure" -> s.getItemId() != null ? procedureRepo.findById(s.getItemId()).map(p -> p.getName()).orElse("Thủ thuật không rõ") : "Thủ thuật không rõ";
                                case "labtest" -> s.getItemId() != null ? labTestRepo.findById(s.getItemId()).map(l -> l.getName()).orElse("Xét nghiệm không rõ") : "Xét nghiệm không rõ";
                                default -> "Bước không rõ";
                            };
                            return type.substring(0, 1).toUpperCase() + type.substring(1) + ": " + name + " → " + stepResult;
                        })
                        : List.<String>of().stream())
                .toList();

        // Triệu chứng tổng hợp
        String symptoms = sessions.stream()
                .map(VisitSession::getSymptoms)
                .filter(s -> s != null && !s.isEmpty())
                .distinct()
                .collect(Collectors.joining("; "));

        String prompt = buildPromptVisitHistory(symptoms, medications, procedures, labTests, results);

        return aiClient.getAISuggestions(prompt, Arrays.asList("superShort"));
    }


}
