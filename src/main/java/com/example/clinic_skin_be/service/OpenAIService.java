package com.example.clinic_skin_be.service;

import com.example.clinic_skin_be.model.staff.doctor.Disease;
import com.example.clinic_skin_be.model.staff.doctor.TreatmentStep;
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

    @Value("${gemini.api.key}")  // key trong application.properties
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

    public Map<String, Object> getDiagnosisWithTreatment(String status, String result) throws Exception {
        // --- Lấy danh sách tên bệnh từ DB
        List<String> diseaseList = diseaseRepository.findAll()
                .stream()
                .map(Disease::getName)
                .collect(Collectors.toList());

        // --- Prompt ép Gemini chỉ trả về JSON diagnosis
        String prompt = "Bệnh nhân có triệu chứng: " + status +
                ". Kết quả khám: " + result +
                ". Danh sách bệnh có thể chọn: " + String.join(", ", diseaseList) +
                ". Hãy chọn bệnh chính xác nhất trong danh sách trên. " +
                "Nếu không có bệnh nào phù hợp, trả về 'Không tồn tại'. " +
                "Chỉ trả về JSON theo format: { \"diagnosis\": \"<tên bệnh giống y hệt trong danh sách hoặc 'Không tồn tại'>\" }";


        // --- JSON request cho Gemini
        String jsonInput = "{"
                + "\"contents\": [{\"parts\":[{\"text\":\"" + prompt.replace("\"", "\\\"") + "\"}]}]"
                + "}";

        // --- Gọi Gemini API
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
        String response = "";

        if (is != null) {
            response = new String(is.readAllBytes(), StandardCharsets.UTF_8).trim();
//            System.out.println("👉 Raw Gemini response: " + response);
        }

//
//        if (is != null) {
//            response = new String(is.readAllBytes(), StandardCharsets.UTF_8).trim();
//        }

        if (statusCode >= 400) {
            throw new RuntimeException("Gemini API error: " + statusCode + " - " + response);
        }
//        if (is != null) {
//            response = new String(is.readAllBytes(), StandardCharsets.UTF_8).trim();
//            System.out.println("👉 Raw Gemini response: " + response);
//        }
//

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response);
        String diagnosis = "Không tồn tại";

        if (root.has("candidates") && root.get("candidates").isArray() && root.get("candidates").size() > 0) {
            String text = root.get("candidates").get(0)
                    .path("content").path("parts").get(0).path("text").asText().trim();

            if (text.startsWith("```")) {
                text = text.replaceAll("```json", "")
                        .replaceAll("```", "")
                        .trim();
            }

            try {
                JsonNode diagNode = mapper.readTree(text);
                diagnosis = diagNode.path("diagnosis").asText("Không tồn tại");
            } catch (Exception e) {
                System.out.println("⚠️ Parse lỗi, text gốc: " + text);
                diagnosis = "Không tồn tại"; // fallback nếu parse lỗi
            }
        }



        // --- Lấy phác đồ từ DB theo bệnh chẩn đoán
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
            stepMap.put("type", step.getType());
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
