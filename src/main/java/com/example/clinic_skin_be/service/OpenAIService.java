package com.example.clinic_skin_be.service;

import com.example.clinic_skin_be.model.Disease;
import com.example.clinic_skin_be.model.TreatmentStep;
import com.example.clinic_skin_be.model.Medication;
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

    @Value("${huggingface.api.key}")
    private String hfApiKey;

    private static final String HF_MODEL_URL =
            "https://api-inference.huggingface.co/models/facebook/bart-large-mnli";

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

        // --- Gọi Hugging Face API để xác định bệnh phù hợp nhất
        String prompt = "Bệnh nhân có triệu chứng: " + status +
                ". Kết quả khám: " + result +
                ". Chỉ chọn tên bệnh từ danh sách sau: " + String.join(", ", diseaseList) +
                ". Nếu không có bệnh nào phù hợp, trả về 'Không tồn tại'.";


        String jsonInput = "{"
                + "\"inputs\": \"" + prompt.replace("\"", "\\\"") + "\","
                + "\"parameters\": {\"candidate_labels\": [\"" + String.join("\",\"", diseaseList) + "\"]}"
                + "}";

        URL url = new URL(HF_MODEL_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + hfApiKey);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonInput.getBytes(StandardCharsets.UTF_8));
        }

        int statusCode = conn.getResponseCode();
        InputStream is = (statusCode >= 400) ? conn.getErrorStream() : conn.getInputStream();
        String response = new String(is.readAllBytes(), StandardCharsets.UTF_8).trim();

        if (statusCode >= 400) {
            throw new RuntimeException("Hugging Face API error: " + statusCode + " - " + response);
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response);
        String diagnosis = "Không xác định";
        if (root.has("labels") && root.get("labels").isArray() && root.get("labels").size() > 0) {
            diagnosis = root.get("labels").get(0).asText(); // lấy nhãn đầu tiên
        }

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

        // --- Tạo phác đồ tổng quát từ các step
        String treatmentTemplate = stepsFromDB.stream()
                .map(TreatmentStep::getDescription)
                .collect(Collectors.joining("; "));

        // --- Kết quả trả về
        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("diagnosis", diagnosis);
        resultMap.put("treatmentTemplate", treatmentTemplate);
        resultMap.put("steps", steps);

        return resultMap;
    }
}
