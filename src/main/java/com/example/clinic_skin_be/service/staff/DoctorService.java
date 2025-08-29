package com.example.clinic_skin_be.service.staff;

import com.example.clinic_skin_be.repository.staff.IDoctorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class DoctorService {
    private final IDoctorRepository doctorRepository;

    public List<Map<String, Object>>  getAllDoctorsInfo() {
        return doctorRepository.findAllDoctorInfo();
    }
}
