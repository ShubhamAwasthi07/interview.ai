package com.shubham.interviewai.authModule.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDto {
    @Email(message = "Email is required")
    public String email;
    @NotBlank(message = "Password is required")
    public String password;
}
