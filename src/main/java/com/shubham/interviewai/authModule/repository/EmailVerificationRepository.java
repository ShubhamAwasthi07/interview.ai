package com.shubham.interviewai.authModule.repository;

import com.shubham.interviewai.authModule.entity.EmailVerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerificationEntity , Integer> {

    EmailVerificationEntity findByEmail(String email);
    boolean existsByEmailAndVerifiedTrue(String email);



}
