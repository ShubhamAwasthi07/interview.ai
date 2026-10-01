package com.shubham.interviewai.authModule.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {

    @NotBlank(message = "name is required!!")
    public String name;
    @NotBlank(message = "email is required!!")
    @Email(message = "Please provide the valid email address!!")
    public String email;
    @NotBlank(message = "password is required!!")
    @Size(min = 8 , max = 12 , message = "Password must be between 8 to 12 characters!!")
    public String password;
}
