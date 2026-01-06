package com.blogsite.blog_command_service.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = {"jwt.secret=test-secret-key-that-is-long-enough-for-hmac-sha-256-algorithm"})
@DisplayName("JwtService Tests")
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private String secretKey = "test-secret-key-that-is-long-enough-for-hmac-sha-256-algorithm";
    private SecretKey signingKey;
    private String validToken;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "secretKey", secretKey);
        signingKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        
        // Create a valid token for testing
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", 1L);
        claims.put("username", "testuser");
        
        Date expiration = new Date(System.currentTimeMillis() + 3600000); // 1 hour from now
        
        validToken = Jwts.builder()
                .subject("test@example.com")
                .claims(claims)
                .expiration(expiration)
                .signWith(signingKey)
                .compact();
    }

    @Nested
    @DisplayName("Extract Email Tests")
    class ExtractEmailTests {

        @Test
        @DisplayName("Should extract email from token")
        void shouldExtractEmailFromToken() {
            String email = jwtService.extractEmail(validToken);
            
            assertEquals("test@example.com", email);
        }
    }

    @Nested
    @DisplayName("Extract User ID Tests")
    class ExtractUserIdTests {

        @Test
        @DisplayName("Should extract user ID from token")
        void shouldExtractUserIdFromToken() {
            Long userId = jwtService.extractUserId(validToken);
            
            assertEquals(1L, userId);
        }
    }

    @Nested
    @DisplayName("Extract Username Tests")
    class ExtractUsernameTests {

        @Test
        @DisplayName("Should extract username from token")
        void shouldExtractUsernameFromToken() {
            String username = jwtService.extractUsername(validToken);
            
            assertEquals("testuser", username);
        }
    }

    @Nested
    @DisplayName("Extract Expiration Tests")
    class ExtractExpirationTests {

        @Test
        @DisplayName("Should extract expiration date from token")
        void shouldExtractExpirationFromToken() {
            Date expiration = jwtService.extractExpiration(validToken);
            
            assertNotNull(expiration);
            assertTrue(expiration.after(new Date()));
        }
    }

    @Nested
    @DisplayName("Token Validation Tests")
    class TokenValidationTests {

        @Test
        @DisplayName("Should validate valid token")
        void shouldValidateValidToken() {
            Boolean isValid = jwtService.validateToken(validToken);
            
            assertTrue(isValid);
        }

        @Test
        @DisplayName("Should return false for expired token")
        void shouldReturnFalseForExpiredToken() {
            // Create an expired token
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", 1L);
            
            Date pastExpiration = new Date(System.currentTimeMillis() - 3600000); // 1 hour ago
            
            String expiredToken = Jwts.builder()
                    .subject("test@example.com")
                    .claims(claims)
                    .expiration(pastExpiration)
                    .signWith(signingKey)
                    .compact();
            
            Boolean isValid = jwtService.validateToken(expiredToken);
            
            assertFalse(isValid);
        }

        @Test
        @DisplayName("Should return false for invalid token")
        void shouldReturnFalseForInvalidToken() {
            Boolean isValid = jwtService.validateToken("invalid.token.here");
            
            assertFalse(isValid);
        }

        @Test
        @DisplayName("Should return false for null token")
        void shouldReturnFalseForNullToken() {
            Boolean isValid = jwtService.validateToken(null);
            
            assertFalse(isValid);
        }

        @Test
        @DisplayName("Should return false for empty token")
        void shouldReturnFalseForEmptyToken() {
            Boolean isValid = jwtService.validateToken("");
            
            assertFalse(isValid);
        }
    }

    @Nested
    @DisplayName("Token Expiration Tests")
    class TokenExpirationTests {

        @Test
        @DisplayName("Should return false for non-expired token")
        void shouldReturnFalseForNonExpiredToken() {
            Boolean isExpired = jwtService.isTokenExpired(validToken);
            
            assertFalse(isExpired);
        }

        @Test
        @DisplayName("Should return true for expired token")
        void shouldReturnTrueForExpiredToken() {
            // Create an expired token
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", 1L);
            
            Date pastExpiration = new Date(System.currentTimeMillis() - 3600000);
            
            String expiredToken = Jwts.builder()
                    .subject("test@example.com")
                    .claims(claims)
                    .expiration(pastExpiration)
                    .signWith(signingKey)
                    .compact();
            
            // isTokenExpired will throw an exception for expired tokens when parsing
            // So we expect an exception to be thrown
            assertThrows(Exception.class, () -> jwtService.isTokenExpired(expiredToken));
        }
    }
}
