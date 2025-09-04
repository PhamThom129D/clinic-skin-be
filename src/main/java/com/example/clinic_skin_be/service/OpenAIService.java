package com.example.clinic_skin_be.service;

import com.example.clinic_skin_be.model.Disease;
import com.example.clinic_skin_be.model.TreatmentStep;
import com.example.clinic_skin_be.repository.IDiseaseRepository;
import com.example.clinic_skin_be.repository.IMedicationRepository;
import com.example.clinic_skin_be.repository.ITreatmentStepRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OpenAIService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private static final String GEMINI_API_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=";

    private final IDiseaseRepository diseaseRepository;
    private final ITreatmentStepRepository stepRepository;
    private final IMedicationRepository medicationRepository;

    public OpenAIService(IDiseaseRepository diseaseRepository,
                         ITreatmentStepRepository stepRepository,
                         IMedicationRepository medicationRepository) {
        this.diseaseRepository = diseaseRepository;
        this.stepRepository = stepRepository;
        this.medicationRepository = medicationRepository;
    }

    // --- 1. Hàm kiểm tra các bệnh có thể gặp
    public List<String> checkPossibleDiseases(String status) throws Exception {
        List<String> diseaseList = diseaseRepository.findAll()
                .stream()
                .map(Disease::getName)
                .collect(Collectors.toList());

        // Prompt yêu cầu AI trả về danh sách các bệnh có khả năng phù hợp
        String prompt = "Bệnh nhân có triệu chứng: " + status +
                ". Danh sách bệnh có thể chọn: " + String.join(", ", diseaseList) +
                ". Hãy kiểm tra các triệu chứng này có thể thuộc những bệnh nào trong danh sách." +
                " Trả về JSON theo format: { \"possibleDiseases\": [\"<bệnh 1>\", \"<bệnh 2>\", ...] }." +
                " Nếu không có bệnh nào phù hợp, trả về mảng rỗng []";

        String jsonInput = "{"
                + "\"contents\": [{\"parts\":[{\"text\":\"" + prompt.replace("\"", "\\\"") + "\"}]}]"
                + "}";

        URL url = new URL(GEMINI_API_URL + geminiApiKey);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonInput.getBytes(StandardCharsets.UTF_8));
        }

        int statusCode = conn.getResponseCode();
        InputStream is = (statusCode >= 400) ? conn.getErrorStream() : conn.getInputStream();
        String response = (is != null) ? new String(is.readAllBytes(), StandardCharsets.UTF_8).trim() : "";

        if (statusCode >= 400) {
            throw new RuntimeException("Gemini API error: " + statusCode + " - " + response);
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response);
        List<String> possibleDiseases = new ArrayList<>();

        if (root.has("candidates") && root.get("candidates").isArray() && root.get("candidates").size() > 0) {
            String text = root.get("candidates").get(0)
                    .path("content").path("parts").get(0).path("text").asText().trim();

            if (text.startsWith("```")) {
                text = text.replaceAll("```json", "").replaceAll("```", "").trim();
            }

            try {
                // Parse JSON AI trả về
                JsonNode diseasesNode = mapper.readTree(text);
                if (diseasesNode.has("possibleDiseases") && diseasesNode.get("possibleDiseases").isArray()) {
                    for (JsonNode node : diseasesNode.get("possibleDiseases")) {
                        String diseaseName = node.asText().trim();
                        // Chỉ lấy bệnh tồn tại trong DB
                        if (diseaseList.contains(diseaseName)) {
                            possibleDiseases.add(diseaseName);
                        }
                    }
                }
            } catch (Exception e) {
                // Fallback: dò xem text trả về có chứa tên bệnh nào trong DB
                for (String disease : diseaseList) {
                    if (text.contains(disease)) {
                        possibleDiseases.add(disease);
                    }
                }
            }
        }

        return possibleDiseases;
    }



    // --- 2. Hàm lấy phác đồ điều trị theo tên bệnh
    public Map<String, Object> getTreatmentForDisease(String diagnosis) {
        List<TreatmentStep> stepsFromDB = stepRepository.findByDisease_Name(diagnosis);

        List<Map<String, Object>> steps = new ArrayList<>();
        int stepNumber = 1;
        for (TreatmentStep step : stepsFromDB) {
            List<Map<String, Object>> meds = medicationRepository.findByStep_Disease_Name(diagnosis)
                    .stream()
                    .map(m -> {
                        Map<String, Object> medMap = new HashMap<>();
                        medMap.put("name", m.getName());
                        medMap.put("dosage", m.getDosage());
                        medMap.put("usageInstructions", m.getUsageInstructions());
                        medMap.put("price", m.getPrice());
                        return medMap;
                    })
                    .collect(Collectors.toList());

            Map<String, Object> stepMap = new HashMap<>();
            stepMap.put("stepNumber", stepNumber++);
            stepMap.put("type", step.getStepType().getTypeName());
            stepMap.put("description", step.getDescription());
            stepMap.put("notes", step.getNotes());
            stepMap.put("medications", meds);

            steps.add(stepMap);
        }

        String treatmentTemplate = stepsFromDB.stream()
                .map(TreatmentStep::getDescription)
                .collect(Collectors.joining("; "));

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("diagnosis", diagnosis);
        resultMap.put("treatmentTemplate", treatmentTemplate);
        resultMap.put("steps", steps);

        return resultMap;
    }
}

