package com.example.clinic_skin_be.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordResetRequest {
    @NotBlank (message = "Email không được để trống")
    @Email (message = "Email không hợp lệ")
    private String email;

    @NotBlank (message = "Mật khẩu mới không được để trống")
    @Size (min=6, message = "Mật khẩu mới phải chứa từ 6 ký tự trở lên")
    private String newPassword;
}
