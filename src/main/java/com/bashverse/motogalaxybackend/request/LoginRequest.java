package com.bashverse.motogalaxybackend.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginRequest {
    private String email;
    private String password;
    private String otp;
}
