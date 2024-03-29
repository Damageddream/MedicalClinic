package com.damageddream.medicalclinic.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
@Component
public class TokenProvider implements Serializable {

    @Value("${app.security.jwt.secret:secret}")
    private String secret;

    UsernamePasswordAuthenticationToken getAuthentication(final String token,
                                                          final UserDetails userDetails) {
        final JwtParser
    }
}
