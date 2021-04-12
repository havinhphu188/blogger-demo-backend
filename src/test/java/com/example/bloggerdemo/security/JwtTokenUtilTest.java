package com.example.bloggerdemo.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class JwtTokenUtilTest {
    private static final long ONE_MINUTE = 60000;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Test
    void testReturnFalseWhenJWTisExpired() {
        ReflectionTestUtils.setField(jwtTokenUtil, "jwtTokenValidity", -ONE_MINUTE);
        String token = jwtTokenUtil.generateToken(1);
        boolean isTokenValid = jwtTokenUtil.validateToken(token);
        assertFalse(isTokenValid);
    }
}