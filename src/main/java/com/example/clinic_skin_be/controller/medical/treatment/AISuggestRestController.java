package com.example.clinic_skin_be.controller.medical.treatment;

import com.example.clinic_skin_be.service.ai.AISuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai-suggest")
public class AISuggestRestController {

    @Autowired
    private AISuggestionService aiSuggestionService;

    @PostMapping("/suggest-lab-test-and-treatment")
    public ResponseEntity<?> suggestTreatment(@RequestBody Map<String, String> request) {
        try {
            String symptoms = request.get("symptoms");

            Map<String, Object> result = aiSuggestionService.suggestLabTestAndTreatment(symptoms);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Lỗi hệ thống: " + e.getMessage()));
        }
    }

    @GetMapping("/suggest-visit-summary")
    public ResponseEntity<?> suggestVisitSummary(@RequestParam Long recordId) {
        try {
            Map<String, Object> result = aiSuggestionService.suggestVisitSummaryByRecord(recordId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Lỗi hệ thống: " + e.getMessage()));
        }
    }
}
