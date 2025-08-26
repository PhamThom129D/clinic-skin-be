package com.example.clinic_skin_be.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class OpenAIService {

    @Value("${huggingface.api.key}")
    private String hfApiKey;

    private static final String HF_MODEL_URL =
            "https://api-inference.huggingface.co/models/facebook/bart-large-mnli";

    private static final List<String> DISEASE_LIST = List.of(
            "Viêm da tiếp xúc dị ứng",
            "Viêm da cơ địa",
            "Mụn trứng cá viêm",
            "Nấm da",
            "Vảy nến",
            "Chàm",
            "Zona",
            "Herpes",
            "Hồng ban",
            "Mụn nước do vi rút"
    );

    private static final Map<String, String> TREATMENT_MAP = Map.of(
            "Viêm da tiếp xúc dị ứng", "Tránh tiếp xúc dị nguyên, bôi corticosteroid, giữ ẩm.",
            "Viêm da cơ địa", "Dùng kem dưỡng ẩm, corticosteroid bôi ngoài, kiểm soát ngứa.",
            "Mụn trứng cá viêm", "Rửa mặt nhẹ nhàng, thuốc bôi kháng sinh, retinoid, nếu nặng có thể dùng thuốc uống.",
            "Nấm da", "Dùng thuốc chống nấm bôi tại chỗ hoặc toàn thân nếu nặng.",
            "Vảy nến", "Dùng kem corticosteroid, dưỡng ẩm, liệu pháp ánh sáng nếu cần.",
            "Chàm", "Giữ ẩm thường xuyên, corticosteroid tại chỗ, tránh kích ứng da.",
            "Zona", "Thuốc kháng virus (acyclovir), giảm đau, chăm sóc da.",
            "Herpes", "Thuốc kháng virus (acyclovir), tránh tiếp xúc vùng nhiễm trùng.",
            "Hồng ban", "Theo dõi triệu chứng, dùng thuốc giảm ngứa hoặc kháng viêm nếu cần.",
            "Mụn nước do vi rút", "Giữ vệ sinh, thuốc kháng virus nếu cần, tránh chọc hoặc gãi."
    );

    public Map<String, String> getDiagnosisWithTreatment(String status, String result) throws Exception {
        // --- Step 1: tạo prompt
        String prompt = "Bệnh nhân có triệu chứng: " + status +
                ". Kết quả khám: " + result;

        // JSON cho zero-shot-classification
        String jsonInput = "{"
                + "\"inputs\": \"" + prompt.replace("\"", "\\\"") + "\","
                + "\"parameters\": {"
                + "\"candidate_labels\": [\"" + String.join("\",\"", DISEASE_LIST) + "\"]"
                + "}"
                + "}";

        // --- Step 2: gọi API Hugging Face
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
            throw new RuntimeException("Hugging Face API error: HTTP " + statusCode + " - " + response);
        }

        // --- Step 3: parse JSON response
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response);

        // Lấy nhãn có score cao nhất
        String diagnosis = "Không xác định";
        if (root.has("labels") && root.get("labels").isArray() && root.get("labels").size() > 0) {
            diagnosis = root.get("labels").get(0).asText();
        }

        String treatment = TREATMENT_MAP.getOrDefault(diagnosis, "Không có phác đồ sẵn có");

        return Map.of("diagnosis", diagnosis, "treatment", treatment);
    }
}
