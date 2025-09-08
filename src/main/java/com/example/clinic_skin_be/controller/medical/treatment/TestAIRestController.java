package com.example.clinic_skin_be.controller.medical.treatment;

import com.example.clinic_skin_be.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/test-ai")
public class TestAIRestController {

    @Autowired
    private TestService testService;

    /**
     * Gợi ý xét nghiệm + phác đồ dựa vào triệu chứng từ AI
     * GET /api/test-ai/suggest-treatment?sessionId=123
     */
    @GetMapping("/suggest-treatment")
    public ResponseEntity<?> suggestTreatment(@RequestParam Long sessionId) {
        try {
            // gọi service trả về Map chứa: possibleLabTests, possibleTreatments, aiNotes
            Map<String, Object> result = testService.suggestLabTestAndTreatment(sessionId);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Lỗi khi gọi Gemini AI: " + e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Lỗi hệ thống: " + e.getMessage()));
        }
    }
}
