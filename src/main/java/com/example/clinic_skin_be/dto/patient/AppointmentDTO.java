package com.example.clinic_skin_be.dto.patient;

import com.example.clinic_skin_be.dto.ValidationGroups;
import com.example.clinic_skin_be.model.manage_enum.ConsultationStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AppointmentDTO {

    // ==== Dùng khi TẠO MỚI (Create) ====
    @NotBlank(message = "Họ và tên không được để trống", groups = ValidationGroups.Create.class)
    private String fullName;

    @NotBlank(message = "Email không được để trống", groups = ValidationGroups.Create.class)
    @Email(message = "Email không hợp lệ", groups = ValidationGroups.Create.class)
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống", groups = ValidationGroups.Create.class)
    @Pattern(regexp = "^\\d{9,15}$", message = "Số điện thoại không hợp lệ, từ 9-15 chữ số", groups = ValidationGroups.Create.class)
    private String phoneNumber;

    @NotBlank(message = "Số hộ chiếu không được để trống", groups = ValidationGroups.Create.class)
    private String passportNumber;

    @NotBlank(message = "Nghề nghiệp không được để trống", groups = ValidationGroups.Create.class)
    private String occupation;

    @NotBlank(message = "Địa chỉ không được để trống", groups = ValidationGroups.Create.class)
    private String address;

    @NotBlank(message = "Giới tính không được để trống", groups = ValidationGroups.Create.class)
    @Pattern(regexp = "MALE|FEMALE|OTHER", message = "Giới tính phải là MALE, FEMALE hoặc OTHER", groups = ValidationGroups.Create.class)
    private String gender;

    @NotBlank(message = "Ngày sinh không được để trống", groups = ValidationGroups.Create.class)
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Ngày sinh phải có định dạng yyyy-MM-dd", groups = ValidationGroups.Create.class)
    private String dateOfBirth;

    // ==== Dùng chung cho TẠO và CẬP NHẬT ====
    @NotBlank(message = "Ngày hẹn không được để trống", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Ngày hẹn phải có định dạng yyyy-MM-dd", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String appointmentDate;

    @NotBlank(message = "Giờ hẹn không được để trống", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "Giờ hẹn phải có định dạng HH:mm", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String appointmentTime;

    private String note;

    private ConsultationStatus status;
}
