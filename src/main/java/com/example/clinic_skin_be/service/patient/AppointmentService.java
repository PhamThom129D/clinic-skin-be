package com.example.clinic_skin_be.service.patient;

import com.example.clinic_skin_be.dto.patient.AppointmentRequest;
import com.example.clinic_skin_be.model.manage_enum.ConsultationStatus;
import com.example.clinic_skin_be.model.patient.Patient;
import com.example.clinic_skin_be.model.staff.doctor.Doctor;
import com.example.clinic_skin_be.model.user.Account;
import com.example.clinic_skin_be.model.user.Appointment;
import com.example.clinic_skin_be.model.manage_enum.Gender;
import com.example.clinic_skin_be.repository.staff.IDoctorRepository;
import com.example.clinic_skin_be.repository.patient.IPatientRepository;
import com.example.clinic_skin_be.repository.user.IAccountRepository;
import com.example.clinic_skin_be.repository.user.IAppointmentRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AppointmentService {

    private final IAccountRepository accountRepository;
    private final IPatientRepository patientRepository;
    private final IAppointmentRepository appointmentRepository;
    private final IDoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder; // để hash password

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    @Transactional
    public Appointment registerAppointment(AppointmentRequest request) {

        Account account = findOrCreateAccount(request);
        Patient patient = findOrCreatePatient(account, request);
        Doctor doctor = findDoctor(request.getDoctorId());

        Appointment appointment = buildAppointment(patient, doctor, request);

        return appointmentRepository.save(appointment);
    }

    private Account findOrCreateAccount(AppointmentRequest request) {
        Optional<Account> accountOpt = accountRepository.findByEmail(request.getEmail());

        if (accountOpt.isEmpty() && request.getPhoneNumber() != null) {
            accountOpt = accountRepository.findByPhoneNumber(request.getPhoneNumber());
        }

        if (accountOpt.isPresent()) {
            return accountOpt.get();
        }

        return createAccount(request);
    }

    private Account createAccount(AppointmentRequest request) {
        Account account = Account.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .gender(request.getGender() != null ? Gender.valueOf(request.getGender().toUpperCase()) : null)
                .dateOfBirth(request.getDateOfBirth() != null ? LocalDate.parse(request.getDateOfBirth(), dateFormatter) : null)
                .password(passwordEncoder.encode("defaultPassword123"))
                .build();

        return accountRepository.save(account);
    }

    private Patient findOrCreatePatient(Account account, AppointmentRequest request) {
        return patientRepository.findByAccount(account)
                .orElseGet(() -> createPatient(account, request));
    }

    private Patient createPatient(Account account, AppointmentRequest request) {
        Patient patient = Patient.builder()
                .account(account)
                .passportNumber(request.getPassportNumber())
                .occupation(request.getOccupation())
                .build();

        return patientRepository.save(patient);
    }

    private Doctor findDoctor(Long doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + doctorId));
    }

    private Appointment buildAppointment(Patient patient, Doctor doctor, AppointmentRequest request) {
        LocalDate appointmentDate = LocalDate.parse(request.getAppointmentDate(), dateFormatter);
        LocalTime appointmentTime = LocalTime.parse(request.getAppointmentTime(), timeFormatter);

        return Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .date(appointmentDate)
                .time(appointmentTime)
                .note(request.getNote())
                .status(ConsultationStatus.PENDING)
                .build();
    }
}
