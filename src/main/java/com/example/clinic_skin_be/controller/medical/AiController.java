package com.example.clinic_skin_be.controller.medical;

import com.example.clinic_skin_be.service.OpenAIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final OpenAIService openAIService;

    public AiController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @PostMapping("/check-disease")
    public ResponseEntity<?> checkDisease(@RequestBody Map<String, String> input) {
        String status = input.get("status");

        try {
            List<String> diseases = openAIService.checkPossibleDiseases(status);
            return ResponseEntity.ok(Map.of("possibleDiseases", diseases));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Lỗi khi kiểm tra bệnh",
                    "message", e.getMessage()
            ));
        }
    }


    @PostMapping("/treatment")
    public ResponseEntity<?> getTreatment(@RequestBody Map<String, String> input) {
        String diagnosis = input.get("diagnosis");

        try {
            Map<String, Object> treatment = openAIService.getTreatmentForDisease(diagnosis);
            return ResponseEntity.ok(treatment);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Lỗi khi lấy phác đồ điều trị",
                    "message", e.getMessage()
            ));
        }
    }
}
