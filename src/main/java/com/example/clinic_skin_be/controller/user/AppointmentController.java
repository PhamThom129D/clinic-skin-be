package com.example.clinic_skin_be.controller.user;

import com.example.clinic_skin_be.dto.ValidationGroups;
import com.example.clinic_skin_be.dto.patient.AppointmentDTO;
import com.example.clinic_skin_be.dto.patient.AppointmentResponse;
import com.example.clinic_skin_be.service.patient.AppointmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@AllArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    // ================= POST: đăng ký lịch hẹn =================
    @PostMapping("/register")
    public ResponseEntity<AppointmentResponse> registerAppointment(
            @Validated(ValidationGroups.Create.class) @RequestBody AppointmentDTO dto) {
        AppointmentResponse response = appointmentService.registerAppointment(dto);
        return ResponseEntity.ok(response);
    }

    // ================= PUT: cập nhật lịch hẹn =================
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponse> updateAppointment(
            @PathVariable Long id,
            @Validated(ValidationGroups.Update.class) @RequestBody AppointmentDTO dto) {
        AppointmentResponse response = appointmentService.updateAppointment(id, dto);
        return ResponseEntity.ok(response);
    }

    // ================= GET: lấy danh sách (tất cả hoặc theo ngày) =================
    @GetMapping
    public ResponseEntity<List<AppointmentResponse>> getAppointments(
            @RequestParam(value = "date", required = false) String date) {
        List<AppointmentResponse> list = appointmentService.getAppointmentsByDate(date);
        return ResponseEntity.ok(list);
    }


    // ================= GET: lấy theo ID =================
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> getAppointmentById(@PathVariable Long id) {
        return appointmentService.getAppointmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= DELETE: xóa lịch hẹn =================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build(); // HTTP 204
    }
}
