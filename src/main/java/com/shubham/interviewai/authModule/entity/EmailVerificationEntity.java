package com.shubham.interviewai.authModule.entity;

import com.shubham.interviewai.authModule.enums.OtpPurpose;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table(name = "email_verification")
@Entity
public class EmailVerificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String email;
    private Integer otp;
    private LocalDateTime expiryTime;
    private Integer attempts;
    private boolean verified;
    @Enumerated(EnumType.STRING)
    private OtpPurpose purpose;

}
