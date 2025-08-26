package com.example.clinic_skin_be.controller.staff;

import com.example.clinic_skin_be.model.staff.Contacts;
import com.example.clinic_skin_be.service.staff.ContactService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
