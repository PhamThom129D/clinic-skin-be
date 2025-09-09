package com.example.clinic_skin_be.service.ai;

import com.example.clinic_skin_be.model.medical.MedicalRecord;
import com.example.clinic_skin_be.model.medical.VisitSession;
import com.example.clinic_skin_be.model.medical.treatment_template.TreatmentTemplate;
import com.example.clinic_skin_be.repository.medical.IMedicalRecordRepository;
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
    private ILabTestRepository labTestRepo;

    @Autowired
    private ITreatmentTemplateRepository treatmentTemplateRepo;

    @Autowired
    private IMedicalRecordRepository medicalRecordRepo;

    @Autowired
    private IMedicationRepository medicationRepo;

    @Autowired
    private IProcedureRepository procedureRepo;

    @Autowired
    private GeminiService aiClient;

    /**
     * Build prompt cho AI. Nếu có kết quả xét nghiệm, sẽ yêu cầu chẩn đoán duy nhất.
     */
    private String buildPrompt(String symptoms, List<String> allLabTests, List<String> allDiseases,
                               String labTest, String labResult) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Bệnh nhân có triệu chứng: ").append(symptoms)
                .append(". Chỉ sử dụng các xét nghiệm có sẵn: ").append(String.join(", ", allLabTests))
                .append(". Chỉ sử dụng các bệnh có sẵn: ").append(String.join(", ", allDiseases))
                .append(".\n");

        if (labTest != null && !labTest.isEmpty() && labResult != null && !labResult.isEmpty()) {
            prompt.append("Kết quả xét nghiệm: ").append(labTest)
                    .append(" = ").append(labResult).append(".\n")
                    .append("Dựa vào kết quả này . Chỉ sử dụng các bệnh có sẵn: ").append(String.join(", ", allDiseases))
                    .append("không được thêm bệnh mới.\n");
        } else {
            prompt.append("Hãy phân tích triệu chứng này và gợi ý:\n")
                    .append("1) Các xét nghiệm nên làm trước chẩn đoán (possibleLabTests)\n")
                    .append("2) Các bệnh có thể gặp phải phù hợp (possibleDiseases)\n");
        }

        prompt.append("Trả về JSON hợp lệ, bắt buộc chỉ chọn từ danh sách có sẵn, ví dụ: ")
                .append("{\"possibleLabTests\": [\"<xét nghiệm>\", ...], ")
                .append("\"possibleDiseases\": [\"<bệnh>\", ...]}");

        return prompt.toString();
    }

    /**
     * Gợi ý xét nghiệm + bệnh dựa trên triệu chứng, hoặc chẩn đoán duy nhất nếu có kết quả xét nghiệm.
     */
    public Map<String, Object> suggestLabTestAndTreatment(String symptoms, String labTest, String labResult) {
        List<String> allLabTests = labTestRepo.findAll().stream()
                .map(p -> p.getName())
                .collect(Collectors.toList());

        List<String> allDiseases = treatmentTemplateRepo.findAll().stream()
                .map(TreatmentTemplate::getDisease_name)
                .collect(Collectors.toList());

        String prompt = buildPrompt(symptoms, allLabTests, allDiseases, labTest, labResult);

        // Nếu có labResult, trả về chỉ possibleDiseases, không cần possibleLabTests
        List<String> keys = (labTest != null && !labTest.isEmpty() && labResult != null && !labResult.isEmpty())
                ? Arrays.asList("possibleDiseases")
                : Arrays.asList("possibleLabTests", "possibleDiseases");

        return aiClient.getAISuggestions(prompt, keys);
    }


    // --- Tóm tắt lịch sử khám
    private String buildPromptVisitHistory(String symptoms, List<String> medications, List<String> procedures,
                                           List<String> labTests, List<String> results) {
        return "Bệnh nhân có triệu chứng: " + symptoms + ".\n" +
                "Tiền sử điều trị trước đó:\n" +
                "- Thuốc đã sử dụng: " + String.join(", ", medications) + "\n" +
                "- Thủ thuật đã thực hiện: " + String.join(", ", procedures) + "\n" +
                "- Xét nghiệm đã làm: " + String.join(", ", labTests) + "\n" +
                "- Kết quả từng bước / kết quả tổng quan: " + String.join("; ", results) + "\n\n" +
                "Yêu cầu: Hãy tóm tắt **ngắn gọn 1-2 dòng**, " +
                "chỉ gồm **thuốc + xét nghiệm + thủ thuật + kết quả tổng quan**, " +
                "trả về **JSON hợp lệ** với key \"superShort\"";
    }

    public Map<String, Object> suggestVisitSummaryByRecord(Long recordId) {
        MedicalRecord record = medicalRecordRepo.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Medical record not found"));

        List<VisitSession> sessions = record.getVisitSessions() != null ? record.getVisitSessions() : List.of();

        // --- Thuốc
        List<String> medications = sessions.stream()
                .flatMap(session -> session.getTreatmentPlan() != null && session.getTreatmentPlan().getSteps() != null
                        ? session.getTreatmentPlan().getSteps().stream()
                        .filter(s -> s.getStepType().getTypeName().equalsIgnoreCase("Medication"))
                        .map(s -> s.getItemId() != null
                                ? medicationRepo.findById(s.getItemId()).map(m -> m.getName()).orElse("Thuốc không rõ")
                                : "Thuốc không rõ")
                        : List.<String>of().stream())
                .toList();

        // --- Thủ thuật
        List<String> procedures = sessions.stream()
                .flatMap(session -> session.getTreatmentPlan() != null && session.getTreatmentPlan().getSteps() != null
                        ? session.getTreatmentPlan().getSteps().stream()
                        .filter(s -> s.getStepType().getTypeName().equalsIgnoreCase("Procedure"))
                        .map(s -> s.getItemId() != null
                                ? procedureRepo.findById(s.getItemId()).map(p -> p.getName()).orElse("Thủ thuật không rõ")
                                : "Thủ thuật không rõ")
                        : List.<String>of().stream())
                .toList();

        // --- Xét nghiệm
        List<String> labTests = sessions.stream()
                .flatMap(session -> session.getTreatmentPlan() != null && session.getTreatmentPlan().getSteps() != null
                        ? session.getTreatmentPlan().getSteps().stream()
                        .filter(s -> s.getStepType().getTypeName().equalsIgnoreCase("LabTest"))
                        .map(s -> s.getItemId() != null
                                ? labTestRepo.findById(s.getItemId()).map(l -> l.getName()).orElse("Xét nghiệm không rõ")
                                : "Xét nghiệm không rõ")
                        : List.<String>of().stream())
                .toList();

        // --- Kết quả
        List<String> results = sessions.stream()
                .flatMap(session -> session.getTreatmentPlan() != null && session.getTreatmentPlan().getSteps() != null
                        ? session.getTreatmentPlan().getSteps().stream()
                        .map(s -> {
                            String stepResult = s.getResults() != null ? s.getResults() : "Chưa có kết quả";
                            String type = s.getStepType().getTypeName().toLowerCase();
                            String name = switch (type) {
                                case "medication" -> s.getItemId() != null
                                        ? medicationRepo.findById(s.getItemId()).map(m -> m.getName()).orElse("Thuốc không rõ")
                                        : "Thuốc không rõ";
                                case "procedure" -> s.getItemId() != null
                                        ? procedureRepo.findById(s.getItemId()).map(p -> p.getName()).orElse("Thủ thuật không rõ")
                                        : "Thủ thuật không rõ";
                                case "labtest" -> s.getItemId() != null
                                        ? labTestRepo.findById(s.getItemId()).map(l -> l.getName()).orElse("Xét nghiệm không rõ")
                                        : "Xét nghiệm không rõ";
                                default -> "Bước không rõ";
                            };
                            return type.substring(0, 1).toUpperCase() + type.substring(1) + ": " + name + " → " + stepResult;
                        })
                        : List.<String>of().stream())
                .toList();

        String symptoms = sessions.stream()
                .map(VisitSession::getSymptoms)
                .filter(s -> s != null && !s.isEmpty())
                .distinct()
                .collect(Collectors.joining("; "));

        String prompt = buildPromptVisitHistory(symptoms, medications, procedures, labTests, results);
        return aiClient.getAISuggestions(prompt, Arrays.asList("superShort"));
    }
}
