package com.example.clinic_skin_be.controller.staff;

import com.example.clinic_skin_be.model.staff.cashier.Cashier;
import com.example.clinic_skin_be.model.staff.consultation.Consultant;
import com.example.clinic_skin_be.model.staff.doctor.Doctor;
import com.example.clinic_skin_be.model.staff.labStaff.LabStaff;
import com.example.clinic_skin_be.model.staff.receptionist.Receptionist;
import com.example.clinic_skin_be.service.staff.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
@RequiredArgsConstructor
public class StaffRestController {

    private final StaffService staffService;

    // ---------- Doctor ----------
    @GetMapping("/doctors")
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        return ResponseEntity.ok(staffService.getAllDoctors());
    }

    @GetMapping("/doctors/{id}")
    public ResponseEntity<Doctor> getDoctor(@PathVariable Long id) {
        return staffService.getDoctorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/doctors")
    public ResponseEntity<Doctor> createDoctor(@RequestBody Doctor doctor) {
        return ResponseEntity.ok(staffService.saveOrUpdateDoctor(doctor));
    }

    @PutMapping("/doctors/{id}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable Long id, @RequestBody Doctor doctor) {
        doctor.setId(id);
        return ResponseEntity.ok(staffService.saveOrUpdateDoctor(doctor));
    }

    @DeleteMapping("/doctors/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        staffService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }

    // ---------- Consultant ----------
    @GetMapping("/consultants")
    public ResponseEntity<List<Consultant>> getAllConsultants() {
        return ResponseEntity.ok(staffService.getAllConsultants());
    }

    @GetMapping("/consultants/{id}")
    public ResponseEntity<Consultant> getConsultant(@PathVariable Long id) {
        Consultant consultant = staffService.getConsultantById(id);
        return consultant != null ? ResponseEntity.ok(consultant) : ResponseEntity.notFound().build();
    }

    @PostMapping("/consultants")
    public ResponseEntity<Consultant> createConsultant(@RequestBody Consultant consultant) {
        return ResponseEntity.ok(staffService.saveOrUpdateConsultant(consultant));
    }

    @PutMapping("/consultants/{id}")
    public ResponseEntity<Consultant> updateConsultant(@PathVariable Long id, @RequestBody Consultant consultant) {
        consultant.setId(id);
        return ResponseEntity.ok(staffService.saveOrUpdateConsultant(consultant));
    }

    @DeleteMapping("/consultants/{id}")
    public ResponseEntity<Void> deleteConsultant(@PathVariable Long id) {
        staffService.deleteConsultant(id);
        return ResponseEntity.noContent().build();
    }

    // ---------- Cashier ----------
    @GetMapping("/cashiers")
    public ResponseEntity<List<Cashier>> getAllCashiers() {
        return ResponseEntity.ok(staffService.getAllCashiers());
    }

    @GetMapping("/cashiers/{id}")
    public ResponseEntity<Cashier> getCashier(@PathVariable Long id) {
        Cashier cashier = staffService.getCashierById(id);
        return cashier != null ? ResponseEntity.ok(cashier) : ResponseEntity.notFound().build();
    }

    @PostMapping("/cashiers")
    public ResponseEntity<Cashier> createCashier(@RequestBody Cashier cashier) {
        return ResponseEntity.ok(staffService.saveOrUpdateCashier(cashier));
    }

    @PutMapping("/cashiers/{id}")
    public ResponseEntity<Cashier> updateCashier(@PathVariable Long id, @RequestBody Cashier cashier) {
        cashier.setId(id);
        return ResponseEntity.ok(staffService.saveOrUpdateCashier(cashier));
    }

    @DeleteMapping("/cashiers/{id}")
    public ResponseEntity<Void> deleteCashier(@PathVariable Long id) {
        staffService.deleteCashier(id);
        return ResponseEntity.noContent().build();
    }

    // ---------- Receptionist ----------
    @GetMapping("/receptionists")
    public ResponseEntity<List<Receptionist>> getAllReceptionists() {
        return ResponseEntity.ok(staffService.getAllReceptionists());
    }

    @GetMapping("/receptionists/{id}")
    public ResponseEntity<Receptionist> getReceptionist(@PathVariable Long id) {
        Receptionist receptionist = staffService.getReceptionistById(id);
        return receptionist != null ? ResponseEntity.ok(receptionist) : ResponseEntity.notFound().build();
    }

    @PostMapping("/receptionists")
    public ResponseEntity<Receptionist> createReceptionist(@RequestBody Receptionist receptionist) {
        return ResponseEntity.ok(staffService.saveOrUpdateReceptionist(receptionist));
    }

    @PutMapping("/receptionists/{id}")
    public ResponseEntity<Receptionist> updateReceptionist(@PathVariable Long id, @RequestBody Receptionist receptionist) {
        receptionist.setId(id);
        return ResponseEntity.ok(staffService.saveOrUpdateReceptionist(receptionist));
    }

    @DeleteMapping("/receptionists/{id}")
    public ResponseEntity<Void> deleteReceptionist(@PathVariable Long id) {
        staffService.deleteReceptionist(id);
        return ResponseEntity.noContent().build();
    }

    // ---------- Lab Staff ----------
    @GetMapping("/lab-staffs")
    public ResponseEntity<List<LabStaff>> getAllLabStaffs() {
        return ResponseEntity.ok(staffService.getAllLabStaffs());
    }

    @GetMapping("/lab-staffs/{id}")
    public ResponseEntity<LabStaff> getLabStaff(@PathVariable Long id) {
        LabStaff labStaff = staffService.getLabStaffById(id);
        return labStaff != null ? ResponseEntity.ok(labStaff) : ResponseEntity.notFound().build();
    }

    @PostMapping("/lab-staffs")
    public ResponseEntity<LabStaff> createLabStaff(@RequestBody LabStaff labStaff) {
        return ResponseEntity.ok(staffService.saveOrUpdateLabStaff(labStaff));
    }

    @PutMapping("/lab-staffs/{id}")
    public ResponseEntity<LabStaff> updateLabStaff(@PathVariable Long id, @RequestBody LabStaff labStaff) {
        labStaff.setId(id);
        return ResponseEntity.ok(staffService.saveOrUpdateLabStaff(labStaff));
    }

    @DeleteMapping("/lab-staffs/{id}")
    public ResponseEntity<Void> deleteLabStaff(@PathVariable Long id) {
        staffService.deleteLabStaff(id);
        return ResponseEntity.noContent().build();
    }
}
