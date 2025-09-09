
package com.example.clinic_skin_be.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GeminiService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private static final String GEMINI_API_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=";

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Gọi API Gemini và trả về raw JSON string
     */
    public String callGeminiApi(String prompt) throws Exception {
        String jsonInput = "{ \"contents\": [{\"parts\":[{\"text\":\"" + prompt.replace("\"", "\\\"") + "\"}]}]}";

        URL url = new URL(GEMINI_API_URL + geminiApiKey);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonInput.getBytes(StandardCharsets.UTF_8));
        }

        InputStream is = (conn.getResponseCode() >= 400) ? conn.getErrorStream() : conn.getInputStream();
        return (is != null) ? new String(is.readAllBytes(), StandardCharsets.UTF_8) : "";
    }

    /**
     * Parse JSON trả về từ AI, trả về map các field theo danh sách expectedFields
     */
    public Map<String, Object> parseAiResponse(String aiResponse, List<String> expectedFields) {
        Map<String, Object> result = new HashMap<>();
        try {
            JsonNode root = objectMapper.readTree(aiResponse);

            if (root.has("candidates") && root.get("candidates").isArray() && root.get("candidates").size() > 0) {
                String text = root.get("candidates").get(0)
                        .path("content").path("parts").get(0).path("text").asText();
                if (text.startsWith("```")) {
                    text = text.replaceAll("```json", "").replaceAll("```", "").trim();
                }

                try {
                    JsonNode jsonNode = objectMapper.readTree(text);
                    for (String field : expectedFields) {
                        if (jsonNode.has(field)) {
                            result.put(field, jsonNode.get(field));
                        } else {
                            result.put(field, Collections.emptyList());
                        }
                    }
                } catch (Exception e) {
                    expectedFields.forEach(f -> result.put(f, Collections.emptyList()));
                }
            } else {
                expectedFields.forEach(f -> result.put(f, Collections.emptyList()));
            }
        } catch (Exception e) {
            expectedFields.forEach(f -> result.put(f, Collections.emptyList()));
        }
        return result;
    }

    /**
     * Wrapper tiện lợi: gọi API + parse JSON
     */
    public Map<String, Object> getAISuggestions(String prompt, List<String> expectedFields) {
        try {
            String aiResponse = callGeminiApi(prompt);
            return parseAiResponse(aiResponse, expectedFields);
        } catch (Exception e) {
            Map<String, Object> fallback = new HashMap<>();
            expectedFields.forEach(f -> fallback.put(f, Collections.emptyList()));
            return fallback;
        }
    }
}
