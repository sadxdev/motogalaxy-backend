package com.bashverse.motogalaxybackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtConstant {

	public static String SECRET_KEY;
	public static final String JWT_HEADER = "Authorization";

	@Value("${JWT_SECRET_KEY}")
	public void setSecretKey(String secretKey) {
		SECRET_KEY = secretKey;
	}
}
