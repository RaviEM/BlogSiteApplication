package com.blogsite.blog_query_service.controller;

import com.blogsite.blog_common.model.dto.response.PagedResponse;
import com.blogsite.blog_query_service.dto.BlogSearchRequest;
import com.blogsite.blog_query_service.dto.BlogWithAuthorResponse;
import com.blogsite.blog_query_service.service.BlogQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = BlogQueryController.class,
        excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration.class,
                org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration.class
        },
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.REGEX,
                pattern = "com\\.blogsite\\.blog_query_service\\.repository\\..*"
        )
)
@DisplayName("BlogQueryController Tests")
class BlogQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlogQueryService blogQueryService;

    @Test
    @DisplayName("Should get blogs by category successfully")
    void shouldGetBlogsByCategorySuccessfully() throws Exception {
        // Given
        BlogWithAuthorResponse blog = BlogWithAuthorResponse.builder()
                .postId("post123")
                .blogName("Test Blog")
                .category("Technology")
                .build();
        PagedResponse<BlogWithAuthorResponse> pagedResponse = PagedResponse.<BlogWithAuthorResponse>builder()
                .content(Arrays.asList(blog))
                .page(0)
                .size(10)
                .totalElements(1L)
                .totalPages(1)
                .build();
        when(blogQueryService.getBlogsByCategory("Technology", 0, 10)).thenReturn(pagedResponse);

        // When/Then
        mockMvc.perform(get("/api/v1.0/blogsite/blogs/info/Technology")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(1));
    }

    @Test
    @DisplayName("Should get blog by ID successfully")
    void shouldGetBlogByIdSuccessfully() throws Exception {
        // Given
        BlogWithAuthorResponse blog = BlogWithAuthorResponse.builder()
                .postId("post123")
                .blogName("Test Blog")
                .build();
        when(blogQueryService.getBlogById("post123")).thenReturn(blog);

        // When/Then
        mockMvc.perform(get("/api/v1.0/blogsite/blogs/post123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.postId").value("post123"));
    }

    @Test
    @DisplayName("Should get blogs by author ID successfully")
    void shouldGetBlogsByAuthorIdSuccessfully() throws Exception {
        // Given
        BlogWithAuthorResponse blog = BlogWithAuthorResponse.builder()
                .postId("post123")
                .blogName("Test Blog")
                .build();
        PagedResponse<BlogWithAuthorResponse> pagedResponse = PagedResponse.<BlogWithAuthorResponse>builder()
                .content(Arrays.asList(blog))
                .page(0)
                .size(10)
                .totalElements(1L)
                .build();
        when(blogQueryService.getBlogsByAuthor(1L, 0, 10)).thenReturn(pagedResponse);

        // When/Then
        mockMvc.perform(get("/api/v1.0/blogsite/blogs/author/1")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray());
    }

    @Test
    @DisplayName("Should get all categories successfully")
    void shouldGetAllCategoriesSuccessfully() throws Exception {
        // Given
        List<String> categories = Arrays.asList("Technology", "Science", "Health");
        when(blogQueryService.getAllCategories()).thenReturn(categories);

        // When/Then
        mockMvc.perform(get("/api/v1.0/blogsite/blogs/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(3));
    }

    @Test
    @DisplayName("Should search blogs by category and date range successfully")
    void shouldSearchBlogsByCategoryAndDateRangeSuccessfully() throws Exception {
        // Given
        BlogWithAuthorResponse blog = BlogWithAuthorResponse.builder()
                .postId("post123")
                .blogName("Test Blog")
                .build();
        PagedResponse<BlogWithAuthorResponse> pagedResponse = PagedResponse.<BlogWithAuthorResponse>builder()
                .content(Arrays.asList(blog))
                .page(0)
                .size(10)
                .totalElements(1L)
                .build();
        when(blogQueryService.searchBlogs(any(BlogSearchRequest.class))).thenReturn(pagedResponse);

        // When/Then
        mockMvc.perform(get("/api/v1.0/blogsite/blogs/get/Technology/2024-01-01/2024-12-31")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray());
    }
}
