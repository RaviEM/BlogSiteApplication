package com.blogsite.blog_common.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ResourceNotFoundException Tests")
class ResourceNotFoundExceptionTest {

    @Test
    @DisplayName("Should create exception with resource name, field name, and field value")
    void shouldCreateExceptionWithResourceDetails() {
        ResourceNotFoundException exception = new ResourceNotFoundException("User", "userId", 123L);
        
        assertEquals("User not found with userId: '123'", exception.getMessage());
        assertEquals("User", exception.getResourceName());
        assertEquals("userId", exception.getFieldName());
        assertEquals(123L, exception.getFieldValue());
    }

    @Test
    @DisplayName("Should create exception with message only")
    void shouldCreateExceptionWithMessageOnly() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource not found");
        
        assertEquals("Resource not found", exception.getMessage());
        assertNull(exception.getResourceName());
        assertNull(exception.getFieldName());
        assertNull(exception.getFieldValue());
    }

    @Test
    @DisplayName("Should handle string field value")
    void shouldHandleStringFieldValue() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Blog", "title", "My Blog");
        
        assertEquals("Blog not found with title: 'My Blog'", exception.getMessage());
        assertEquals("Blog", exception.getResourceName());
        assertEquals("title", exception.getFieldName());
        assertEquals("My Blog", exception.getFieldValue());
    }
}
