package com.blogsite.blog_user_service.controller;

import com.blogsite.blog_common.model.dto.request.LoginRequest;
import com.blogsite.blog_common.model.dto.request.UserRegistrationRequest;
import com.blogsite.blog_common.model.dto.response.ApiResponse;
import com.blogsite.blog_common.model.dto.response.AuthResponse;
import com.blogsite.blog_common.model.dto.response.UserResponse;
import com.blogsite.blog_user_service.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.blogsite.blog_user_service.config.JwtAuthenticationFilter;
import com.blogsite.blog_user_service.config.TestSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class, 
        excludeAutoConfiguration = {
            org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
            org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class
        })
@Import(TestSecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("UserController Tests")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should register user successfully")
    void shouldRegisterUserSuccessfully() throws Exception {
        // Given
        UserRegistrationRequest request = UserRegistrationRequest.builder()
                .userName("testuser")
                .userEmail("test@example.com")
                .password("Password123")
                .build();

        UserResponse response = UserResponse.builder()
                .userId(1L)
                .username("testuser")
                .email("test@example.com")
                .build();

        when(userService.registerUser(any(UserRegistrationRequest.class))).thenReturn(response);

        // When/Then
        mockMvc.perform(post("/api/v1.0/blogsite/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.username").value("testuser"));
    }

    @Test
    @DisplayName("Should login user successfully")
    void shouldLoginUserSuccessfully() throws Exception {
        // Given
        LoginRequest request = LoginRequest.builder()
                .userEmail("test@example.com")
                .password("Password123")
                .build();

        AuthResponse authResponse = AuthResponse.of("jwt-token", 3600000L, 
                UserResponse.builder().userId(1L).username("testuser").build());

        when(userService.loginUser(any(LoginRequest.class))).thenReturn(authResponse);

        // When/Then
        mockMvc.perform(post("/api/v1.0/blogsite/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").value("jwt-token"));
    }

    @Test
    @DisplayName("Should get all users successfully")
    void shouldGetAllUsersSuccessfully() throws Exception {
        // Given
        List<UserResponse> users = Arrays.asList(
                UserResponse.builder().userId(1L).username("user1").build(),
                UserResponse.builder().userId(2L).username("user2").build()
        );
        when(userService.getAllActiveUsers()).thenReturn(users);

        // When/Then
        mockMvc.perform(get("/api/v1.0/blogsite/user/getall"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    @DisplayName("Should get user by ID successfully")
    void shouldGetUserByIdSuccessfully() throws Exception {
        // Given
        UserResponse response = UserResponse.builder()
                .userId(1L)
                .username("testuser")
                .email("test@example.com")
                .build();
        when(userService.getUserById(1L)).thenReturn(response);

        // When/Then
        mockMvc.perform(get("/api/v1.0/blogsite/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.userId").value(1));
    }

    @Test
    @DisplayName("Should delete user successfully")
    void shouldDeleteUserSuccessfully() throws Exception {
        // Given
        doNothing().when(userService).deactivateUser(1L);

        // When/Then
        mockMvc.perform(delete("/api/v1.0/blogsite/user/delete/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("Should search users successfully")
    void shouldSearchUsersSuccessfully() throws Exception {
        // Given
        List<UserResponse> users = Arrays.asList(
                UserResponse.builder().userId(1L).username("testuser").build()
        );
        when(userService.searchUsers("test")).thenReturn(users);

        // When/Then
        mockMvc.perform(get("/api/v1.0/blogsite/user/search")
                        .param("keyword", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @DisplayName("Should return empty list when no users found in search")
    void shouldReturnEmptyListWhenNoUsersFound() throws Exception {
        // Given
        when(userService.searchUsers("nonexistent")).thenReturn(List.of());

        // When/Then
        mockMvc.perform(get("/api/v1.0/blogsite/user/search")
                        .param("keyword", "nonexistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(0));
    }

    @Test
    @DisplayName("Should handle registration with validation errors")
    void shouldHandleRegistrationWithValidationErrors() throws Exception {
        // Given
        UserRegistrationRequest request = UserRegistrationRequest.builder()
                .userName("")
                .userEmail("invalid-email")
                .password("short")
                .build();

        // When/Then - Should return 400 Bad Request
        mockMvc.perform(post("/api/v1.0/blogsite/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
