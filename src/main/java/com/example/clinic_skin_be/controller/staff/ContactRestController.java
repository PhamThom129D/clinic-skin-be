package com.example.clinic_skin_be.controller.staff;

import com.example.clinic_skin_be.dto.staff.ConsultationAssignmentDTO;
import com.example.clinic_skin_be.dto.staff.ConsultationAssignmentResponseDTO;
import com.example.clinic_skin_be.model.manage_enum.ConsultationStatus;
import com.example.clinic_skin_be.model.staff.consultation.Contacts;
import com.example.clinic_skin_be.service.staff.ContactService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class ContactRestController {

    private final ContactService contactService;

    @PostMapping
    public ResponseEntity<Contacts> createContact(@RequestBody Contacts contact) {
        Contacts savedContact = contactService.save(contact);
        return ResponseEntity.ok(savedContact);
    }

    @PutMapping("/consultation-assignment")
    public ResponseEntity<ConsultationAssignmentResponseDTO> updateStatusAndNote(
            @RequestBody ConsultationAssignmentDTO dto) {

        ConsultationAssignmentResponseDTO updated = contactService.assignConsultant(dto);

        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/list-assignments")
    public ResponseEntity<List<ConsultationAssignmentResponseDTO>> getAssignments(
            @RequestParam(required = false) ConsultationStatus status) {

        List<ConsultationAssignmentResponseDTO> assignments = contactService.getConsultationAssignments(status);
        return ResponseEntity.ok(assignments);
    }

}
