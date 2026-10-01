package com.shubham.interviewai.authModule.service;

import com.shubham.interviewai.authModule.dto.LoginDto;
import com.shubham.interviewai.authModule.dto.ResponseDto;
import com.shubham.interviewai.authModule.dto.UserDto;

public interface AuthService {

    public ResponseDto registerUser(UserDto userDto);
    public ResponseDto loginUser(LoginDto loginDto);
}
