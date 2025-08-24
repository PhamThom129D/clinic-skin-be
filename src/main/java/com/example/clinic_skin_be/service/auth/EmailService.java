package com.example.clinic_skin_be.service.auth;

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

    public void sendOtpEmail(String toEmail, String otpCode) {
        String subject = "Mã xác thực OTP từ ClinicSkin";

        String body = """
            <html>
            <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;">
                <div style="max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 10px; padding: 30px; box-shadow: 0 0 10px rgba(0,0,0,0.1);">
                    <h2 style="color: #2E86C1;">Xin chào!</h2>
                    <p>Bạn đã yêu cầu mã xác thực OTP từ <strong>HospitalCare</strong>.</p>
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
