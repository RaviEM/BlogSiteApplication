package com.blogsite.blog_common.model.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DTO Response Tests")
class DtoResponseTests {

    @Nested
    @DisplayName("UserResponse Tests")
    class UserResponseTests {

        @Test
        @DisplayName("Should create UserResponse using builder")
        void shouldCreateUserResponseUsingBuilder() {
            List<String> postIds = new ArrayList<>();
            postIds.add("post1");
            LocalDateTime now = LocalDateTime.now();

            UserResponse response = UserResponse.builder()
                    .userId(1L)
                    .username("testuser")
                    .email("test@example.com")
                    .postIds(postIds)
                    .createdAt(now)
                    .isActive(true)
                    .build();

            assertEquals(1L, response.getUserId());
            assertEquals("testuser", response.getUsername());
            assertEquals("test@example.com", response.getEmail());
            assertEquals(postIds, response.getPostIds());
            assertEquals(now, response.getCreatedAt());
            assertTrue(response.getIsActive());
        }

        @Test
        @DisplayName("Should create UserResponse using no-args constructor")
        void shouldCreateUserResponseUsingNoArgsConstructor() {
            UserResponse response = new UserResponse();

            assertNotNull(response);
            assertNull(response.getUserId());
            assertNull(response.getUsername());
        }
    }

    @Nested
    @DisplayName("BlogPostResponse Tests")
    class BlogPostResponseTests {

        @Test
        @DisplayName("Should create BlogPostResponse using builder")
        void shouldCreateBlogPostResponseUsingBuilder() {
            List<String> tagIds = new ArrayList<>();
            tagIds.add("tag1");
            List<String> tagNames = new ArrayList<>();
            tagNames.add("Technology");
            LocalDateTime now = LocalDateTime.now();

            BlogPostResponse response = BlogPostResponse.builder()
                    .postId("post123")
                    .blogName("Test Blog Post")
                    .content("Content here")
                    .category("Technology")
                    .categoryId("cat123")
                    .authorId(1L)
                    .authorName("Author")
                    .tagIds(tagIds)
                    .tagNames(tagNames)
                    .createdAt(now)
                    .updatedAt(now)
                    .isPublished(true)
                    .viewCount(100L)
                    .likeCount(50L)
                    .commentCount(25L)
                    .build();

            assertEquals("post123", response.getPostId());
            assertEquals("Test Blog Post", response.getBlogName());
            assertEquals("Content here", response.getContent());
            assertEquals("Technology", response.getCategory());
            assertEquals(1L, response.getAuthorId());
            assertEquals(tagIds, response.getTagIds());
            assertEquals(now, response.getCreatedAt());
            assertTrue(response.getIsPublished());
            assertEquals(100L, response.getViewCount());
        }
    }

    @Nested
    @DisplayName("CategoryResponse Tests")
    class CategoryResponseTests {

        @Test
        @DisplayName("Should create CategoryResponse using builder")
        void shouldCreateCategoryResponseUsingBuilder() {
            CategoryResponse response = CategoryResponse.builder()
                    .categoryId("cat123")
                    .name("Technology")
                    .description("Tech category")
                    .postCount(10L)
                    .build();

            assertEquals("cat123", response.getCategoryId());
            assertEquals("Technology", response.getName());
            assertEquals("Tech category", response.getDescription());
            assertEquals(10L, response.getPostCount());
        }
    }

    @Nested
    @DisplayName("AuthResponse Tests")
    class AuthResponseTests {

        @Test
        @DisplayName("Should create AuthResponse using builder")
        void shouldCreateAuthResponseUsingBuilder() {
            UserResponse user = UserResponse.builder()
                    .userId(1L)
                    .username("testuser")
                    .build();

            AuthResponse response = AuthResponse.builder()
                    .token("jwt-token-123")
                    .tokenType("Bearer")
                    .expiresIn(3600L)
                    .user(user)
                    .build();

            assertEquals("jwt-token-123", response.getToken());
            assertEquals("Bearer", response.getTokenType());
            assertEquals(3600L, response.getExpiresIn());
            assertEquals(user, response.getUser());
        }

        @Test
        @DisplayName("Should create AuthResponse using static factory method")
        void shouldCreateAuthResponseUsingStaticFactoryMethod() {
            UserResponse user = UserResponse.builder()
                    .userId(1L)
                    .username("testuser")
                    .build();

            AuthResponse response = AuthResponse.of("jwt-token-123", 3600L, user);

            assertEquals("jwt-token-123", response.getToken());
            assertEquals("Bearer", response.getTokenType());
            assertEquals(3600L, response.getExpiresIn());
            assertEquals(user, response.getUser());
        }
    }

    @Nested
    @DisplayName("PagedResponse Tests")
    class PagedResponseTests {

        @Test
        @DisplayName("Should create PagedResponse using builder")
        void shouldCreatePagedResponseUsingBuilder() {
            List<String> content = new ArrayList<>();
            content.add("item1");
            content.add("item2");

            PagedResponse<String> response = PagedResponse.<String>builder()
                    .content(content)
                    .page(0)
                    .size(10)
                    .totalElements(2L)
                    .totalPages(1)
                    .last(true)
                    .build();

            assertEquals(content, response.getContent());
            assertEquals(0, response.getPage());
            assertEquals(10, response.getSize());
            assertEquals(2L, response.getTotalElements());
            assertEquals(1, response.getTotalPages());
            assertTrue(response.isLast());
        }

        @Test
        @DisplayName("Should create PagedResponse using static factory method")
        void shouldCreatePagedResponseUsingStaticFactoryMethod() {
            List<String> content = new ArrayList<>();
            content.add("item1");
            content.add("item2");

            PagedResponse<String> response = PagedResponse.of(content, 0, 10, 2L);

            assertEquals(content, response.getContent());
            assertEquals(0, response.getPage());
            assertEquals(10, response.getSize());
            assertEquals(2L, response.getTotalElements());
            assertEquals(1, response.getTotalPages());
            assertTrue(response.isFirst());
            assertTrue(response.isLast());
        }

        @Test
        @DisplayName("Should calculate first and last correctly")
        void shouldCalculateFirstAndLastCorrectly() {
            List<String> content = new ArrayList<>();
            content.add("item1");

            // First page
            PagedResponse<String> firstPage = PagedResponse.of(content, 0, 10, 25L);
            assertTrue(firstPage.isFirst());
            assertFalse(firstPage.isLast());

            // Last page
            PagedResponse<String> lastPage = PagedResponse.of(content, 2, 10, 25L);
            assertFalse(lastPage.isFirst());
            assertTrue(lastPage.isLast());

            // Middle page
            PagedResponse<String> middlePage = PagedResponse.of(content, 1, 10, 25L);
            assertFalse(middlePage.isFirst());
            assertFalse(middlePage.isLast());
        }

        @Test
        @DisplayName("Should calculate total pages correctly")
        void shouldCalculateTotalPagesCorrectly() {
            List<String> content = new ArrayList<>();
            content.add("item1");

            PagedResponse<String> response = PagedResponse.of(content, 0, 10, 25L);
            assertEquals(3, response.getTotalPages()); // ceil(25/10) = 3
        }
    }
}
