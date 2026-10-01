package com.shubham.interviewai.authModule.service.implementation;

import com.shubham.interviewai.authModule.constant.Constants;
import com.shubham.interviewai.authModule.dto.LoginDto;
import com.shubham.interviewai.authModule.dto.LoginResponseDto;
import com.shubham.interviewai.authModule.dto.ResponseDto;
import com.shubham.interviewai.authModule.dto.UserDto;
import com.shubham.interviewai.authModule.entity.UserEntity;
import com.shubham.interviewai.authModule.mapper.UserMapper;
import com.shubham.interviewai.authModule.repository.EmailVerificationRepository;
import com.shubham.interviewai.authModule.repository.UserRepository;
import com.shubham.interviewai.authModule.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    @Value("${jwt.expiration}")
    private String expirationTime;

    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(UserRepository userRepository,
                           EmailVerificationRepository emailVerificationRepository,
                           PasswordEncoder passwordEncoder,
                           JwtService jwtService) {
        this.userRepository = userRepository;
        this.emailVerificationRepository = emailVerificationRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public ResponseDto registerUser(UserDto userDto) {

        ResponseDto responseDto = new ResponseDto();
        String email = userDto.getEmail();

        // check the email is verified or not!!
        boolean isVerified = emailVerificationRepository.existsByEmailAndVerifiedTrue(email);

        if(!isVerified) {
            responseDto.setStatus(Constants.ResponseStatus.ERROR);
            responseDto.setMessage("Please verify your email before registration.");
            responseDto.setResponseCode(HttpStatus.FORBIDDEN.value());

            return responseDto;
        }

        //Check if user if already exist or not
        boolean isUserExist = userRepository.existsByEmail(email);
        if(isUserExist) {
            responseDto.setStatus(Constants.ResponseStatus.ERROR);
            responseDto.setMessage("Email already exists. Please login using your email and password.");
            responseDto.setResponseCode(HttpStatus.CONFLICT.value());
        }
        else {
            UserEntity userEntity = UserMapper.toEntity(userDto);
            userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userRepository.save(userEntity);

            responseDto.setStatus(Constants.ResponseStatus.SUCCESS);
            responseDto.setMessage("Registration successful.");
            responseDto.setResponseCode(HttpStatus.CREATED.value());
        }
        return responseDto;
    }

    @Override
    public ResponseDto loginUser(LoginDto loginDto) {
        ResponseDto responseDto = new ResponseDto();

        String email = loginDto.getEmail();

        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(email);
        if(userEntityOptional.isEmpty()) {
            responseDto.setStatus(Constants.ResponseStatus.ERROR);
            responseDto.setMessage("Invalid email or password.");
            responseDto.setResponseCode(HttpStatus.UNAUTHORIZED.value());
            return responseDto;
        }

        boolean isVerified = emailVerificationRepository.existsByEmailAndVerifiedTrue(email);
        if(!isVerified) {
            responseDto.setStatus(Constants.ResponseStatus.ERROR);
            responseDto.setMessage("Please verify your email before login.");
            responseDto.setResponseCode(HttpStatus.FORBIDDEN.value());
            return responseDto;
        }

        UserEntity userEntity = userEntityOptional.get();
        if(!passwordEncoder.matches(loginDto.getPassword(), userEntity.getPassword())) {
            responseDto.setStatus(Constants.ResponseStatus.ERROR);
            responseDto.setMessage("Invalid email or password.");
            responseDto.setResponseCode(HttpStatus.UNAUTHORIZED.value());
            return responseDto;
        }

        String token = jwtService.generateToken(userEntity);
        responseDto.setStatus(Constants.ResponseStatus.SUCCESS);
        responseDto.setData(new LoginResponseDto(token , "bearer" ,Long.parseLong(expirationTime)));
        responseDto.setMessage("Login successful.");
        responseDto.setResponseCode(HttpStatus.OK.value());

        return responseDto;
    }
}
