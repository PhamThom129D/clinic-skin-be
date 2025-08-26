package com.example.clinic_skin_be.controller;

import com.example.clinic_skin_be.service.OpenAIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final OpenAIService openAIService;

    public AiController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @PostMapping("/diagnose")
    public ResponseEntity<?> diagnose(@RequestBody Map<String, String> input) {
        String status = input.get("status");
        String result = input.get("result");

        try {
            Map<String, String> response = openAIService.getDiagnosisWithTreatment(status, result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Lỗi khi gọi AI",
                    "message", e.getMessage()
            ));
        }
    }
}
