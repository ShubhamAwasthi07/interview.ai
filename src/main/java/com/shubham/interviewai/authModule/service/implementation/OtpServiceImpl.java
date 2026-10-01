package com.shubham.interviewai.authModule.service.implementation;

import com.shubham.interviewai.authModule.dto.ResponseDto;
import com.shubham.interviewai.authModule.dto.SendOtpDto;
import com.shubham.interviewai.authModule.dto.VerifyOtpDto;
import com.shubham.interviewai.authModule.entity.EmailVerificationEntity;
import com.shubham.interviewai.authModule.enums.OtpPurpose;
import com.shubham.interviewai.authModule.repository.EmailVerificationRepository;
import com.shubham.interviewai.authModule.service.OtpService;
import com.shubham.interviewai.authModule.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@Slf4j
public class OtpServiceImpl implements OtpService {

    private final EmailService emailService;
    private final EmailVerificationRepository emailVerificationRepository;

    public OtpServiceImpl(EmailService emailService, EmailVerificationRepository emailVerificationRepository) {
        this.emailService = emailService;
        this.emailVerificationRepository = emailVerificationRepository;
    }

    private static final SecureRandom random = new SecureRandom();

    @Override
    public ResponseDto sendOtp(SendOtpDto sendOtpDto) {
        ResponseDto responseDto = new ResponseDto();
        try {
            String email = sendOtpDto.getEmail();
            int otp = 111111; //OtpUtil.generateOtp();

            //Calling the Email Service to Send otp
            boolean response = emailService.sendOtpEmail(email, otp);

            if (response) {
                EmailVerificationEntity emailVerificationEntity;

                // check the email is already exist or not
                emailVerificationEntity = emailVerificationRepository.findByEmail(email);

                if (emailVerificationEntity != null) {
                    emailVerificationEntity.setOtp(otp);
                    LocalDateTime expiryTime = LocalDateTime.now().plusSeconds(120);
                    emailVerificationEntity.setExpiryTime(expiryTime);
                    emailVerificationEntity.setVerified(false);
                    emailVerificationEntity.setPurpose(sendOtpDto.getPurpose());
                    emailVerificationEntity.setAttempts(emailVerificationEntity.getAttempts() + 1);

                } else {
                    emailVerificationEntity = new EmailVerificationEntity();
                    LocalDateTime expiryTime = LocalDateTime.now().plusSeconds(120);
                    emailVerificationEntity.setEmail(email);
                    emailVerificationEntity.setOtp(otp);
                    emailVerificationEntity.setPurpose(sendOtpDto.getPurpose());
                    emailVerificationEntity.setAttempts(1);
                    emailVerificationEntity.setVerified(false);
                    emailVerificationEntity.setExpiryTime(expiryTime);
                }

                emailVerificationRepository.save(emailVerificationEntity);
            }

            responseDto.setStatus(Constants.ResponseStatus.SUCCESS);
            responseDto.setMessage(Constants.ResponseMessage.OTP_SEND_SUCCESS);
            responseDto.setResponseCode(HttpStatus.OK.value());

        } catch (Exception e) {
            log.error("Error while sending the otp {}:", e.getMessage());
            responseDto.setStatus(Constants.ResponseStatus.ERROR);
            responseDto.setMessage(e.getMessage());
            responseDto.setResponseCode(HttpStatus.BAD_REQUEST.value());
        }

        return responseDto;
    }

    @Override
    public ResponseDto validateOtp(VerifyOtpDto verifyOtpDto) {
        ResponseDto responseDto = new ResponseDto();

        try {
            String email = verifyOtpDto.getEmail();
            Integer otp = verifyOtpDto.getOtp();
            OtpPurpose otpPurpose = verifyOtpDto.getPurpose();


            EmailVerificationEntity emailVerificationEntity = emailVerificationRepository.findByEmail(email);

            if (emailVerificationEntity == null) {
                responseDto.setStatus(Constants.ResponseStatus.ERROR);
                responseDto.setMessage("Please request OTP first!!");
                responseDto.setResponseCode(HttpStatus.BAD_REQUEST.value());
                return responseDto;
            }

            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime expiryTime = emailVerificationEntity.getExpiryTime();

            if (expiryTime == null || currentTime.isAfter(expiryTime)) {
                responseDto.setStatus(Constants.ResponseStatus.ERROR);
                responseDto.setMessage("OTP has expired!!");
                responseDto.setResponseCode(HttpStatus.BAD_REQUEST.value());
                return responseDto;
            }

            if (emailVerificationEntity.getOtp() != null && emailVerificationEntity.getOtp().equals(otp) && emailVerificationEntity.getPurpose().equals(otpPurpose)) {
                emailVerificationEntity.setVerified(true);
                emailVerificationEntity.setOtp(null);
                emailVerificationRepository.save(emailVerificationEntity);

                responseDto.setStatus(Constants.ResponseStatus.SUCCESS);
                responseDto.setMessage(Constants.ResponseMessage.OTP_VERIFY_SUCCESS);
                responseDto.setResponseCode(HttpStatus.OK.value());
            }
            else {
                responseDto.setStatus(Constants.ResponseStatus.ERROR);
                responseDto.setMessage("Invalid Otp!!");
                responseDto.setResponseCode(HttpStatus.BAD_REQUEST.value());
            }

            return responseDto;

        } catch (Exception e) {
            log.error("Error while validating the otp {}:", e.getMessage());
            responseDto.setStatus(Constants.ResponseStatus.ERROR);
            responseDto.setMessage(e.getMessage());
            responseDto.setResponseCode(HttpStatus.BAD_REQUEST.value());
        }

        return responseDto;
    }

}
