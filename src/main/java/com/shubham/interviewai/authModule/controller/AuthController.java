package com.shubham.interviewai.authModule.controller;

import com.shubham.interviewai.authModule.dto.*;
import com.shubham.interviewai.authModule.service.AuthService;
import com.shubham.interviewai.authModule.service.OtpService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private OtpService otpService;

    @Autowired
    AuthService authService;

    @PostMapping("/send-otp")
    public ResponseEntity<ResponseDto> handleSendOtpRequest(@RequestBody @Valid SendOtpDto sendOtpDto) {
        // calling the OTP Service
        ResponseDto response = otpService.sendOtp(sendOtpDto);
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ResponseDto> handleVerifyOtpRequest(@RequestBody @Valid VerifyOtpDto verifyOtpDto) {
        // calling the verify OTP service
        ResponseDto response = otpService.validateOtp(verifyOtpDto);
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> handleUserRegisterRequest(@RequestBody @Valid UserDto userDto) {
        // calling the auth service for registration
        ResponseDto response = authService.registerUser(userDto);
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> handleUserLoginRequest(@RequestBody @Valid LoginDto loginDto) {
        ResponseDto response = authService.loginUser(loginDto);
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }

}
