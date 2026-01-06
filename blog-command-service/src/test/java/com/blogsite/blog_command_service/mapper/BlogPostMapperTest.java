package com.blogsite.blog_command_service.mapper;

import com.blogsite.blog_common.model.dto.request.BlogPostRequest;
import com.blogsite.blog_common.model.dto.response.BlogPostResponse;
import com.blogsite.blog_common.model.entity.BlogPost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BlogPostMapper Tests")
class BlogPostMapperTest {

    private BlogPostMapper blogPostMapper;

    @BeforeEach
    void setUp() {
        blogPostMapper = new BlogPostMapper();
    }

    @Test
    @DisplayName("Should map BlogPostRequest to BlogPost entity")
    void shouldMapRequestToEntity() {
        // Given
        BlogPostRequest request = BlogPostRequest.builder()
                .blogName("Test Blog")
                .content("This is a test blog content with enough words to meet minimum requirements")
                .category("Technology")
                .tagIds(Arrays.asList("tag1", "tag2"))
                .build();

        // When
        BlogPost entity = blogPostMapper.toEntity(request);

        // Then
        assertNotNull(entity);
        assertEquals("Test Blog", entity.getBlogName());
        assertEquals("This is a test blog content with enough words to meet minimum requirements", entity.getContent());
        assertEquals("Technology", entity.getCategory());
        assertNotNull(entity.getTagIds());
        assertEquals(2, entity.getTagIds().size());
        assertNotNull(entity.getCommentIds());
        assertNotNull(entity.getLikeIds());
    }

    @Test
    @DisplayName("Should map BlogPostRequest with null tagIds to entity with empty list")
    void shouldMapRequestWithNullTagIds() {
        // Given
        BlogPostRequest request = BlogPostRequest.builder()
                .blogName("Test Blog")
                .content("Content")
                .category("Technology")
                .tagIds(null)
                .build();

        // When
        BlogPost entity = blogPostMapper.toEntity(request);

        // Then
        assertNotNull(entity);
        assertNotNull(entity.getTagIds());
        assertTrue(entity.getTagIds().isEmpty());
    }

    @Test
    @DisplayName("Should return null when mapping null request")
    void shouldReturnNullWhenMappingNullRequest() {
        // When
        BlogPost entity = blogPostMapper.toEntity(null);

        // Then
        assertNull(entity);
    }

    @Test
    @DisplayName("Should map BlogPost entity to BlogPostResponse")
    void shouldMapEntityToResponse() {
        // Given
        BlogPost entity = BlogPost.builder()
                .postId("post123")
                .blogName("Test Blog")
                .content("Content")
                .category("Technology")
                .categoryId("cat123")
                .authorId(1L)
                .authorName("TestUser")
                .tagIds(Arrays.asList("tag1", "tag2"))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isPublished(true)
                .viewCount(100L)
                .likeCount(50L)
                .commentCount(25L)
                .build();

        // When
        BlogPostResponse response = blogPostMapper.toResponse(entity);

        // Then
        assertNotNull(response);
        assertEquals("post123", response.getPostId());
        assertEquals("Test Blog", response.getBlogName());
        assertEquals("Content", response.getContent());
        assertEquals("Technology", response.getCategory());
        assertEquals("cat123", response.getCategoryId());
        assertEquals(1L, response.getAuthorId());
        assertEquals("TestUser", response.getAuthorName());
        assertNotNull(response.getTagIds());
        assertEquals(2, response.getTagIds().size());
        assertTrue(response.getIsPublished());
        assertEquals(100L, response.getViewCount());
        assertEquals(50L, response.getLikeCount());
        assertEquals(25L, response.getCommentCount());
    }

    @Test
    @DisplayName("Should return null when mapping null entity")
    void shouldReturnNullWhenMappingNullEntity() {
        // When
        BlogPostResponse response = blogPostMapper.toResponse(null);

        // Then
        assertNull(response);
    }

    @Test
    @DisplayName("Should map entity with null optional fields")
    void shouldMapEntityWithNullOptionalFields() {
        // Given
        BlogPost entity = BlogPost.builder()
                .postId("post123")
                .blogName("Test Blog")
                .content("Content")
                .category("Technology")
                .build();

        // When
        BlogPostResponse response = blogPostMapper.toResponse(entity);

        // Then
        assertNotNull(response);
        assertEquals("post123", response.getPostId());
        assertNull(response.getCategoryId());
        assertNull(response.getAuthorId());
        assertNull(response.getAuthorName());
    }
}
