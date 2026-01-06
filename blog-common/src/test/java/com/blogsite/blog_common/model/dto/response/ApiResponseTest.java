package com.blogsite.blog_common.model.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ApiResponse Tests")
class ApiResponseTest {

    @Nested
    @DisplayName("Success Response Tests")
    class SuccessResponseTests {

        @Test
        @DisplayName("Should create success response with data and message")
        void shouldCreateSuccessResponseWithDataAndMessage() {
            String testData = "test data";
            String message = "Operation successful";

            ApiResponse<String> response = ApiResponse.success(testData, message);

            assertNotNull(response);
            assertTrue(response.isSuccess());
            assertEquals(message, response.getMessage());
            assertEquals(testData, response.getData());
            assertNotNull(response.getTimestamp());
            assertNull(response.getPath());
        }

        @Test
        @DisplayName("Should create success response with message only")
        void shouldCreateSuccessResponseWithMessageOnly() {
            String message = "Operation successful";

            ApiResponse<String> response = ApiResponse.success(message);

            assertNotNull(response);
            assertTrue(response.isSuccess());
            assertEquals(message, response.getMessage());
            assertNull(response.getData());
            assertNotNull(response.getTimestamp());
            assertNull(response.getPath());
        }

        @Test
        @DisplayName("Should set timestamp when creating success response")
        void shouldSetTimestampWhenCreatingSuccessResponse() {
            LocalDateTime before = LocalDateTime.now();
            ApiResponse<String> response = ApiResponse.success("test");
            LocalDateTime after = LocalDateTime.now();

            assertNotNull(response.getTimestamp());
            assertTrue(response.getTimestamp().isAfter(before.minusSeconds(1)));
            assertTrue(response.getTimestamp().isBefore(after.plusSeconds(1)));
        }
    }

    @Nested
    @DisplayName("Error Response Tests")
    class ErrorResponseTests {

        @Test
        @DisplayName("Should create error response with message")
        void shouldCreateErrorResponseWithMessage() {
            String message = "Error occurred";

            ApiResponse<String> response = ApiResponse.error(message);

            assertNotNull(response);
            assertFalse(response.isSuccess());
            assertEquals(message, response.getMessage());
            assertNull(response.getData());
            assertNotNull(response.getTimestamp());
            assertNull(response.getPath());
        }

        @Test
        @DisplayName("Should create error response with message and path")
        void shouldCreateErrorResponseWithMessageAndPath() {
            String message = "Error occurred";
            String path = "/api/test";

            ApiResponse<String> response = ApiResponse.error(message, path);

            assertNotNull(response);
            assertFalse(response.isSuccess());
            assertEquals(message, response.getMessage());
            assertEquals(path, response.getPath());
            assertNull(response.getData());
            assertNotNull(response.getTimestamp());
        }

        @Test
        @DisplayName("Should create error response with data")
        void shouldCreateErrorResponseWithData() {
            String message = "Validation error";
            String errorData = "Field validation failed";

            ApiResponse<String> response = ApiResponse.errorWithData(message, errorData);

            assertNotNull(response);
            assertFalse(response.isSuccess());
            assertEquals(message, response.getMessage());
            assertEquals(errorData, response.getData());
            assertNotNull(response.getTimestamp());
            assertNull(response.getPath());
        }

        @Test
        @DisplayName("Should set timestamp when creating error response")
        void shouldSetTimestampWhenCreatingErrorResponse() {
            LocalDateTime before = LocalDateTime.now();
            ApiResponse<String> response = ApiResponse.error("test");
            LocalDateTime after = LocalDateTime.now();

            assertNotNull(response.getTimestamp());
            assertTrue(response.getTimestamp().isAfter(before.minusSeconds(1)));
            assertTrue(response.getTimestamp().isBefore(after.plusSeconds(1)));
        }
    }

    @Nested
    @DisplayName("Builder and Constructor Tests")
    class BuilderAndConstructorTests {

        @Test
        @DisplayName("Should create response using no-args constructor")
        void shouldCreateResponseUsingNoArgsConstructor() {
            ApiResponse<String> response = new ApiResponse<>();

            assertNotNull(response);
            assertFalse(response.isSuccess());
            assertNull(response.getMessage());
            assertNull(response.getData());
            assertNull(response.getTimestamp());
            assertNull(response.getPath());
        }

        @Test
        @DisplayName("Should create response using all-args constructor")
        void shouldCreateResponseUsingAllArgsConstructor() {
            LocalDateTime timestamp = LocalDateTime.now();
            ApiResponse<String> response = new ApiResponse<>(
                    true, "test", "data", timestamp, "/path"
            );

            assertTrue(response.isSuccess());
            assertEquals("test", response.getMessage());
            assertEquals("data", response.getData());
            assertEquals(timestamp, response.getTimestamp());
            assertEquals("/path", response.getPath());
        }

        @Test
        @DisplayName("Should create response using builder")
        void shouldCreateResponseUsingBuilder() {
            LocalDateTime timestamp = LocalDateTime.now();
            ApiResponse<String> response = ApiResponse.<String>builder()
                    .success(true)
                    .message("test")
                    .data("data")
                    .timestamp(timestamp)
                    .path("/path")
                    .build();

            assertTrue(response.isSuccess());
            assertEquals("test", response.getMessage());
            assertEquals("data", response.getData());
            assertEquals(timestamp, response.getTimestamp());
            assertEquals("/path", response.getPath());
        }
    }
}
