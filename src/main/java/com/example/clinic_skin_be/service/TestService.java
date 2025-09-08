package com.example.clinic_skin_be.service;

import com.example.clinic_skin_be.model.medical.VisitSession;
import com.example.clinic_skin_be.model.medical.treatment_template.TreatmentTemplate;
import com.example.clinic_skin_be.model.medical.treatment_plan.TreatmentStep;
import com.example.clinic_skin_be.repository.medical.IVisitSessionRepository;
import com.example.clinic_skin_be.repository.medical.lab_test.ILabTestRepository;
import com.example.clinic_skin_be.repository.medical.procedure.IProcedureRepository;
import com.example.clinic_skin_be.repository.medical.treatmnet_template.ITreatmentTemplateRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TestService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private static final String GEMINI_API_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=";

    @Autowired
    private IVisitSessionRepository visitSessionRepo;
    @Autowired
    private IProcedureRepository procedureRepo;
    @Autowired
    private ILabTestRepository labTestRepo;
    @Autowired
    private ITreatmentTemplateRepository treatmentTemplateRepo;


    public Map<String, Object> suggestLabTestAndTreatment(Long sessionId) throws IOException {
        VisitSession session = visitSessionRepo.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Visit session not found"));

        List<String> labTests = labTestRepo.findAll()
                .stream().map(p -> p.getName()).toList();

        List<String> treatments = treatmentTemplateRepo.findAll()
                .stream().map(TreatmentTemplate::getName).toList();

        String prompt = "Bệnh nhân có triệu chứng: " + session.getSymptoms() +
                ". Danh sách xét nghiệm có thể chọn: " + String.join(", ", labTests) +
                ". Danh sách phác đồ mẫu có sẵn: " + String.join(", ", treatments) +
                ". Hãy phân tích triệu chứng này và gợi ý:\n" +
                "1) Các xét nghiệm nên làm trước chẩn đoán (possibleLabTests)\n" +
                "2) Các phác đồ điều trị phù hợp (possibleTreatments)\n" +
                "Trả về JSON hợp lệ ví dụ: {\"possibleLabTests\": [\"<xét nghiệm 1>\", ...], \"possibleTreatments\": [\"<phác đồ 1>\", ...], \"aiNotes\": \"...\"}";

        String jsonInput = "{ \"contents\": [{\"parts\":[{\"text\":\"" + prompt.replace("\"","\\\"") + "\"}]}]}";

        URL url = new URL(GEMINI_API_URL + geminiApiKey);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonInput.getBytes(StandardCharsets.UTF_8));
        }

        InputStream is = (conn.getResponseCode() >= 400) ? conn.getErrorStream() : conn.getInputStream();
        String response = (is != null) ? new String(is.readAllBytes(), StandardCharsets.UTF_8) : "";

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response);

        Map<String, Object> result = new HashMap<>();
        if (root.has("candidates") && root.get("candidates").isArray() && root.get("candidates").size() > 0) {
            String text = root.get("candidates").get(0).path("content").path("parts").get(0).path("text").asText();
            if (text.startsWith("```")) text = text.replaceAll("```json","").replaceAll("```","").trim();
            try {
                JsonNode jsonNode = mapper.readTree(text);
                result.put("possibleLabTests", jsonNode.has("possibleLabTests") ? jsonNode.get("possibleLabTests") : Collections.emptyList());
                result.put("possibleTreatments", jsonNode.has("possibleTreatments") ? jsonNode.get("possibleTreatments") : Collections.emptyList());
            } catch (Exception e) {
                // fallback: trả về rỗng + ghi log
                result.put("possibleLabTests", Collections.emptyList());
                result.put("possibleTreatments", Collections.emptyList());
            }
        }
        return result;
    }






//    // --- 2. Hàm lấy phác đồ điều trị theo tên bệnh
//    public Map<String, Object> getTreatmentForDisease(String diagnosis) {
//        List<TreatmentStep> stepsFromDB = stepRepository.findByDisease_Name(diagnosis);
//
//        List<Map<String, Object>> steps = new ArrayList<>();
//        int stepNumber = 1;
//        for (TreatmentStep step : stepsFromDB) {
//            List<Map<String, Object>> meds = medicationRepository.findByStep_Disease_Name(diagnosis)
//                    .stream()
//                    .map(m -> {
//                        Map<String, Object> medMap = new HashMap<>();
//                        medMap.put("name", m.getName());
//                        medMap.put("dosage", m.getDosage());
//                        medMap.put("usageInstructions", m.getUsageInstructions());
//                        medMap.put("price", m.getPrice());
//                        return medMap;
//                    })
//                    .collect(Collectors.toList());
//
//            Map<String, Object> stepMap = new HashMap<>();
//            stepMap.put("stepNumber", stepNumber++);
//            stepMap.put("type", step.getStepType().getTypeName());
//            stepMap.put("description", step.getDescription());
//            stepMap.put("notes", step.getNotes());
//            stepMap.put("medications", meds);
//
//            steps.add(stepMap);
//        }
//
//        String treatmentTemplate = stepsFromDB.stream()
//                .map(TreatmentStep::getDescription)
//                .collect(Collectors.joining("; "));
//
//        Map<String, Object> resultMap = new LinkedHashMap<>();
//        resultMap.put("diagnosis", diagnosis);
//        resultMap.put("treatmentTemplate", treatmentTemplate);
//        resultMap.put("steps", steps);
//
//        return resultMap;
//    }
}

