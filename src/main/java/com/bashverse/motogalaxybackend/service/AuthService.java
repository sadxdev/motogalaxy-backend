package com.bashverse.motogalaxybackend.service;

import com.bashverse.motogalaxybackend.exception.SellerException;
import com.bashverse.motogalaxybackend.exception.UserException;
import com.bashverse.motogalaxybackend.request.LoginRequest;
import com.bashverse.motogalaxybackend.request.SignupRequest;
import com.bashverse.motogalaxybackend.response.AuthResponse;
import jakarta.mail.MessagingException;

public interface AuthService {
    void sendLoginOTP(String email) throws UserException, MessagingException;
    String createUser(SignupRequest req) throws SellerException;
    AuthResponse signin(LoginRequest req) throws SellerException;
}
