package com.blogsite.blog_common.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DuplicateResourceException Tests")
class DuplicateResourceExceptionTest {

    @Test
    @DisplayName("Should create exception with resource name, field name, and field value")
    void shouldCreateExceptionWithResourceDetails() {
        DuplicateResourceException exception = new DuplicateResourceException("User", "email", "test@example.com");
        
        assertEquals("User already exists with value email: 'test@example.com'", exception.getMessage());
        assertEquals("User", exception.getResourceName());
        assertEquals("email", exception.getFieldName());
        assertEquals("test@example.com", exception.getFieldValue());
    }

    @Test
    @DisplayName("Should create exception with message only")
    void shouldCreateExceptionWithMessageOnly() {
        DuplicateResourceException exception = new DuplicateResourceException("Resource already exists");
        
        assertEquals("Resource already exists", exception.getMessage());
        assertNull(exception.getResourceName());
        assertNull(exception.getFieldName());
        assertNull(exception.getFieldValue());
    }

    @Test
    @DisplayName("Should handle numeric field value")
    void shouldHandleNumericFieldValue() {
        DuplicateResourceException exception = new DuplicateResourceException("Blog", "id", 123L);
        
        assertEquals("Blog already exists with value id: '123'", exception.getMessage());
        assertEquals("Blog", exception.getResourceName());
        assertEquals("id", exception.getFieldName());
        assertEquals(123L, exception.getFieldValue());
    }
}
