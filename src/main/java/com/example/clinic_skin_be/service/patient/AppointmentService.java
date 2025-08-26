package com.example.clinic_skin_be.service.patient;


import com.example.clinic_skin_be.dto.patient.AppointmentRequest;
import com.example.clinic_skin_be.model.patient.Patient;
import com.example.clinic_skin_be.model.staff.doctor.Doctor;
import com.example.clinic_skin_be.model.user.Account;
import com.example.clinic_skin_be.model.user.Appointment;

import com.example.clinic_skin_be.repository.staff.IDoctorRepository;
import com.example.clinic_skin_be.repository.patient.IPatientRepository;
import com.example.clinic_skin_be.repository.user.IAccountRepository;
import com.example.clinic_skin_be.repository.user.IAppointmentRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AppointmentService {

    private final IAccountRepository accountRepository;
    private final IPatientRepository patientRepository;
    private final IAppointmentRepository appointmentRepository;
    private final IDoctorRepository doctorRepository;


    @Transactional
    public Appointment registerAppointment(AppointmentRequest request) {
        Optional<Account> accountOpt = accountRepository.findByEmail(request.getEmail());

// Nếu chưa tìm thấy theo email thì kiểm tra theo số điện thoại
        if (accountOpt.isEmpty() && request.getPhoneNumber() != null) {
            accountOpt = accountRepository.findByPhoneNumber(request.getPhoneNumber());
        }

        Account account;
        Patient patient;

        if (accountOpt.isPresent()) {
            account = accountOpt.get();
            patient = patientRepository.findByAccount(account)
                    .orElseThrow(() -> new RuntimeException("Patient not found for account"));
        } else {
            // Tạo account mới
            account = Account.builder()
                    .fullName(request.getFullName())
                    .email(request.getEmail())
                    .phoneNumber(request.getPhoneNumber())
                    .address(request.getAddress())
                    .gender(request.getGender() != null ? Enum.valueOf(com.example.clinic_skin_be.model.manage_enum.Gender.class, request.getGender()) : null)
                    .dateOfBirth(request.getDateOfBirth() != null ? LocalDate.parse(request.getDateOfBirth()) : null)
                    .password("defaultPassword123") // bạn nên hash password
                    .build();
            accountRepository.save(account);

            // Tạo patient mới
            patient = Patient.builder()
                    .account(account)
                    .passportNumber(request.getPassportNumber())
                    .occupation(request.getOccupation())
                    .build();
            patientRepository.save(patient);
        }

        // 2. Tạo Appointment
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .date(request.getAppointmentDate())
                .time(request.getAppointmentTime())
                .note(request.getNote())
                .build();

        return appointmentRepository.save(appointment);
    }
}
