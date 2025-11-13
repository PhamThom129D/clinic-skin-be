package com.example.clinic_skin_be.service.patient;

import com.example.clinic_skin_be.dto.medical.VisitSessionDTO;
import com.example.clinic_skin_be.dto.patient.AppointmentDTO;
import com.example.clinic_skin_be.dto.patient.AppointmentResponse;
import com.example.clinic_skin_be.mapper.AppointmentMapper;
import com.example.clinic_skin_be.model.manage_enum.AccountStatus;
import com.example.clinic_skin_be.model.manage_enum.ConsultationStatus;
import com.example.clinic_skin_be.model.manage_enum.Gender;
import com.example.clinic_skin_be.model.patient.Patient;
import com.example.clinic_skin_be.model.user.Account;
import com.example.clinic_skin_be.model.user.Appointment;
import com.example.clinic_skin_be.model.user.Role;
import com.example.clinic_skin_be.repository.staff.IDoctorRepository;
import com.example.clinic_skin_be.repository.patient.IPatientRepository;
import com.example.clinic_skin_be.repository.user.IAccountRepository;
import com.example.clinic_skin_be.repository.staff.booking.IAppointmentRepository;
import com.example.clinic_skin_be.repository.user.IRoleRepository;
import com.example.clinic_skin_be.service.auth.impl.EmailService;
import com.example.clinic_skin_be.service.medical.VisitSessionService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AppointmentService {

    private final IAccountRepository accountRepository;
    private final IPatientRepository patientRepository;
    private final IAppointmentRepository appointmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final AppointmentMapper appointmentMapper;
    private final IRoleRepository roleRepository;
    private final VisitSessionService visitSessionService;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    /** Đăng ký lịch hẹn mới */
    @Transactional
    public AppointmentResponse registerAppointment(AppointmentDTO dto) {

        boolean isNewAccount = false;
        Account account = findOrCreateAccount(dto);
        if (account.getId() == null) isNewAccount = true;

        Patient patient = findOrCreatePatient(account, dto);

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .date(LocalDate.parse(dto.getAppointmentDate(), dateFormatter))
                .time(LocalTime.parse(dto.getAppointmentTime(), timeFormatter))
                .note(dto.getNote())
                .status(ConsultationStatus.PENDING)
                .build();

        Appointment saved = appointmentRepository.save(appointment);

        // Gửi email thông báo
        emailService.sendAppointmentEmail(account, saved, isNewAccount);

        return appointmentMapper.toResponse(saved);
    }

    /** Cập nhật lịch hẹn (chỉ date/time/note/status) */
    @Transactional
    public AppointmentResponse updateAppointment(Long id, AppointmentDTO dto) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (dto.getAppointmentDate() != null && !dto.getAppointmentDate().isBlank()) {
            appointment.setDate(LocalDate.parse(dto.getAppointmentDate(), dateFormatter));
        }
        if (dto.getAppointmentTime() != null && !dto.getAppointmentTime().isBlank()) {
            appointment.setTime(LocalTime.parse(dto.getAppointmentTime(), timeFormatter));
        }

        appointment.setNote(dto.getNote());

        if (dto.getStatus() != null) {
            appointment.setStatus(dto.getStatus());
        }

        Appointment updated = appointmentRepository.save(appointment);
        if(updated.getStatus() != ConsultationStatus.IN_PROGRESS) {
            Long recordId = updated.getId();
            VisitSessionDTO newSession = new VisitSessionDTO();
            newSession.setSessionDate(updated.getDate().atStartOfDay());
            newSession.setDiagnosis(updated.getNote());
            visitSessionService.createVisitSession(recordId,newSession);
        }
        return appointmentMapper.toResponse(updated);
    }

    public List<AppointmentResponse> getAppointmentsByDate(String date) {
        List<Appointment> appointments;

        if (date == null || date.isBlank()) {
            appointments = appointmentRepository.findAll();
        } else {
            LocalDate localDate = LocalDate.parse(date, dateFormatter);
            appointments = appointmentRepository.findByDate(localDate);
        }

        return appointments.stream()
                .map(appointmentMapper::toResponse)
                .collect(Collectors.toList());
    }


    /** Lấy lịch hẹn theo ID */
    public Optional<AppointmentResponse> getAppointmentById(Long id) {
        return appointmentRepository.findById(id).map(appointmentMapper::toResponse);
    }

    /** Xóa lịch hẹn */
    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

    private Account findOrCreateAccount(AppointmentDTO dto) {
        // Tìm account theo email trước
        Optional<Account> accountOpt = accountRepository.findByEmail(dto.getEmail());

        // Nếu chưa có, thử tìm theo số điện thoại
        if (accountOpt.isEmpty() && dto.getPhoneNumber() != null) {
            accountOpt = accountRepository.findByPhoneNumber(dto.getPhoneNumber());
        }

        // Nếu có rồi thì trả về luôn
        if (accountOpt.isPresent()) return accountOpt.get();

        // Tạo account mới
        Account account = Account.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .address(dto.getAddress())
                .gender(dto.getGender() != null ? Gender.valueOf(dto.getGender().toUpperCase()) : null)
                .dateOfBirth(dto.getDateOfBirth() != null ? LocalDate.parse(dto.getDateOfBirth(), DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null)
                .password(passwordEncoder.encode("Abc@1234"))
                .avtPath("https://i.pinimg.com/originals/7f/3f/3c/7f3f3c8b26d0d1a6a5f1a4a9e7f8b7c6.jpg")
                .status(AccountStatus.Active)
                .build();

        // Khởi tạo roles nếu null
        if (account.getRoles() == null) {
            account.setRoles(new HashSet<>());
        }

        Role patientRole = roleRepository.findByName("ROLE_PATIENT")
                .orElseThrow(() -> new RuntimeException("ROLE_PATIENT not found"));
        account.getRoles().add(patientRole);

        return accountRepository.saveAndFlush(account);
    }


    private Patient findOrCreatePatient(Account account, AppointmentDTO dto) {
        return patientRepository.findByAccount(account)
                .orElseGet(() -> {
                    Patient patient = Patient.builder()
                            .account(account)
                            .passportNumber(dto.getPassportNumber())
                            .occupation(dto.getOccupation())
                            .build();
                    return patientRepository.save(patient);
                });
    }
}
