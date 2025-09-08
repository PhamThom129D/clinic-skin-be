package com.example.clinic_skin_be.service.ai;

import com.example.clinic_skin_be.model.medical.VisitSession;
import com.example.clinic_skin_be.model.medical.treatment_template.TreatmentTemplate;
import com.example.clinic_skin_be.repository.medical.IVisitSessionRepository;
import com.example.clinic_skin_be.repository.medical.lab_test.ILabTestRepository;
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
    private GeminiService aiClient;

    /**
     * Tạo prompt cho LabTest + Treatment
     */
    public String buildPrompt(String symptoms, List<String> labTests, List<String> treatments) {
        return "Bệnh nhân có triệu chứng: " + symptoms +
                ". Danh sách xét nghiệm có thể chọn: " + String.join(", ", labTests) +
                ". Danh sách phác đồ mẫu có sẵn: " + String.join(", ", treatments) +
                ". Hãy phân tích triệu chứng này và gợi ý:\n" +
                "1) Các xét nghiệm nên làm trước chẩn đoán (possibleLabTests)\n" +
                "2) Các phác đồ điều trị phù hợp (possibleTreatments)\n" +
                "Trả về JSON hợp lệ ví dụ: {\"possibleLabTests\": [\"<xét nghiệm 1>\", ...], \"possibleTreatments\": [\"<phác đồ 1>\", ...] }";
    }

    /**
     * Gợi ý LabTest + Treatment từ sessionId
     */
    public Map<String, Object> suggestLabTestAndTreatment(Long sessionId) {
        VisitSession session = visitSessionRepo.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Visit session not found"));

        List<String> labTests = labTestRepo.findAll().stream()
                .map(p -> p.getName())
                .collect(Collectors.toList());

        List<String> treatments = treatmentTemplateRepo.findAll().stream()
                .map(TreatmentTemplate::getName)
                .collect(Collectors.toList());

        String prompt = buildPrompt(session.getSymptoms(), labTests, treatments);

        // gọi client để nhận kết quả AI
        return aiClient.getAISuggestions(prompt, Arrays.asList("possibleLabTests", "possibleTreatments"));
    }
}
