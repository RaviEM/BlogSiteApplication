package com.blogsite.blog_command_service.service.impl;

import com.blogsite.blog_common.exception.DuplicateResourceException;
import com.blogsite.blog_common.exception.ResourceNotFoundException;
import com.blogsite.blog_common.exception.ValidationException;
import com.blogsite.blog_common.model.dto.request.BlogPostRequest;
import com.blogsite.blog_common.model.dto.response.BlogPostResponse;
import com.blogsite.blog_common.model.entity.BlogPost;
import com.blogsite.blog_common.model.entity.Category;
import com.blogsite.blog_command_service.mapper.BlogPostMapper;
import com.blogsite.blog_command_service.repository.BlogPostRepository;
import com.blogsite.blog_command_service.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BlogPostServiceImpl Tests")
class BlogPostServiceImplTest {

    @Mock
    private BlogPostRepository blogPostRepository;

    @Mock
    private BlogPostMapper blogPostMapper;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private BlogPostServiceImpl blogPostService;

    private BlogPostRequest request;
    private BlogPost blogPost;
    private BlogPostResponse response;
    private Category category;

    @BeforeEach
    void setUp() {
        request = BlogPostRequest.builder()
                .blogName("This is a very long blog name that exceeds minimum length requirement")
                .category("This is a very long category name that exceeds minimum")
                .content(generateContent(1000))
                .build();

        category = Category.builder()
                .categoryId("cat123")
                .name("This is a very long category name that exceeds minimum")
                .postCount(0L)
                .build();

        blogPost = BlogPost.builder()
                .postId("post123")
                .blogName(request.getBlogName())
                .category(request.getCategory())
                .content(request.getContent())
                .categoryId("cat123")
                .authorId(1L)
                .authorName("testuser")
                .authorEmail("test@example.com")
                .isPublished(true)
                .build();

        response = BlogPostResponse.builder()
                .postId("post123")
                .blogName(request.getBlogName())
                .category(request.getCategory())
                .content(request.getContent())
                .build();
    }

    @Nested
    @DisplayName("Create Blog Post Tests")
    class CreateBlogPostTests {
        @Test
        @DisplayName("Should create blog post successfully")
        void shouldCreateBlogPostSuccessfully() {
            // Given
            when(blogPostRepository.existsByBlogNameAndAuthorId(anyString(), anyLong())).thenReturn(false);
            when(categoryService.getOrCreateCategory(anyString())).thenReturn(category);
            when(blogPostMapper.toEntity(any(BlogPostRequest.class))).thenReturn(blogPost);
            when(blogPostRepository.save(any(BlogPost.class))).thenReturn(blogPost);
            when(blogPostMapper.toResponse(any(BlogPost.class))).thenReturn(response);

            // When
            BlogPostResponse result = blogPostService.createBlogPost(request, 1L, "testuser", "test@example.com");

            // Then
            assertNotNull(result);
            assertEquals("post123", result.getPostId());
            verify(blogPostRepository).existsByBlogNameAndAuthorId(request.getBlogName(), 1L);
            verify(categoryService).getOrCreateCategory(request.getCategory());
            verify(blogPostRepository).save(any(BlogPost.class));
            verify(categoryService).incrementPostCount("cat123", "post123");
        }

        @Test
        @DisplayName("Should throw ValidationException for short blog name")
        void shouldThrowExceptionForShortBlogName() {
            // Given
            request.setBlogName("Short");

            // When/Then
            assertThrows(ValidationException.class, 
                () -> blogPostService.createBlogPost(request, 1L, "testuser", "test@example.com"));
            verify(blogPostRepository, never()).save(any(BlogPost.class));
        }

        @Test
        @DisplayName("Should throw ValidationException for short category")
        void shouldThrowExceptionForShortCategory() {
            // Given
            request.setCategory("Short");

            // When/Then
            assertThrows(ValidationException.class, 
                () -> blogPostService.createBlogPost(request, 1L, "testuser", "test@example.com"));
            verify(blogPostRepository, never()).save(any(BlogPost.class));
        }

        @Test
        @DisplayName("Should throw ValidationException for insufficient word count")
        void shouldThrowExceptionForInsufficientWords() {
            // Given
            request.setContent("Short content");

            // When/Then
            assertThrows(ValidationException.class, 
                () -> blogPostService.createBlogPost(request, 1L, "testuser", "test@example.com"));
            verify(blogPostRepository, never()).save(any(BlogPost.class));
        }

        @Test
        @DisplayName("Should throw DuplicateResourceException for duplicate blog name")
        void shouldThrowExceptionForDuplicateBlogName() {
            // Given
            when(blogPostRepository.existsByBlogNameAndAuthorId(anyString(), anyLong())).thenReturn(true);

            // When/Then
            assertThrows(DuplicateResourceException.class, 
                () -> blogPostService.createBlogPost(request, 1L, "testuser", "test@example.com"));
            verify(blogPostRepository, never()).save(any(BlogPost.class));
        }
    }

    @Nested
    @DisplayName("Delete Blog Post Tests")
    class DeleteBlogPostTests {
        @Test
        @DisplayName("Should delete blog post successfully")
        void shouldDeleteBlogPostSuccessfully() {
            // Given
            when(blogPostRepository.findByBlogNameAndAuthorId(anyString(), anyLong()))
                    .thenReturn(Optional.of(blogPost));

            // When
            blogPostService.deleteBlogPostByName("Blog Name", 1L);

            // Then
            verify(blogPostRepository).findByBlogNameAndAuthorId("Blog Name", 1L);
            verify(categoryService).decrementPostCount("cat123", "post123");
            verify(blogPostRepository).delete(blogPost);
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when blog post not found")
        void shouldThrowExceptionWhenBlogPostNotFound() {
            // Given
            when(blogPostRepository.findByBlogNameAndAuthorId(anyString(), anyLong()))
                    .thenReturn(Optional.empty());

            // When/Then
            assertThrows(ResourceNotFoundException.class, 
                () -> blogPostService.deleteBlogPostByName("NonExistent", 1L));
            verify(blogPostRepository, never()).delete(any(BlogPost.class));
        }

        @Test
        @DisplayName("Should handle blog post without category")
        void shouldHandleBlogPostWithoutCategory() {
            // Given
            blogPost.setCategoryId(null);
            when(blogPostRepository.findByBlogNameAndAuthorId(anyString(), anyLong()))
                    .thenReturn(Optional.of(blogPost));

            // When
            blogPostService.deleteBlogPostByName("Blog Name", 1L);

            // Then
            verify(categoryService, never()).decrementPostCount(anyString(), anyString());
            verify(blogPostRepository).delete(blogPost);
        }
    }

    // Helper method
    private String generateContent(int wordCount) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < wordCount; i++) {
            sb.append("word").append(i);
            if (i < wordCount - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }
}
