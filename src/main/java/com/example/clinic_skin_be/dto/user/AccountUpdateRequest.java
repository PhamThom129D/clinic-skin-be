package com.example.clinic_skin_be.dto.user;

import com.example.clinic_skin_be.model.manage_enum.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Data
public class AccountUpdateRequest {
    // Không cần @NotBlank
    private String fullName;

    @Pattern(regexp = "^\\d{9,15}$", message = "Số điện thoại không hợp lệ, từ 9-15 chữ số")
    private String phoneNumber;

    @Email(message = "Email không hợp lệ")
    private String email;

    // Bỏ @NotBlank cho password và role
    private String password;

    // Không @NotBlank
    private String address;

    @Past(message = "Ngày sinh phải là ngày trong quá khứ")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;

    private Gender gender;

    // Bỏ cả status và role nếu bạn không cập nhật chúng
    // private AccountStatus status;
    // private String role;

    private MultipartFile avatarFile;
}