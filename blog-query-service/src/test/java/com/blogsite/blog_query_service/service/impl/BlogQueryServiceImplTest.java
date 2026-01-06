package com.blogsite.blog_query_service.service.impl;

import com.blogsite.blog_common.exception.ResourceNotFoundException;
import com.blogsite.blog_common.model.dto.response.PagedResponse;
import com.blogsite.blog_common.model.entity.BlogPost;
import com.blogsite.blog_query_service.dto.BlogSearchRequest;
import com.blogsite.blog_query_service.dto.BlogWithAuthorResponse;
import com.blogsite.blog_query_service.repository.BlogPostQueryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BlogQueryServiceImpl Tests")
class BlogQueryServiceImplTest {

    @Mock
    private BlogPostQueryRepository blogPostRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private BlogQueryServiceImpl blogQueryService;

    private BlogPost blogPost;
    private BlogWithAuthorResponse response;

    @BeforeEach
    void setUp() {
        blogPost = BlogPost.builder()
                .postId("post123")
                .blogName("This is a very long blog name that exceeds minimum")
                .category("Technology and Innovation")
                .content(generateContent(1000))
                .authorId(1L)
                .authorName("testuser")
                .authorEmail("test@example.com")
                .isPublished(true)
                .createdAt(LocalDateTime.now())
                .build();

        response = BlogWithAuthorResponse.builder()
                .postId("post123")
                .blogName(blogPost.getBlogName())
                .category(blogPost.getCategory())
                .build();
    }

    @Nested
    @DisplayName("Get Blogs By Category Tests")
    class GetBlogsByCategoryTests {
        @Test
        @DisplayName("Should return paged blogs by category")
        void shouldReturnPagedBlogsByCategory() {
            // Given
            Pageable pageable = PageRequest.of(0, 10);
            Page<BlogPost> blogPage = new PageImpl<>(Arrays.asList(blogPost), pageable, 1);
            when(blogPostRepository.findByCategoryIgnoreCaseAndIsPublishedTrue(anyString(), any(Pageable.class)))
                    .thenReturn(blogPage);

            // When
            PagedResponse<BlogWithAuthorResponse> result = blogQueryService.getBlogsByCategory("Technology", 0, 10);

            // Then
            assertNotNull(result);
            assertEquals(1, result.getContent().size());
            assertEquals(0, result.getPage());
            assertEquals(10, result.getSize());
            verify(blogPostRepository).findByCategoryIgnoreCaseAndIsPublishedTrue("Technology", pageable);
        }
    }

    @Nested
    @DisplayName("Search Blogs Tests")
    class SearchBlogsTests {
        @Test
        @DisplayName("Should search blogs by category only")
        void shouldSearchBlogsByCategory() {
            // Given
            BlogSearchRequest searchRequest = BlogSearchRequest.builder()
                    .category("Technology")
                    .page(0)
                    .size(10)
                    .build();
            Pageable pageable = PageRequest.of(0, 10);
            Page<BlogPost> blogPage = new PageImpl<>(Arrays.asList(blogPost), pageable, 1);
            when(blogPostRepository.findByCategoryIgnoreCaseAndIsPublishedTrue(anyString(), any(Pageable.class)))
                    .thenReturn(blogPage);

            // When
            PagedResponse<BlogWithAuthorResponse> result = blogQueryService.searchBlogs(searchRequest);

            // Then
            assertNotNull(result);
            assertEquals(1, result.getContent().size());
        }

        @Test
        @DisplayName("Should search blogs by date range only")
        void shouldSearchBlogsByDateRange() {
            // Given
            BlogSearchRequest searchRequest = BlogSearchRequest.builder()
                    .startDate(LocalDate.of(2024, 1, 1))
                    .endDate(LocalDate.of(2024, 12, 31))
                    .page(0)
                    .size(10)
                    .build();
            Pageable pageable = PageRequest.of(0, 10);
            Page<BlogPost> blogPage = new PageImpl<>(Arrays.asList(blogPost), pageable, 1);
            when(blogPostRepository.findByIsPublishedTrueAndCreatedAtBetween(any(LocalDateTime.class), 
                    any(LocalDateTime.class), any(Pageable.class))).thenReturn(blogPage);

            // When
            PagedResponse<BlogWithAuthorResponse> result = blogQueryService.searchBlogs(searchRequest);

            // Then
            assertNotNull(result);
            assertEquals(1, result.getContent().size());
        }

        @Test
        @DisplayName("Should search blogs by author ID")
        void shouldSearchBlogsByAuthorId() {
            // Given
            BlogSearchRequest searchRequest = BlogSearchRequest.builder()
                    .authorId(1L)
                    .page(0)
                    .size(10)
                    .build();
            Pageable pageable = PageRequest.of(0, 10);
            Page<BlogPost> blogPage = new PageImpl<>(Arrays.asList(blogPost), pageable, 1);
            when(blogPostRepository.findByAuthorIdAndIsPublishedTrue(anyLong(), any(Pageable.class)))
                    .thenReturn(blogPage);

            // When
            PagedResponse<BlogWithAuthorResponse> result = blogQueryService.searchBlogs(searchRequest);

            // Then
            assertNotNull(result);
            assertEquals(1, result.getContent().size());
        }

        @Test
        @DisplayName("Should return all published blogs when no criteria")
        void shouldReturnAllPublishedBlogsWhenNoCriteria() {
            // Given
            BlogSearchRequest searchRequest = BlogSearchRequest.builder()
                    .page(0)
                    .size(10)
                    .build();
            Pageable pageable = PageRequest.of(0, 10);
            Page<BlogPost> blogPage = new PageImpl<>(Arrays.asList(blogPost), pageable, 1);
            when(blogPostRepository.findByIsPublishedTrue(any(Pageable.class))).thenReturn(blogPage);

            // When
            PagedResponse<BlogWithAuthorResponse> result = blogQueryService.searchBlogs(searchRequest);

            // Then
            assertNotNull(result);
            assertEquals(1, result.getContent().size());
        }
    }

    @Nested
    @DisplayName("Get Blog By ID Tests")
    class GetBlogByIdTests {
        @Test
        @DisplayName("Should return blog when found and published")
        void shouldReturnBlogWhenFoundAndPublished() {
            // Given
            when(blogPostRepository.findById("post123")).thenReturn(Optional.of(blogPost));

            // When
            BlogWithAuthorResponse result = blogQueryService.getBlogById("post123");

            // Then
            assertNotNull(result);
            assertEquals("post123", result.getPostId());
            verify(blogPostRepository).findById("post123");
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when blog not found")
        void shouldThrowExceptionWhenBlogNotFound() {
            // Given
            when(blogPostRepository.findById(anyString())).thenReturn(Optional.empty());

            // When/Then
            assertThrows(ResourceNotFoundException.class, () -> blogQueryService.getBlogById("nonexistent"));
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when blog not published")
        void shouldThrowExceptionWhenBlogNotPublished() {
            // Given
            blogPost.setIsPublished(false);
            when(blogPostRepository.findById("post123")).thenReturn(Optional.of(blogPost));

            // When/Then
            assertThrows(ResourceNotFoundException.class, () -> blogQueryService.getBlogById("post123"));
        }
    }

    @Nested
    @DisplayName("Get Blogs By Author Name Tests")
    class GetBlogsByAuthorNameTests {
        @Test
        @DisplayName("Should return blogs by author name")
        void shouldReturnBlogsByAuthorName() {
            // Given
            when(mongoTemplate.getDb()).thenReturn(null);
            when(mongoTemplate.getCollection(anyString())).thenReturn(null);
            when(mongoTemplate.count(any(Query.class), eq(BlogPost.class))).thenReturn(1L);
            when(mongoTemplate.findOne(any(Query.class), eq(BlogPost.class))).thenReturn(blogPost);
            when(mongoTemplate.find(any(Query.class), eq(BlogPost.class))).thenReturn(Arrays.asList(blogPost));

            // When
            List<BlogWithAuthorResponse> result = blogQueryService.getBlogsByAuthorName("testuser");

            // Then
            assertNotNull(result);
            verify(mongoTemplate, atLeastOnce()).find(any(Query.class), eq(BlogPost.class));
        }
    }

    @Nested
    @DisplayName("Get Blogs By Author ID Tests")
    class GetBlogsByAuthorIdTests {
        @Test
        @DisplayName("Should return paged blogs by author ID")
        void shouldReturnPagedBlogsByAuthorId() {
            // Given
            Pageable pageable = PageRequest.of(0, 10);
            Page<BlogPost> blogPage = new PageImpl<>(Arrays.asList(blogPost), pageable, 1);
            when(blogPostRepository.findByAuthorIdAndIsPublishedTrue(anyLong(), any(Pageable.class)))
                    .thenReturn(blogPage);

            // When
            PagedResponse<BlogWithAuthorResponse> result = blogQueryService.getBlogsByAuthor(1L, 0, 10);

            // Then
            assertNotNull(result);
            assertEquals(1, result.getContent().size());
            verify(blogPostRepository).findByAuthorIdAndIsPublishedTrue(1L, pageable);
        }
    }

    @Nested
    @DisplayName("Get All Categories Tests")
    class GetAllCategoriesTests {
        @Test
        @DisplayName("Should return all distinct categories")
        void shouldReturnAllDistinctCategories() {
            // Given
            BlogPost post1 = BlogPost.builder().category("Technology").build();
            BlogPost post2 = BlogPost.builder().category("Science").build();
            when(blogPostRepository.findDistinctCategories()).thenReturn(Arrays.asList(post1, post2));

            // When
            List<String> result = blogQueryService.getAllCategories();

            // Then
            assertNotNull(result);
            assertEquals(2, result.size());
            verify(blogPostRepository).findDistinctCategories();
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
