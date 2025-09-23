package com.example.clinic_skin_be.service.auth.impl;

import com.example.clinic_skin_be.model.user.Account;
import com.example.clinic_skin_be.model.user.Appointment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // --- Gửi OTP ---
    public void sendOtpEmail(String toEmail, String otpCode) {
        String subject = "Mã xác thực OTP từ ClinicSkin";
        String body = """
            <html>
            <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;">
                <div style="max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 10px; padding: 30px; box-shadow: 0 0 10px rgba(0,0,0,0.1);">
                    <h2 style="color: #2E86C1;">Xin chào!</h2>
                    <p>Bạn đã yêu cầu mã xác thực OTP từ <strong>ClinicSkin</strong>.</p>
                    <div style="text-align: center; margin: 30px 0;">
                        <span style="display: inline-block; font-size: 30px; font-weight: bold; color: #ffffff; background-color: #28a745; padding: 15px 25px; border-radius: 5px;">
                            %s
                        </span>
                    </div>
                    <p>Mã này sẽ hết hạn sau <strong>3 phút</strong>.</p>
                    <hr style="border: none; border-top: 1px solid #ddd; margin: 20px 0;">
                    <p>Trân trọng,<br><strong>Đội ngũ ClinicSkin</strong></p>
                </div>
            </body>
            </html>
        """.formatted(otpCode);

        sendEmail(toEmail, subject, body);
    }

    // --- Gửi thông báo đăng ký khám ---
    public void sendAppointmentEmail(Account account, Appointment appointment, boolean isNewAccount) {
        String subject = "Xác nhận đăng ký khám tại ClinicSkin";

        StringBuilder body = new StringBuilder();
        body.append("<html><body style='font-family: Arial, sans-serif;'>");
        body.append("<h2>Xin chào ").append(account.getFullName()).append("!</h2>");
        body.append("<p>Bạn đã đăng ký khám thành công tại <strong>ClinicSkin</strong>.</p>");
        body.append("<p>Thông tin lịch hẹn:</p>");
        body.append("<ul>");
        body.append("<li>Ngày: ").append(appointment.getDate()).append("</li>");
        body.append("<li>Giờ: ").append(appointment.getTime()).append("</li>");
        body.append("</ul>");

        if (isNewAccount) {
            body.append("<p>Chúng tôi đã tạo tài khoản cho bạn với mật khẩu mặc định: <strong>Abc@1234</strong></p>");
            body.append("<p>Vui lòng đăng nhập và đổi mật khẩu để bảo mật.</p>");
        }

        body.append("<p>Trân trọng,<br><strong>Đội ngũ ClinicSkin</strong></p>");
        body.append("</body></html>");

        sendEmail(account.getEmail(), subject, body.toString());
    }

    // --- Gửi email đăng ký thành công ---
    public void sendRegisterSuccessEmail(Account account) {
        String subject = "Chào mừng bạn đến với ClinicSkin!";
        String body = """
        <html>
        <body style="font-family: Arial, sans-serif;">
            <h2>Xin chào %s!</h2>
            <p>Bạn đã đăng ký tài khoản thành công tại <strong>ClinicSkin</strong>.</p>
            <p>Vui lòng <a href="http://localhost:3000/auth" target="_blank">click vào đây</a> để đăng nhập và thiết lập mật khẩu cho tài khoản của bạn.</p>
            <p>Chúc bạn có trải nghiệm tốt với ClinicSkin!</p>
            <p>Trân trọng,<br><strong>Đội ngũ ClinicSkin</strong></p>
        </body>
        </html>
    """.formatted(account.getFullName());

        sendEmail(account.getEmail(), subject, body);
    }


    // --- Gửi email đăng nhập thành công ---
    public void sendLoginSuccessEmail(Account account) {
        String subject = "Đăng nhập thành công tại ClinicSkin";
        String body = """
            <html>
            <body style="font-family: Arial, sans-serif;">
                <h2>Xin chào %s!</h2>
                <p>Bạn vừa đăng nhập thành công vào tài khoản <strong>ClinicSkin</strong>.</p>
                <p>Nếu không phải bạn, vui lòng thay đổi mật khẩu ngay lập tức để bảo mật.</p>
                <p>Chúc bạn có trải nghiệm tốt với ClinicSkin!</p>
                <p>Trân trọng,<br><strong>Đội ngũ ClinicSkin</strong></p>
            </body>
            </html>
        """.formatted(account.getFullName());

        sendEmail(account.getEmail(), subject, body);
    }

    // --- Helper gửi email thực sự ---
    private void sendEmail(String toEmail, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Gửi email thất bại: " + e.getMessage());
        }
    }
}
