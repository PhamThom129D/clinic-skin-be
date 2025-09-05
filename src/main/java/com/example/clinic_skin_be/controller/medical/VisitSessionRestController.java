package com.example.clinic_skin_be.controller.medical;

import com.example.clinic_skin_be.dto.medical.VisitSessionDTO;
import com.example.clinic_skin_be.model.medical.VisitSession;
import com.example.clinic_skin_be.service.medical.VisitSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visit-sessions")
@RequiredArgsConstructor
public class VisitSessionRestController {

    private final VisitSessionService visitSessionService;

    // danh sach phien
    @GetMapping("/record/{recordId}")
    public ResponseEntity<List<VisitSessionDTO>> getSessionsByRecord(@PathVariable Long recordId) {
        return ResponseEntity.ok(visitSessionService.getSessionsByRecord(recordId));
    }

    // Chi tiet phien
    @GetMapping("/{id}")
    public ResponseEntity<VisitSessionDTO> getSessionById(@PathVariable Long id) {
        VisitSessionDTO dto = visitSessionService.getSessionById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    // Them phien moi
    @PostMapping("/record/{recordId}")
    public ResponseEntity<VisitSessionDTO> createSession(
            @PathVariable Long recordId,
            @RequestBody VisitSessionDTO dto) {
        return ResponseEntity.ok(visitSessionService.saveVisitSession(recordId, dto));
    }

    // Cap nhat phien
    @PutMapping("/{id}/record/{recordId}")
    public ResponseEntity<VisitSessionDTO> updateSession(
            @PathVariable Long id,
            @PathVariable Long recordId,
            @RequestBody VisitSessionDTO dto) {
        dto.setSessionId(id);
        return ResponseEntity.ok(visitSessionService.saveVisitSession(recordId, dto));
    }


    // Xoá phiên khám
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        visitSessionService.deleteVisitSession(id);
        return ResponseEntity.noContent().build();
    }
}
