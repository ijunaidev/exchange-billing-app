package com.xische.exchangebilling.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private final String secret = "ThisIsASecretKeyThatIsAtLeast32BytesLong!";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(secret, 3600000);
    }

    @Test
    void testGenerateAndValidateToken() {
        UserDetails userDetails = User.builder()
                .username("admin")
                .password("password")
                .roles("USER")
                .build();

        String token = jwtUtil.generateToken(userDetails.getUsername());

        assertNotNull(token);
        assertEquals("admin", jwtUtil.extractUsername(token));
        assertTrue(jwtUtil.isTokenValid(token, userDetails));
    }

    @Test
    void testInvalidToken() {
        UserDetails userDetails = User.builder()
                .username("admin")
                .password("password")
                .roles("USER")
                .build();

        String token = jwtUtil.generateToken("otheruser");

        assertFalse(jwtUtil.isTokenValid(token, userDetails));
    }
}