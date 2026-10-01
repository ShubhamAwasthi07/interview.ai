package com.shubham.interviewai.authModule.dto;

import lombok.Data;

@Data
public class LoginResponseDto {

    private String token;
    private String tokenType;
    private Long expiresIn;


    public LoginResponseDto(String token, String tokenType, Long expiresIn) {
        this.token = token;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }
}
