package com.blogsite.blog_query_service.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DTO Tests")
class DtoTests {

    @Nested
    @DisplayName("BlogSearchRequest Tests")
    class BlogSearchRequestTests {

        @Test
        @DisplayName("Should create BlogSearchRequest using builder")
        void shouldCreateBlogSearchRequestUsingBuilder() {
            LocalDate startDate = LocalDate.now().minusDays(7);
            LocalDate endDate = LocalDate.now();

            BlogSearchRequest request = BlogSearchRequest.builder()
                    .category("Technology")
                    .startDate(startDate)
                    .endDate(endDate)
                    .keyword("Java")
                    .authorId(1L)
                    .page(0)
                    .size(10)
                    .build();

            assertEquals("Technology", request.getCategory());
            assertEquals(startDate, request.getStartDate());
            assertEquals(endDate, request.getEndDate());
            assertEquals("Java", request.getKeyword());
            assertEquals(1L, request.getAuthorId());
            assertEquals(0, request.getPage());
            assertEquals(10, request.getSize());
        }

        @Test
        @DisplayName("Should create BlogSearchRequest using no-args constructor")
        void shouldCreateBlogSearchRequestUsingNoArgsConstructor() {
            BlogSearchRequest request = new BlogSearchRequest();

            assertNotNull(request);
            assertNull(request.getCategory());
            assertNull(request.getStartDate());
            assertNull(request.getEndDate());
            assertNull(request.getKeyword());
            assertNull(request.getAuthorId());
            assertEquals(0, request.getPage());
            assertEquals(10, request.getSize());
        }

        @Test
        @DisplayName("Should use default values for page and size")
        void shouldUseDefaultValuesForPageAndSize() {
            BlogSearchRequest request = BlogSearchRequest.builder()
                    .category("Technology")
                    .build();

            assertEquals(0, request.getPage());
            assertEquals(10, request.getSize());
        }
    }

    @Nested
    @DisplayName("BlogWithAuthorResponse Tests")
    class BlogWithAuthorResponseTests {

        @Test
        @DisplayName("Should create BlogWithAuthorResponse using builder")
        void shouldCreateBlogWithAuthorResponseUsingBuilder() {
            LocalDateTime now = LocalDateTime.now();
            List<String> tagIds = new ArrayList<>();
            tagIds.add("tag1");
            tagIds.add("tag2");

            BlogWithAuthorResponse.AuthorDetails author = BlogWithAuthorResponse.AuthorDetails.builder()
                    .authorName("John Doe")
                    .authorEmail("john@example.com")
                    .build();

            BlogWithAuthorResponse response = BlogWithAuthorResponse.builder()
                    .postId("post123")
                    .blogName("Test Blog")
                    .article("Article content")
                    .category("Technology")
                    .tagIds(tagIds)
                    .createdAt(now)
                    .updatedAt(now)
                    .wordCount(100)
                    .author(author)
                    .build();

            assertEquals("post123", response.getPostId());
            assertEquals("Test Blog", response.getBlogName());
            assertEquals("Article content", response.getArticle());
            assertEquals("Technology", response.getCategory());
            assertEquals(tagIds, response.getTagIds());
            assertEquals(now, response.getCreatedAt());
            assertEquals(100, response.getWordCount());
            assertEquals(author, response.getAuthor());
            assertEquals("John Doe", response.getAuthor().getAuthorName());
            assertEquals("john@example.com", response.getAuthor().getAuthorEmail());
        }

        @Test
        @DisplayName("Should create BlogWithAuthorResponse using no-args constructor")
        void shouldCreateBlogWithAuthorResponseUsingNoArgsConstructor() {
            BlogWithAuthorResponse response = new BlogWithAuthorResponse();

            assertNotNull(response);
            assertNull(response.getPostId());
            assertNull(response.getBlogName());
            assertNull(response.getAuthor());
        }

        @Test
        @DisplayName("Should create AuthorDetails using builder")
        void shouldCreateAuthorDetailsUsingBuilder() {
            BlogWithAuthorResponse.AuthorDetails author = BlogWithAuthorResponse.AuthorDetails.builder()
                    .authorName("Jane Doe")
                    .authorEmail("jane@example.com")
                    .build();

            assertEquals("Jane Doe", author.getAuthorName());
            assertEquals("jane@example.com", author.getAuthorEmail());
        }

        @Test
        @DisplayName("Should create AuthorDetails using no-args constructor")
        void shouldCreateAuthorDetailsUsingNoArgsConstructor() {
            BlogWithAuthorResponse.AuthorDetails author = new BlogWithAuthorResponse.AuthorDetails();

            assertNotNull(author);
            assertNull(author.getAuthorName());
            assertNull(author.getAuthorEmail());
        }
    }
}
