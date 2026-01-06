package com.blogsite.blog_user_service.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtService Tests")
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private static final String SECRET_KEY = "mySecretKeyThatIsAtLeast256BitsLongForHS512Algorithm";
    private static final Long EXPIRATION = 3600000L; // 1 hour

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "secretKey", SECRET_KEY);
        ReflectionTestUtils.setField(jwtService, "expiration", EXPIRATION);
    }

    @Test
    @DisplayName("Should generate token successfully")
    void shouldGenerateTokenSuccessfully() {
        // When
        String token = jwtService.generateToken("test@example.com", 1L, "testuser");

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("Should extract email from token")
    void shouldExtractEmailFromToken() {
        // Given
        String token = jwtService.generateToken("test@example.com", 1L, "testuser");

        // When
        String email = jwtService.extractEmail(token);

        // Then
        assertEquals("test@example.com", email);
    }

    @Test
    @DisplayName("Should extract user ID from token")
    void shouldExtractUserIdFromToken() {
        // Given
        String token = jwtService.generateToken("test@example.com", 1L, "testuser");

        // When
        Long userId = jwtService.extractUserId(token);

        // Then
        assertEquals(1L, userId);
    }

    @Test
    @DisplayName("Should extract username from token")
    void shouldExtractUsernameFromToken() {
        // Given
        String token = jwtService.generateToken("test@example.com", 1L, "testuser");

        // When
        String username = jwtService.extractUsername(token);

        // Then
        assertEquals("testuser", username);
    }

    @Test
    @DisplayName("Should extract expiration from token")
    void shouldExtractExpirationFromToken() {
        // Given
        String token = jwtService.generateToken("test@example.com", 1L, "testuser");

        // When
        Date expiration = jwtService.extractExpiration(token);

        // Then
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    @DisplayName("Should return false for non-expired token")
    void shouldReturnFalseForNonExpiredToken() {
        // Given
        String token = jwtService.generateToken("test@example.com", 1L, "testuser");

        // When
        Boolean isExpired = jwtService.isTokenExpired(token);

        // Then
        assertFalse(isExpired);
    }

    @Test
    @DisplayName("Should validate token successfully")
    void shouldValidateTokenSuccessfully() {
        // Given
        String token = jwtService.generateToken("test@example.com", 1L, "testuser");

        // When
        Boolean isValid = jwtService.validateToken(token, "test@example.com");

        // Then
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should return false when email doesn't match")
    void shouldReturnFalseWhenEmailDoesntMatch() {
        // Given
        String token = jwtService.generateToken("test@example.com", 1L, "testuser");

        // When
        Boolean isValid = jwtService.validateToken(token, "other@example.com");

        // Then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should validate token without email check")
    void shouldValidateTokenWithoutEmailCheck() {
        // Given
        String token = jwtService.generateToken("test@example.com", 1L, "testuser");

        // When
        Boolean isValid = jwtService.validateToken(token);

        // Then
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should return false for invalid token")
    void shouldReturnFalseForInvalidToken() {
        // When
        Boolean isValid = jwtService.validateToken("invalid.token.here");

        // Then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should return expiration time")
    void shouldReturnExpirationTime() {
        // When
        Long expirationTime = jwtService.getExpirationTime();

        // Then
        assertEquals(EXPIRATION, expirationTime);
    }

    @Test
    @DisplayName("Should handle null token in validateToken")
    void shouldHandleNullToken() {
        // When/Then - validateToken should catch exceptions and return false
        Boolean isValid = jwtService.validateToken(null);
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should handle empty token string")
    void shouldHandleEmptyToken() {
        // When
        Boolean isValid = jwtService.validateToken("");

        // Then
        assertFalse(isValid);
    }
}
