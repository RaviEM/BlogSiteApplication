package com.blogsite.blog_common.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ValidationException Tests")
class ValidationExceptionTest {

    @Test
    @DisplayName("Should create exception with message only")
    void shouldCreateExceptionWithMessageOnly() {
        ValidationException exception = new ValidationException("Validation failed");
        
        assertEquals("Validation failed", exception.getMessage());
        assertNotNull(exception.getErrors());
        assertTrue(exception.getErrors().isEmpty());
        assertFalse(exception.hasError());
    }

    @Test
    @DisplayName("Should create exception with message and errors map")
    void shouldCreateExceptionWithMessageAndErrors() {
        Map<String, String> errors = new HashMap<>();
        errors.put("email", "Email is invalid");
        errors.put("password", "Password is too short");
        
        ValidationException exception = new ValidationException("Validation failed", errors);
        
        assertEquals("Validation failed", exception.getMessage());
        assertEquals(2, exception.getErrors().size());
        assertTrue(exception.getErrors().containsKey("email"));
        assertTrue(exception.getErrors().containsKey("password"));
        assertTrue(exception.hasError());
    }

    @Test
    @DisplayName("Should create exception with field and error message")
    void shouldCreateExceptionWithFieldAndMessage() {
        ValidationException exception = new ValidationException("email", "Email is invalid");
        
        assertEquals("Email is invalid", exception.getMessage());
        assertEquals(1, exception.getErrors().size());
        assertTrue(exception.getErrors().containsKey("email"));
        assertEquals("Email is invalid", exception.getErrors().get("email"));
        assertTrue(exception.hasError());
    }

    @Test
    @DisplayName("Should add error to existing exception")
    void shouldAddErrorToExistingException() {
        ValidationException exception = new ValidationException("Validation failed");
        
        assertFalse(exception.hasError());
        
        exception.addError("email", "Email is invalid");
        
        assertTrue(exception.hasError());
        assertEquals(1, exception.getErrors().size());
        assertTrue(exception.getErrors().containsKey("email"));
    }

    @Test
    @DisplayName("Should add multiple errors")
    void shouldAddMultipleErrors() {
        ValidationException exception = new ValidationException("Validation failed");
        
        exception.addError("email", "Email is invalid");
        exception.addError("password", "Password is too short");
        exception.addError("username", "Username is required");
        
        assertTrue(exception.hasError());
        assertEquals(3, exception.getErrors().size());
        assertTrue(exception.getErrors().containsKey("email"));
        assertTrue(exception.getErrors().containsKey("password"));
        assertTrue(exception.getErrors().containsKey("username"));
    }

    @Test
    @DisplayName("Should overwrite error when same field is added twice")
    void shouldOverwriteErrorWhenSameFieldAddedTwice() {
        ValidationException exception = new ValidationException("Validation failed");
        
        exception.addError("email", "Email is invalid");
        exception.addError("email", "Email is required");
        
        assertEquals(1, exception.getErrors().size());
        assertEquals("Email is required", exception.getErrors().get("email"));
    }
}
