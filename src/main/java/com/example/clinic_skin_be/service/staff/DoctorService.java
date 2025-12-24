package com.example.clinic_skin_be.service.staff;

import com.example.clinic_skin_be.dto.staff.CertificateDTO;
import com.example.clinic_skin_be.dto.staff.DoctorDTO;
import com.example.clinic_skin_be.dto.user.AccountRequest;
import com.example.clinic_skin_be.mapper.DoctorMapper;
import com.example.clinic_skin_be.model.staff.doctor.Certificate;
import com.example.clinic_skin_be.model.staff.doctor.Doctor;
import com.example.clinic_skin_be.model.user.Account;
import com.example.clinic_skin_be.model.user.Role;
import com.example.clinic_skin_be.repository.staff.ICertificateRepository;
import com.example.clinic_skin_be.repository.staff.IDoctorRepository;
import com.example.clinic_skin_be.repository.user.IAccountRepository;
import com.example.clinic_skin_be.repository.user.IRoleRepository;
import com.example.clinic_skin_be.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.beans.Encoder;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final IDoctorRepository doctorRepository;
    private final ICertificateRepository certificateRepository;
    private final DoctorMapper doctorMapper;
    private final CloudinaryService cloudinaryService;
    private final PasswordEncoder passwordEncoder;
    private final IAccountRepository accountRepository;
    private final IRoleRepository roleRepository;

    // ========================= CRUD Doctor =========================

    public DoctorDTO createDoctor(DoctorDTO dto, AccountRequest accountRequest) {
        // 1. Tạo Account từ AccountRequest
        Account account = new Account();
        account.setFullName(accountRequest.getFullName());
        account.setPhoneNumber(accountRequest.getPhoneNumber());
        account.setEmail(accountRequest.getEmail());
        account.setPassword(passwordEncoder.encode(accountRequest.getPassword())); // nhớ encode
        account.setAddress(accountRequest.getAddress());
        account.setDateOfBirth(accountRequest.getDateOfBirth());
        account.setGender(accountRequest.getGender());
        account.setStatus(accountRequest.getStatus());
        Role role = roleRepository.findByName(accountRequest.getRole())
                .orElseThrow(() -> new RuntimeException("Role not found: " + accountRequest.getRole()));

        account.getRoles().add(role);

        // Upload avatar nếu có
        if (accountRequest.getAvatarFile() != null && !accountRequest.getAvatarFile().isEmpty()) {
            Map uploadResult = cloudinaryService.uploadImage(accountRequest.getAvatarFile(), "avatars");
            account.setAvtPath((String) uploadResult.get("secure_url"));
        }

        Account savedAccount = accountRepository.save(account);

        // 2. Tạo Doctor
        Doctor doctor = doctorMapper.toEntity(dto, savedAccount, null);
        doctor.setAccount(savedAccount);

        Doctor savedDoctor = doctorRepository.save(doctor);

        // 3. Trả về DTO
        return doctorMapper.toDTO(savedDoctor);
    }


    public DoctorDTO updateDoctor(Long id, DoctorDTO dto) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        doctor.setSpecialty(dto.getSpecialty());
        doctor.setLevel(dto.getLevel());
        doctor.setAccount(accountRepository.findById(dto.getAccountId()).orElseThrow(() -> new RuntimeException("Account not found")));

        Doctor updated = doctorRepository.save(doctor);
        return doctorMapper.toDTO(updated);
    }

    public void deleteDoctor(Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new RuntimeException("Doctor not found");
        }
        doctorRepository.deleteById(id);
    }

    public List<DoctorDTO> getAllDoctorDTOs() {
        return doctorMapper.toDTOList(doctorRepository.findAll());
    }

    public DoctorDTO getDoctorDTOById(Long id) {
        return doctorRepository.findById(id)
                .map(doctorMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
    }

    // ========================= CRUD Certificate =========================

    public CertificateDTO addCertificateToDoctor(Long doctorId, CertificateDTO dto) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // Upload ảnh nếu có file
        if (dto.getFile() != null && !dto.getFile().isEmpty()) {
            Map uploadResult = cloudinaryService.uploadImage(dto.getFile(), "doctor_certificates");
            dto.setImageUrl((String) uploadResult.get("secure_url"));
        }

        Certificate cert = doctorMapper.toCertificateEntity(dto, doctor);
        Certificate saved = certificateRepository.save(cert);

        doctor.getCertificates().add(saved);
        doctorRepository.save(doctor);

        return doctorMapper.toCertificateDTO(saved);
    }

    public List<CertificateDTO> getCertificatesByDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        return doctor.getCertificates()
                .stream()
                .map(doctorMapper::toCertificateDTO)
                .collect(Collectors.toList());
    }

    public CertificateDTO updateCertificate(Long certId, CertificateDTO dto) {
        Certificate cert = certificateRepository.findById(certId)
                .orElseThrow(() -> new RuntimeException("Certificate not found"));

        cert.setName(dto.getName());
        cert.setIssuedBy(dto.getIssuedBy());
        cert.setIssuedDate(dto.getIssuedDate());
        cert.setExpiryDate(dto.getExpiryDate());
        cert.setCertificateNumber(dto.getCertificateNumber());
        cert.setDescription(dto.getDescription());

        if (dto.getFile() != null && !dto.getFile().isEmpty()) {
            Map uploadResult = cloudinaryService.uploadImage(dto.getFile(), "doctor_certificates");
            cert.setImageUrl((String) uploadResult.get("secure_url"));
        }

        Certificate updated = certificateRepository.save(cert);
        return doctorMapper.toCertificateDTO(updated);
    }

    public void deleteCertificate(Long certId) {
        if (!certificateRepository.existsById(certId)) {
            throw new RuntimeException("Certificate not found");
        }
        certificateRepository.deleteById(certId);
    }
}
