package com.bashverse.motogalaxybackend.service.impl;

import com.bashverse.motogalaxybackend.config.JwtProvider;
import com.bashverse.motogalaxybackend.domain.USER_ROLE;
import com.bashverse.motogalaxybackend.exception.SellerException;
import com.bashverse.motogalaxybackend.exception.UserException;
import com.bashverse.motogalaxybackend.model.Cart;
import com.bashverse.motogalaxybackend.model.User;
import com.bashverse.motogalaxybackend.model.VerificationCode;
import com.bashverse.motogalaxybackend.repository.CartRepository;
import com.bashverse.motogalaxybackend.repository.UserRepository;
import com.bashverse.motogalaxybackend.repository.VerificationCodeRepository;
import com.bashverse.motogalaxybackend.request.LoginRequest;
import com.bashverse.motogalaxybackend.request.SignupRequest;
import com.bashverse.motogalaxybackend.response.AuthResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import com.bashverse.motogalaxybackend.service.AuthService;
import com.bashverse.motogalaxybackend.service.EmailService;
import com.bashverse.motogalaxybackend.service.UserService;
import com.bashverse.motogalaxybackend.utils.OtpUtils;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;

    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private final JwtProvider jwtProvider;
    private final CustomUserServiceImplementation customUserDetails;
    private final CartRepository cartRepository;

    @Override
    public void sendLoginOTP(String email) throws UserException, MessagingException {
        String SIGNING_PREFIX = "signing_";

        if(email.startsWith(SIGNING_PREFIX)){
            email = email.substring(SIGNING_PREFIX.length());
            userService.findUserByEmail(email);
        }

        VerificationCode isExist = verificationCodeRepository.findByEmail(email);

        if(isExist != null){
            verificationCodeRepository.delete(isExist);
        }

        String otp = OtpUtils.generateOtp();

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(email);
        verificationCodeRepository.save(verificationCode);

        String subject = "MotoGalaxy Login/Signup OTP";
        String text = "Your login OTP is - ";
        emailService.sendVerificationOtpEmail(email, otp, subject, text);
    }

    @Override
    public String createUser(SignupRequest req) throws SellerException {

        String email = req.getEmail();

        String fullName = req.getFullName();

        String otp = req.getOtp();

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(email);

        if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
            throw new SellerException("wrong otp...");
        }

        User user = userRepository.findByEmail(email);

        if (user == null) {

            User createdUser = new User();
            createdUser.setEmail(email);
            createdUser.setFullName(fullName);
            createdUser.setRole(USER_ROLE.ROLE_CUSTOMER);
            createdUser.setContact("9884114663");
            createdUser.setPassword(passwordEncoder.encode(otp));

            System.out.println(createdUser);

            user = userRepository.save(createdUser);

            Cart cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }


        List<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority(
                USER_ROLE.ROLE_CUSTOMER.toString()));


        Authentication authentication = new UsernamePasswordAuthenticationToken(
                email, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtProvider.generateToken(authentication);
    }

    @Override
    public AuthResponse signin(LoginRequest req) throws SellerException {
        String userName = req.getEmail();
        String otp = req.getOtp();

        System.out.println(userName + " ----- " + otp);

        Authentication authentication = authenticate(userName, otp);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();

        authResponse.setMessage("Login successful");
        authResponse.setJwt(token);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();


        String roleName = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();


        authResponse.setRole(USER_ROLE.valueOf(roleName));

        return authResponse;
    }

    private Authentication authenticate(String userName, String otp) throws SellerException{
        UserDetails userDetails = customUserDetails.loadUserByUsername(userName);

        System.out.println("sign in userDetails - " + userDetails);

        if (userDetails == null) {
            System.out.println("sign in userDetails - null ");
            throw new BadCredentialsException("Invalid username or password");
        }
        VerificationCode verificationCode = verificationCodeRepository.findByEmail(userName);

        if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
            throw new SellerException("wrong otp...");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}
