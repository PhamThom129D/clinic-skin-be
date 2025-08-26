package com.example.clinic_skin_be.service.staff;

import com.example.clinic_skin_be.dto.staff.ConsultationAssignmentDTO;
import com.example.clinic_skin_be.dto.staff.ConsultationAssignmentResponseDTO;
import com.example.clinic_skin_be.exception.AssignmentAlreadyExistsException;
import com.example.clinic_skin_be.exception.ContactNotFoundException;
import com.example.clinic_skin_be.exception.ConsultantNotFoundException;
import com.example.clinic_skin_be.model.manage_enum.ConsultationStatus;
import com.example.clinic_skin_be.model.staff.Consultant;
import com.example.clinic_skin_be.model.staff.ConsultationAssignment;
import com.example.clinic_skin_be.model.staff.Contacts;
import com.example.clinic_skin_be.repository.staff.IConsultantRepository;
import com.example.clinic_skin_be.repository.staff.IConsultationAssignmentRepository;
import com.example.clinic_skin_be.repository.staff.IContactRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ContactService {
    private final IContactRepository contactRepository;
    private final IConsultantRepository consultantRepository;
    private final IConsultationAssignmentRepository assignmentRepository;

    public Contacts save(Contacts contact) {
        return contactRepository.save(contact);
    }

    public ConsultationAssignmentResponseDTO assignConsultant(ConsultationAssignmentDTO dto) {

        Contacts contact = contactRepository.findById(dto.getContactId())
                .orElseThrow(() -> new ContactNotFoundException("Contact not found with id: " + dto.getContactId()));

        Consultant consultant = consultantRepository.findById(dto.getConsultantId())
                .orElseThrow(() -> new ConsultantNotFoundException("Consultant not found with id: " + dto.getConsultantId()));

        Optional<ConsultationAssignment> existingAssignment = contact.getAssignments().stream()
                .filter(a -> a.getConsultant().getId().equals(consultant.getId()))
                .findFirst();

        ConsultationAssignment assignment;

        if (existingAssignment.isPresent()) {
            assignment = existingAssignment.get();
            if (dto.getNote() != null) {
                assignment.setNote(dto.getNote());
            }
            throw new AssignmentAlreadyExistsException("Consultant already assigned to this contact");
        } else {
            assignment = ConsultationAssignment.builder()
                    .contact(contact)
                    .consultant(consultant)
                    .note(dto.getNote())
                    .build();
            contact.getAssignments().add(assignment);
        }


        if (dto.getStatus() != null) {
            contact.setStatus(dto.getStatus());
        }

        contactRepository.save(contact);

        return toDTO(assignment);
    }

    public List<ConsultationAssignmentResponseDTO> getConsultationAssignments(ConsultationStatus status) {
        List<Contacts> contacts;

        if (status == null || status == ConsultationStatus.ALL) {
            contacts = contactRepository.findAll();
        } else {
            contacts = contactRepository.findByStatus(status);
        }

        List<ConsultationAssignmentResponseDTO> result = new ArrayList<>();

        for (Contacts c : contacts) {
            if (c.getAssignments().isEmpty()) {
                result.add(ConsultationAssignmentResponseDTO.builder()
                        .assignmentId(null)
                        .contactId(c.getId())
                        .contactName(c.getFullname())
                        .consultantId(null)
                        .consultantName(null)
                        .note(c.getNote())
                        .build());
            } else {
                for (ConsultationAssignment a : c.getAssignments()) {
                    result.add(ConsultationAssignmentResponseDTO.builder()
                            .assignmentId(a.getId())
                            .contactId(c.getId())
                            .contactName(c.getFullname())
                            .consultantId(a.getConsultant().getId())
                            .consultantName(a.getConsultant().getFullname())
                            .note(a.getNote())
                            .build());
                }
            }
        }

        return result;
    }


    public ConsultationAssignmentResponseDTO toDTO(ConsultationAssignment assignment) {
        return ConsultationAssignmentResponseDTO.builder()
                .assignmentId(assignment.getId())
                .contactId(assignment.getContact().getId())
                .contactName(assignment.getContact().getFullname())
                .consultantId(assignment.getConsultant().getId())
                .consultantName(assignment.getConsultant().getFullname())
                .note(assignment.getNote())
                .build();
    }
}
