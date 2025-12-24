package com.example.clinic_skin_be.controller.staff;

import com.example.clinic_skin_be.dto.user.AccountRequest;
import com.example.clinic_skin_be.model.staff.cashier.Cashier;
import com.example.clinic_skin_be.model.staff.consultation.Consultant;
import com.example.clinic_skin_be.model.staff.labStaff.LabStaff;
import com.example.clinic_skin_be.model.staff.receptionist.Receptionist;
import com.example.clinic_skin_be.service.staff.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/staff")
@RequiredArgsConstructor
public class StaffRestController {

    private final StaffService staffService;

    /** ================= CREATE ================= */
    @PostMapping("/consultants")
    public ResponseEntity<Consultant> createConsultant(@ModelAttribute AccountRequest request) throws IOException {
        return ResponseEntity.ok(staffService.createConsultant(request));
    }

    @PostMapping("/cashiers")
    public ResponseEntity<Cashier> createCashier(@ModelAttribute AccountRequest request) throws IOException {
        return ResponseEntity.ok(staffService.createCashier(request));
    }

    @PostMapping("/receptionists")
    public ResponseEntity<Receptionist> createReceptionist(@ModelAttribute AccountRequest request) throws IOException {
        return ResponseEntity.ok(staffService.createReceptionist(request));
    }

    @PostMapping("/lab-staffs")
    public ResponseEntity<LabStaff> createLabStaff(@ModelAttribute AccountRequest request) throws IOException {
        return ResponseEntity.ok(staffService.createLabStaff(request));
    }

    /** ================= READ ================= */

    @GetMapping("/consultants/{id}")
    public ResponseEntity<Consultant> getConsultantById(@PathVariable Long id) {
        return ResponseEntity.ok(staffService.getConsultantById(id));
    }

    @GetMapping("/cashiers/{id}")
    public ResponseEntity<Cashier> getCashierById(@PathVariable Long id) {
        return ResponseEntity.ok(staffService.getCashierById(id));
    }

    @GetMapping("/receptionists/{id}")
    public ResponseEntity<Receptionist> getReceptionistById(@PathVariable Long id) {
        return ResponseEntity.ok(staffService.getReceptionistById(id));
    }

    @GetMapping("/lab-staffs/{id}")
    public ResponseEntity<LabStaff> getLabStaffById(@PathVariable Long id) {
        return ResponseEntity.ok(staffService.getLabStaffById(id));
    }

    /** ================= UPDATE ================= */
    @PutMapping("/consultants/{id}")
    public ResponseEntity<Consultant> updateConsultant(@PathVariable Long id, @ModelAttribute AccountRequest request) throws IOException {
        return ResponseEntity.ok(staffService.updateConsultant(id, request));
    }

    @PutMapping("/cashiers/{id}")
    public ResponseEntity<Cashier> updateCashier(@PathVariable Long id, @ModelAttribute AccountRequest request) throws IOException {
        return ResponseEntity.ok(staffService.updateCashier(id, request));
    }

    @PutMapping("/receptionists/{id}")
    public ResponseEntity<Receptionist> updateReceptionist(@PathVariable Long id, @ModelAttribute AccountRequest request) throws IOException {
        return ResponseEntity.ok(staffService.updateReceptionist(id, request));
    }

    @PutMapping("/lab-staffs/{id}")
    public ResponseEntity<LabStaff> updateLabStaff(@PathVariable Long id, @ModelAttribute AccountRequest request) throws IOException {
        return ResponseEntity.ok(staffService.updateLabStaff(id, request));
    }

    /** ================= DELETE ================= */
    @DeleteMapping("/consultants/{id}")
    public ResponseEntity<Void> deleteConsultant(@PathVariable Long id) {
        staffService.deleteConsultant(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/cashiers/{id}")
    public ResponseEntity<Void> deleteCashier(@PathVariable Long id) {
        staffService.deleteCashier(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/receptionists/{id}")
    public ResponseEntity<Void> deleteReceptionist(@PathVariable Long id) {
        staffService.deleteReceptionist(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/lab-staffs/{id}")
    public ResponseEntity<Void> deleteLabStaff(@PathVariable Long id) {
        staffService.deleteLabStaff(id);
        return ResponseEntity.noContent().build();
    }
}
