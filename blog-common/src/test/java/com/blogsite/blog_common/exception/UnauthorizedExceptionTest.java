package com.blogsite.blog_common.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UnauthorizedException Tests")
class UnauthorizedExceptionTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create exception with message")
        void shouldCreateExceptionWithMessage() {
            String message = "Unauthorized access";

            UnauthorizedException exception = new UnauthorizedException(message);

            assertNotNull(exception);
            assertEquals(message, exception.getMessage());
            assertNull(exception.getCause());
        }

        @Test
        @DisplayName("Should create exception with message and cause")
        void shouldCreateExceptionWithMessageAndCause() {
            String message = "Unauthorized access";
            Throwable cause = new RuntimeException("Root cause");

            UnauthorizedException exception = new UnauthorizedException(message, cause);

            assertNotNull(exception);
            assertEquals(message, exception.getMessage());
            assertEquals(cause, exception.getCause());
        }
    }

    @Nested
    @DisplayName("Static Factory Method Tests")
    class StaticFactoryMethodTests {

        @Test
        @DisplayName("Should create invalid credentials exception")
        void shouldCreateInvalidCredentialsException() {
            UnauthorizedException exception = UnauthorizedException.invalidCredentials();

            assertNotNull(exception);
            assertEquals("Invalid email or password", exception.getMessage());
            assertNull(exception.getCause());
        }

        @Test
        @DisplayName("Should create token expired exception")
        void shouldCreateTokenExpiredException() {
            UnauthorizedException exception = UnauthorizedException.tokenExpired();

            assertNotNull(exception);
            assertEquals("Token has expired", exception.getMessage());
            assertNull(exception.getCause());
        }

        @Test
        @DisplayName("Should create access denied exception")
        void shouldCreateAccessDeniedException() {
            UnauthorizedException exception = UnauthorizedException.accessDenied();

            assertNotNull(exception);
            assertEquals("Access denied", exception.getMessage());
            assertNull(exception.getCause());
        }
    }
}
