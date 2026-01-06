package com.blogsite.blog_command_service.controller;

import com.blogsite.blog_common.model.dto.request.BlogPostRequest;
import com.blogsite.blog_common.model.dto.response.ApiResponse;
import com.blogsite.blog_common.model.dto.response.BlogPostResponse;
import com.blogsite.blog_command_service.service.BlogPostService;
import com.blogsite.blog_command_service.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.servlet.http.HttpServletRequest;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = BlogPostController.class,
        properties = {
                "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration,org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration,org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration,org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration"
        }
)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
        "spring.data.mongodb.uri=mongodb://localhost:27017/test"
})
@DisplayName("BlogPostController Tests")
class BlogPostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlogPostService blogPostService;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("Add Blog Post Tests")
    class AddBlogPostTests {

        @Test
        @DisplayName("Should create blog post successfully")
        void shouldCreateBlogPostSuccessfully() throws Exception {
            // Given
            String blogName = "Test Blog Post Name";
            BlogPostRequest request = new BlogPostRequest();
            request.setContent("This is a test blog post content with enough words to pass validation.");
            request.setCategory("Technology Category Name");

            BlogPostResponse response = BlogPostResponse.builder()
                    .postId("post123")
                    .blogName(blogName)
                    .build();

            when(jwtService.extractUserId(anyString())).thenReturn(1L);
            when(jwtService.extractEmail(anyString())).thenReturn("test@example.com");
            when(jwtService.extractUsername(anyString())).thenReturn("testuser");
            when(blogPostService.createBlogPost(any(BlogPostRequest.class), anyLong(), anyString(), anyString()))
                    .thenReturn(response);

            // When/Then
            mockMvc.perform(post("/api/v1.0/blogsite/user/blogs/add/{blogName}", blogName)
                    .header("Authorization", "Bearer test-token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.postId").value("post123"));

            verify(blogPostService).createBlogPost(any(BlogPostRequest.class), eq(1L), eq("testuser"), eq("test@example.com"));
        }

        @Test
        @DisplayName("Should use email as author name when username is null")
        void shouldUseEmailAsAuthorNameWhenUsernameIsNull() throws Exception {
            // Given
            String blogName = "Test Blog Post";
            BlogPostRequest request = new BlogPostRequest();
            request.setContent("This is a test blog post content with enough words.");
            request.setCategory("Technology Category Name");

            BlogPostResponse response = BlogPostResponse.builder()
                    .postId("post123")
                    .build();

            when(jwtService.extractUserId(anyString())).thenReturn(1L);
            when(jwtService.extractEmail(anyString())).thenReturn("test@example.com");
            when(jwtService.extractUsername(anyString())).thenReturn(null);
            when(blogPostService.createBlogPost(any(BlogPostRequest.class), anyLong(), anyString(), anyString()))
                    .thenReturn(response);

            // When/Then
            mockMvc.perform(post("/api/v1.0/blogsite/user/blogs/add/{blogName}", blogName)
                    .header("Authorization", "Bearer test-token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());

            verify(blogPostService).createBlogPost(any(BlogPostRequest.class), eq(1L), eq("test@example.com"), eq("test@example.com"));
        }

        @Test
        @DisplayName("Should set blog name from path variable when not in request")
        void shouldSetBlogNameFromPathVariable() throws Exception {
            // Given
            String blogName = "Test Blog Post Name";
            BlogPostRequest request = new BlogPostRequest();
            request.setBlogName(null);
            request.setContent("This is a test blog post content with enough words.");
            request.setCategory("Technology Category Name");

            BlogPostResponse response = BlogPostResponse.builder()
                    .postId("post123")
                    .blogName(blogName)
                    .build();

            when(jwtService.extractUserId(anyString())).thenReturn(1L);
            when(jwtService.extractEmail(anyString())).thenReturn("test@example.com");
            when(jwtService.extractUsername(anyString())).thenReturn("testuser");
            when(blogPostService.createBlogPost(any(BlogPostRequest.class), anyLong(), anyString(), anyString()))
                    .thenReturn(response);

            // When/Then
            mockMvc.perform(post("/api/v1.0/blogsite/user/blogs/add/{blogName}", blogName)
                    .header("Authorization", "Bearer test-token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());

            verify(blogPostService).createBlogPost(argThat(req -> blogName.equals(req.getBlogName())), anyLong(), anyString(), anyString());
        }
    }

    @Nested
    @DisplayName("Delete Blog Post Tests")
    class DeleteBlogPostTests {

        @Test
        @DisplayName("Should delete blog post successfully")
        void shouldDeleteBlogPostSuccessfully() throws Exception {
            // Given
            String blogName = "Test Blog Post";
            when(jwtService.extractUserId(anyString())).thenReturn(1L);
            doNothing().when(blogPostService).deleteBlogPostByName(anyString(), anyLong());

            // When/Then
            mockMvc.perform(delete("/api/v1.0/blogsite/user/delete/{blogName}", blogName)
                    .header("Authorization", "Bearer test-token"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true));

            verify(blogPostService).deleteBlogPostByName(blogName, 1L);
        }
    }

    @Nested
    @DisplayName("Token Extraction Tests")
    class TokenExtractionTests {

        @Test
        @DisplayName("Should extract token from Authorization header")
        void shouldExtractTokenFromAuthorizationHeader() throws Exception {
            // Given
            String blogName = "Test Blog";
            BlogPostRequest request = new BlogPostRequest();
            request.setContent("This is a test blog post content with enough words.");
            request.setCategory("Technology Category Name");

            when(jwtService.extractUserId("valid-token")).thenReturn(1L);
            when(jwtService.extractEmail("valid-token")).thenReturn("test@example.com");
            when(jwtService.extractUsername("valid-token")).thenReturn("testuser");
            when(blogPostService.createBlogPost(any(), anyLong(), anyString(), anyString()))
                    .thenReturn(BlogPostResponse.builder().postId("post123").build());

            // When/Then
            mockMvc.perform(post("/api/v1.0/blogsite/user/blogs/add/{blogName}", blogName)
                    .header("Authorization", "Bearer valid-token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());

            verify(jwtService).extractUserId("valid-token");
        }

        @Test
        @DisplayName("Should throw exception when Authorization header is missing")
        void shouldThrowExceptionWhenAuthorizationHeaderMissing() throws Exception {
            // Given
            String blogName = "Test Blog";
            BlogPostRequest request = new BlogPostRequest();
            request.setContent("This is a test blog post content.");
            request.setCategory("Technology Category Name");

            // When/Then
            mockMvc.perform(post("/api/v1.0/blogsite/user/blogs/add/{blogName}", blogName)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should throw exception when Authorization header doesn't start with Bearer")
        void shouldThrowExceptionWhenAuthorizationHeaderInvalid() throws Exception {
            // Given
            String blogName = "Test Blog";
            BlogPostRequest request = new BlogPostRequest();
            request.setContent("This is a test blog post content.");
            request.setCategory("Technology Category Name");

            // When/Then
            mockMvc.perform(post("/api/v1.0/blogsite/user/blogs/add/{blogName}", blogName)
                    .header("Authorization", "Invalid token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }
}
