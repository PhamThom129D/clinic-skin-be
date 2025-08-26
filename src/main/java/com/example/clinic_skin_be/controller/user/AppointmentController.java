package com.example.clinic_skin_be.controller.user;


import com.example.clinic_skin_be.dto.patient.AppointmentRequest;
import com.example.clinic_skin_be.model.user.Appointment;
import com.example.clinic_skin_be.service.patient.AppointmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointments")
@AllArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;


    @PostMapping("/register")
    public ResponseEntity<?> registerAppointment(@RequestBody AppointmentRequest request) {
        Appointment appointment = appointmentService.registerAppointment(request);
        return ResponseEntity.ok(appointment);
    }
}
