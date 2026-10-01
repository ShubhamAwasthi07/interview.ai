package com.shubham.interviewai.authModule.service;

import com.shubham.interviewai.authModule.dto.ResponseDto;
import com.shubham.interviewai.authModule.dto.SendOtpDto;
import com.shubham.interviewai.authModule.dto.VerifyOtpDto;

public interface OtpService {

    public ResponseDto sendOtp(SendOtpDto sendOtpDto);
    public ResponseDto validateOtp(VerifyOtpDto verifyOtpDto);
}
