package com.example.clinic_skin_be.dto.patient;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AppointmentRequest {

    @NotBlank(message = "Họ và tên không được để trống")
    private String fullName;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^\\d{9,15}$", message = "Số điện thoại không hợp lệ, từ 9-15 chữ số")
    private String phoneNumber;

    @NotBlank(message = "Số hộ chiếu không được để trống")
    private String passportNumber;

    @NotBlank(message = "Nghề nghiệp không được để trống")
    private String occupation;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

    @NotBlank(message = "Giới tính không được để trống")
    @Pattern(regexp = "Male|Female|Other", message = "Giới tính phải là Male, Female hoặc Other")
    private String gender;

    @NotBlank(message = "Ngày sinh không được để trống")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Ngày sinh phải có định dạng yyyy-MM-dd")
    private String dateOfBirth;

    @NotBlank(message = "Ngày hẹn không được để trống")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Ngày hẹn phải có định dạng yyyy-MM-dd")
    private String appointmentDate;

    @NotBlank(message = "Giờ hẹn không được để trống")
    @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "Giờ hẹn phải có định dạng HH:mm")
    private String appointmentTime;

    private String note;

    @NotNull(message = "Doctor ID không được để trống")
    @Positive(message = "Doctor ID phải là số dương")
    private Long doctorId;
}
