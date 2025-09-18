package com.bashverse.motogalaxybackend.repository;

import com.bashverse.motogalaxybackend.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long>{
    VerificationCode findByEmail(String email);
    VerificationCode findByOtp(String otp);
}
