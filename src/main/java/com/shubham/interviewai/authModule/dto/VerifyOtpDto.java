package com.shubham.interviewai.authModule.dto;

import com.shubham.interviewai.authModule.enums.OtpPurpose;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VerifyOtpDto {

    @Email(message = "Please enter valid Email!!")
    @NotEmpty(message = "Email is required!!")
    public String email;
    public OtpPurpose purpose;
    @NotNull(message = "Otp is required!!")
    public Integer otp;
}
