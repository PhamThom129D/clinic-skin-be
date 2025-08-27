package com.example.clinic_skin_be.controller.doctor;

import com.example.clinic_skin_be.repository.staff.IDoctorRepository;
import com.example.clinic_skin_be.service.staff.DoctorService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping ("/api/doctors")
@AllArgsConstructor
public class DoctorRestController {
    private final DoctorService doctorService;

    @GetMapping("/basic")
    public List<Map<String, Object>> getAllDoctors () {
        return doctorService.getAllDoctorsInfo();
    }
}
