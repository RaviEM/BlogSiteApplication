package com.blogsite.blog_query_service.controller;

import com.blogsite.blog_common.model.dto.response.ApiResponse;
import com.blogsite.blog_query_service.dto.BlogWithAuthorResponse;
import com.blogsite.blog_query_service.service.BlogQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserBlogController.class, excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration.class,
        org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration.class
})
@DisplayName("UserBlogController Tests")
class UserBlogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlogQueryService blogQueryService;

    @Test
    @DisplayName("Should get all blogs by author name successfully")
    void shouldGetAllBlogsByAuthorNameSuccessfully() throws Exception {
        // Given
        BlogWithAuthorResponse blog = BlogWithAuthorResponse.builder()
                .postId("post123")
                .blogName("Test Blog")
                .build();
        List<BlogWithAuthorResponse> blogs = Arrays.asList(blog);
        when(blogQueryService.getBlogsByAuthorName("testuser")).thenReturn(blogs);

        // When/Then
        mockMvc.perform(get("/api/v1.0/blogsite/user/getall")
                        .param("authorName", "testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].postId").value("post123"));
    }

    @Test
    @DisplayName("Should return empty list when no blogs found")
    void shouldReturnEmptyListWhenNoBlogsFound() throws Exception {
        // Given
        when(blogQueryService.getBlogsByAuthorName(anyString())).thenReturn(List.of());

        // When/Then
        mockMvc.perform(get("/api/v1.0/blogsite/user/getall")
                        .param("authorName", "nonexistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(0));
    }
}
