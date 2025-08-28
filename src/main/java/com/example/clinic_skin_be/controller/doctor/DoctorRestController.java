package com.example.clinic_skin_be.controller.doctor;

import com.example.clinic_skin_be.dto.staff.DoctorInfoDTO;
import com.example.clinic_skin_be.model.staff.doctor.Doctor;
import com.example.clinic_skin_be.repository.staff.IDoctorRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController
@RequestMapping ("/api/doctors")
@AllArgsConstructor
public class DoctorRestController {
    @Autowired
    private IDoctorRepository doctorRepository;

    @GetMapping("/basic")
    public List<DoctorInfoDTO> getAllDoctors () {
        List<Doctor> doctors = doctorRepository.findAll();
        return doctors.stream().map(doctor -> new DoctorInfoDTO(
               doctor.getId(),
                doctor.getAccount().getFullName(),
                doctor.getSpecialty(),
                doctor.getAccount().getAvtPath()
        )).collect(Collectors.toList());
    }
}
