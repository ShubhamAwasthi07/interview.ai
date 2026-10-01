package com.shubham.interviewai.authModule.dto;

import com.shubham.interviewai.authModule.enums.OtpPurpose;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class SendOtpDto {

    @Email(message = "Please enter valid Email!!")
    public String email;
    public OtpPurpose purpose;
}
