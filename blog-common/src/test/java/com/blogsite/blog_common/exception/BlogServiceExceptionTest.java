package com.blogsite.blog_common.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BlogServiceException Tests")
class BlogServiceExceptionTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create exception with message")
        void shouldCreateExceptionWithMessage() {
            String message = "Service error occurred";

            BlogServiceException exception = new BlogServiceException(message);

            assertNotNull(exception);
            assertEquals(message, exception.getMessage());
            assertNull(exception.getCause());
        }

        @Test
        @DisplayName("Should create exception with message and cause")
        void shouldCreateExceptionWithMessageAndCause() {
            String message = "Service error occurred";
            Throwable cause = new RuntimeException("Root cause");

            BlogServiceException exception = new BlogServiceException(message, cause);

            assertNotNull(exception);
            assertEquals(message, exception.getMessage());
            assertEquals(cause, exception.getCause());
        }

        @Test
        @DisplayName("Should handle null message")
        void shouldHandleNullMessage() {
            BlogServiceException exception = new BlogServiceException((String) null);

            assertNotNull(exception);
            assertNull(exception.getMessage());
        }

        @Test
        @DisplayName("Should handle null cause")
        void shouldHandleNullCause() {
            String message = "Service error occurred";

            BlogServiceException exception = new BlogServiceException(message, null);

            assertNotNull(exception);
            assertEquals(message, exception.getMessage());
            assertNull(exception.getCause());
        }
    }
}
