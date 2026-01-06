package com.blogsite.blog_user_service.exception;

import com.blogsite.blog_common.exception.DuplicateResourceException;
import com.blogsite.blog_common.exception.ResourceNotFoundException;
import com.blogsite.blog_common.exception.UnauthorizedException;
import com.blogsite.blog_common.exception.ValidationException;
import com.blogsite.blog_common.model.dto.response.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        webRequest = new ServletWebRequest(request);
    }

    @Test
    @DisplayName("Should handle ResourceNotFoundException")
    void shouldHandleResourceNotFoundException() {
        // Given
        ResourceNotFoundException ex = new ResourceNotFoundException("User", "userId", 1L);

        // When
        ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleResourceNotFoundException(ex, webRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertNotNull(response.getBody().getMessage());
    }

    @Test
    @DisplayName("Should handle DuplicateResourceException")
    void shouldHandleDuplicateResourceException() {
        // Given
        DuplicateResourceException ex = new DuplicateResourceException("User", "email", "test@example.com");

        // When
        ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleDuplicateResourceException(ex, webRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
    }

    @Test
    @DisplayName("Should handle UnauthorizedException")
    void shouldHandleUnauthorizedException() {
        // Given
        UnauthorizedException ex = UnauthorizedException.invalidCredentials();

        // When
        ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleUnauthorizedException(ex, webRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
    }

    @Test
    @DisplayName("Should handle ValidationException with errors")
    void shouldHandleValidationException() {
        // Given
        ValidationException ex = new ValidationException("Validation failed");
        ex.addError("email", "Email is invalid");
        ex.addError("password", "Password is too short");

        // When
        ResponseEntity<ApiResponse<Map<String, String>>> response = exceptionHandler.handleValidationException(ex, webRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertNotNull(response.getBody().getData());
        assertEquals(2, response.getBody().getData().size());
        assertTrue(response.getBody().getData().containsKey("email"));
        assertTrue(response.getBody().getData().containsKey("password"));
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException")
    void shouldHandleMethodArgumentNotValidException() {
        // Given
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError1 = new FieldError("user", "email", "Email is required");
        FieldError fieldError2 = new FieldError("user", "password", "Password is required");
        
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(java.util.Arrays.asList(fieldError1, fieldError2));

        // When
        ResponseEntity<ApiResponse<Map<String, String>>> response = exceptionHandler.handleMethodArgumentNotValid(ex, webRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Validation failed", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
        assertEquals(2, response.getBody().getData().size());
    }

    @Test
    @DisplayName("Should handle generic Exception")
    void shouldHandleGenericException() {
        // Given
        Exception ex = new RuntimeException("Unexpected error occurred");

        // When
        ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleGlobalException(ex, webRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("Unexpected error"));
    }

    @Test
    @DisplayName("Should handle ValidationException without errors")
    void shouldHandleValidationExceptionWithoutErrors() {
        // Given
        ValidationException ex = new ValidationException("Validation failed");

        // When
        ResponseEntity<ApiResponse<Map<String, String>>> response = exceptionHandler.handleValidationException(ex, webRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
    }
}
