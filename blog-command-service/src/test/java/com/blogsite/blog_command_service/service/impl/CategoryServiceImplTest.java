package com.blogsite.blog_command_service.service.impl;

import com.blogsite.blog_common.exception.ResourceNotFoundException;
import com.blogsite.blog_common.model.entity.Category;
import com.blogsite.blog_command_service.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CategoryServiceImpl Tests")
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .categoryId("cat123")
                .name("Technology and Innovation")
                .description("Auto-created category")
                .postCount(0L)
                .isActive(true)
                .build();
    }

    @Nested
    @DisplayName("Get Or Create Category Tests")
    class GetOrCreateCategoryTests {
        @Test
        @DisplayName("Should return existing category")
        void shouldReturnExistingCategory() {
            // Given
            when(categoryRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(category));

            // When
            Category result = categoryService.getOrCreateCategory("Technology and Innovation");

            // Then
            assertNotNull(result);
            assertEquals("cat123", result.getCategoryId());
            verify(categoryRepository).findByNameIgnoreCase("Technology and Innovation");
            verify(categoryRepository, never()).save(any(Category.class));
        }

        @Test
        @DisplayName("Should create new category when not found")
        void shouldCreateNewCategoryWhenNotFound() {
            // Given
            when(categoryRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());
            when(categoryRepository.save(any(Category.class))).thenReturn(category);

            // When
            Category result = categoryService.getOrCreateCategory("New Category Name That Is Long Enough");

            // Then
            assertNotNull(result);
            verify(categoryRepository).findByNameIgnoreCase("New Category Name That Is Long Enough");
            verify(categoryRepository).save(any(Category.class));
        }

        @Test
        @DisplayName("Should handle case-insensitive category lookup")
        void shouldHandleCaseInsensitiveLookup() {
            // Given
            when(categoryRepository.findByNameIgnoreCase("TECHNOLOGY AND INNOVATION"))
                    .thenReturn(Optional.of(category));

            // When
            Category result = categoryService.getOrCreateCategory("TECHNOLOGY AND INNOVATION");

            // Then
            assertNotNull(result);
            verify(categoryRepository).findByNameIgnoreCase("TECHNOLOGY AND INNOVATION");
        }
    }

    @Nested
    @DisplayName("Increment Post Count Tests")
    class IncrementPostCountTests {
        @Test
        @DisplayName("Should increment post count successfully")
        void shouldIncrementPostCountSuccessfully() {
            // Given
            when(categoryRepository.findById("cat123")).thenReturn(Optional.of(category));
            when(categoryRepository.save(any(Category.class))).thenReturn(category);

            // When
            categoryService.incrementPostCount("cat123", "post123");

            // Then
            verify(categoryRepository).findById("cat123");
            verify(categoryRepository).save(category);
            assertTrue(category.getPostIds().contains("post123"));
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when category not found")
        void shouldThrowExceptionWhenCategoryNotFound() {
            // Given
            when(categoryRepository.findById(anyString())).thenReturn(Optional.empty());

            // When/Then
            assertThrows(ResourceNotFoundException.class, 
                () -> categoryService.incrementPostCount("nonexistent", "post123"));
            verify(categoryRepository, never()).save(any(Category.class));
        }
    }

    @Nested
    @DisplayName("Decrement Post Count Tests")
    class DecrementPostCountTests {
        @Test
        @DisplayName("Should decrement post count successfully")
        void shouldDecrementPostCountSuccessfully() {
            // Given
            category.addPost("post123");
            when(categoryRepository.findById("cat123")).thenReturn(Optional.of(category));
            when(categoryRepository.save(any(Category.class))).thenReturn(category);

            // When
            categoryService.decrementPostCount("cat123", "post123");

            // Then
            verify(categoryRepository).findById("cat123");
            verify(categoryRepository).save(category);
        }

        @Test
        @DisplayName("Should handle category not found gracefully")
        void shouldHandleCategoryNotFoundGracefully() {
            // Given
            when(categoryRepository.findById(anyString())).thenReturn(Optional.empty());

            // When/Then - Should not throw exception
            assertDoesNotThrow(() -> categoryService.decrementPostCount("nonexistent", "post123"));
            verify(categoryRepository, never()).save(any(Category.class));
        }
    }
}
