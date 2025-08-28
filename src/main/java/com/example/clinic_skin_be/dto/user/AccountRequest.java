package com.example.clinic_skin_be.dto.user;

import com.example.clinic_skin_be.model.manage_enum.AccountStatus;
import com.example.clinic_skin_be.model.manage_enum.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Data
public class AccountRequest {

    @NotBlank(message = "Họ và tên không được để trống")
    private String fullName;

    @Pattern(regexp = "^\\d{9,15}$", message = "Số điện thoại không hợp lệ, từ 9-15 chữ số")
    private String phoneNumber;

    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, message = "Mật khẩu phải từ 6 ký tự trở lên")
    private String password;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

    @Past(message = "Ngày sinh phải là ngày trong quá khứ")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;

    private Gender gender;

    private AccountStatus status;

    private MultipartFile avatarFile;

    @NotBlank(message = "Role không được để trống")
    private String role;
}
