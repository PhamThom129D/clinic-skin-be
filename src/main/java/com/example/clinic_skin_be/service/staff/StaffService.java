package com.example.clinic_skin_be.service.staff;

import com.example.clinic_skin_be.model.staff.cashier.Cashier;
import com.example.clinic_skin_be.model.staff.doctor.Doctor;
import com.example.clinic_skin_be.model.staff.consultation.Consultant;
import com.example.clinic_skin_be.model.staff.labStaff.LabStaff;
import com.example.clinic_skin_be.model.staff.receptionist.Receptionist;
import com.example.clinic_skin_be.repository.staff.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StaffService {

    private final IDoctorRepository doctorRepo;
    private final IConsultantRepository consultantRepo;
    private final ICashierRepository cashierRepo;
    private final IReceptionistRepository receptionistRepo;
    private final ILabStaffRepository labStaffRepo;


    // list
    public List<Doctor> getAllDoctors() {
        return doctorRepo.findAll();
    }
    public List<Consultant> getAllConsultants() {
        return consultantRepo.findAll();
    }
    public List<Cashier> getAllCashiers() {
        return cashierRepo.findAll();
    }
    public List<Receptionist> getAllReceptionists() {
        return receptionistRepo.findAll();
    }
    public List<LabStaff> getAllLabStaffs() {
        return labStaffRepo.findAll();
    }

    //detail
    public Optional<Doctor> getDoctorById(Long id) {
        return doctorRepo.findById(id);
    }
    public Consultant getConsultantById(Long id) {
        return consultantRepo.findById(id).orElse(null);
    }
    public Cashier getCashierById(Long id) {
        return cashierRepo.findById(id).orElse(null);
    }
    public Receptionist getReceptionistById(Long id) {
        return receptionistRepo.findById(id).orElse(null);
    }
    public LabStaff getLabStaffById(Long id) {
        return labStaffRepo.findById(id).orElse(null);
    }

    // create or update
    public Doctor saveOrUpdateDoctor(Doctor doctor) {
        return doctorRepo.save(doctor);
    }
    public Consultant saveOrUpdateConsultant(Consultant consultant) {
        return consultantRepo.save(consultant);
    }
    public Cashier saveOrUpdateCashier(Cashier cashier) {
        return cashierRepo.save(cashier);
    }
    public Receptionist saveOrUpdateReceptionist(Receptionist receptionist) {
        return receptionistRepo.save(receptionist);
    }
    public LabStaff saveOrUpdateLabStaff(LabStaff labStaff) {
        return labStaffRepo.save(labStaff);
    }

    // delete
    public void deleteDoctor(Long id) {
        doctorRepo.deleteById(id);
    }
    public void deleteConsultant(Long id) {
        consultantRepo.deleteById(id);
    }
    public void deleteCashier(Long id) {
        cashierRepo.deleteById(id);
    }
    public void deleteReceptionist(Long id) {
        receptionistRepo.deleteById(id);
    }
    public void deleteLabStaff(Long id) {
        labStaffRepo.deleteById(id);
    }

}
