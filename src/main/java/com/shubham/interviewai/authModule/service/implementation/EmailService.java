package com.shubham.interviewai.authModule.service.implementation;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public boolean sendOtpEmail(String toEmail, int otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Your OTP Verification Code");
            helper.setText(buildOtpEmailBody(otp), true);
            //mailSender.send(message);

            return true;
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to send OTP email: " + e.getMessage());
        }
    }

    private String buildOtpEmailBody(int otp) {
        return """
                <html>
                <body style="font-family: Arial, sans-serif; padding: 20px;">
                    <h2 style="color: #4CAF50;">OTP Verification</h2>
                    <p>Your One-Time Password (OTP) is:</p>
                    <h1 style="letter-spacing: 8px; color: #333;">%s</h1>
                    <p>This OTP is valid for <strong>1 minutes</strong>.</p>
                    <p style="color: red;">Do not share this OTP with anyone.</p>
                </body>
                </html>
                """.formatted(otp);
    }
}
