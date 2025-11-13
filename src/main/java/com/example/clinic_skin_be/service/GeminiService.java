package com.example.clinic_skin_be.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class GeminiService {

    @Value("${gemini.api.keys}")
    private String geminiApiKeysStr;

    private List<String> geminiApiKeys;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String GEMINI_API_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=";


    @PostConstruct
    private void init() {
        geminiApiKeys = Arrays.stream(geminiApiKeysStr.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        if (geminiApiKeys.isEmpty()) {
            throw new RuntimeException(" Không load được Gemini API keys! Kiểm tra application.properties hoặc biến môi trường.");
        }

        System.out.println("Loaded Gemini API keys: " + geminiApiKeys.size());
    }

    /**
     * Gọi API Gemini với cơ chế thử nhiều key
     */
    public String callGeminiApi(String prompt) throws Exception {
        String jsonInput = "{ \"contents\": [{\"parts\":[{\"text\":\""
                + prompt.replace("\"", "\\\"") + "\"}]}]}";

        Exception lastException = null;

        for (String key : geminiApiKeys) {
            try {
                URL url = new URL(GEMINI_API_URL + key);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(jsonInput.getBytes(StandardCharsets.UTF_8));
                }

                InputStream is = (conn.getResponseCode() >= 400) ? conn.getErrorStream() : conn.getInputStream();
                String response = (is != null) ? new String(is.readAllBytes(), StandardCharsets.UTF_8) : "";

                if (conn.getResponseCode() >= 400) {
                    System.err.println(" Key failed: " + key + ", HTTP code: " + conn.getResponseCode() + ", response: " + response);
                    continue; // thử key khác
                }

                if (!response.isEmpty()) {
                    System.out.println(" Gemini API success with key: " + key);
                    return response;
                }

            } catch (Exception e) {
                lastException = e;
                System.err.println(" Exception with key " + key + ": " + e.getMessage());
            }
        }

        throw lastException != null ? lastException : new RuntimeException("All Gemini API keys failed");
    }

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

    public Map<String, Object> getAISuggestions(String prompt, List<String> expectedFields) {
        try {
            String aiResponse = callGeminiApi(prompt);
            return parseAiResponse(aiResponse, expectedFields);
        } catch (Exception e) {
            System.err.println("❌ Gemini API call failed: " + e.getMessage());
            Map<String, Object> fallback = new HashMap<>();
            expectedFields.forEach(f -> fallback.put(f, Collections.emptyList()));
            return fallback;
        }
    }
}
