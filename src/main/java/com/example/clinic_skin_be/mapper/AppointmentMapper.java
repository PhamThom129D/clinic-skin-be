package com.example.clinic_skin_be.mapper;

import com.example.clinic_skin_be.dto.patient.AppointmentResponse;
import com.example.clinic_skin_be.dto.patient.PatientResponse;
import com.example.clinic_skin_be.dto.user.AccountResponse;
import com.example.clinic_skin_be.model.user.Appointment;
import com.example.clinic_skin_be.model.user.Account;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Component
public class AppointmentMapper {

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public AppointmentResponse toResponse(Appointment appointment) {
        if (appointment == null) return null;

        AppointmentResponse response = new AppointmentResponse();
        response.setId(appointment.getId());
        response.setAppointmentDate(appointment.getDate().format(dateFormatter));
        response.setAppointmentTime(appointment.getTime().format(timeFormatter));
        response.setNote(appointment.getNote());
        response.setStatus(appointment.getStatus());

        // Map patient
        PatientResponse patientResp = new PatientResponse();
        if (appointment.getPatient() != null) {
            patientResp.setId(appointment.getPatient().getId());
            patientResp.setPassportNumber(appointment.getPatient().getPassportNumber());
            patientResp.setOccupation(appointment.getPatient().getOccupation());

            // Map account
            Account account = appointment.getPatient().getAccount();
            if (account != null) {
                AccountResponse accountResp = new AccountResponse();
                accountResp.setId(account.getId());
                accountResp.setFullName(account.getFullName());
                accountResp.setEmail(account.getEmail());
                accountResp.setPhoneNumber(account.getPhoneNumber());
                accountResp.setGender(account.getGender() != null ? account.getGender().name() : null);
                accountResp.setAvtPath(account.getAvtPath());
                accountResp.setStatus(account.getStatus() != null ? account.getStatus().name() : null);

                // Map roles
                if (account.getRoles() != null) {
                    accountResp.setRoles(account.getRoles().stream()
                            .map(role -> role.getName())
                            .collect(Collectors.toSet()));
                }

                // Map createdAt / updatedAt
                if (account.getCreatedAt() != null)
                    accountResp.setCreatedAt(account.getCreatedAt().toString());
                if (account.getUpdatedAt() != null)
                    accountResp.setUpdatedAt(account.getUpdatedAt().toString());

                patientResp.setAccount(accountResp);
            }
        }

        response.setPatient(patientResp);
        return response;
    }
}
