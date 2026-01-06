package com.blogsite.blog_user_service.mapper;

import com.blogsite.blog_common.model.dto.request.UserRegistrationRequest;
import com.blogsite.blog_common.model.dto.response.UserResponse;
import com.blogsite.blog_common.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserMapper Tests")
class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
    }

    @Test
    @DisplayName("Should map UserRegistrationRequest to User entity")
    void shouldMapRequestToEntity() {
        // Given
        UserRegistrationRequest request = UserRegistrationRequest.builder()
                .userName("testuser")
                .userEmail("test@example.com")
                .password("Password123")
                .build();

        // When
        User user = userMapper.toEntity(request);

        // Then
        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("Password123", user.getPassword());
        assertTrue(user.getIsActive());
    }

    @Test
    @DisplayName("Should map User entity to UserResponse")
    void shouldMapEntityToResponse() {
        // Given
        User user = User.builder()
                .userId(1L)
                .username("testuser")
                .email("test@example.com")
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .postIds(Arrays.asList("post1", "post2"))
                .build();

        // When
        UserResponse response = userMapper.toResponse(user);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getUserId());
        assertEquals("testuser", response.getUsername());
        assertEquals("test@example.com", response.getEmail());
        assertTrue(response.getIsActive());
        assertNotNull(response.getPostIds());
        assertEquals(2, response.getPostIds().size());
    }

    @Test
    @DisplayName("Should map list of Users to list of UserResponses")
    void shouldMapListOfUsersToResponses() {
        // Given
        User user1 = User.builder()
                .userId(1L)
                .username("user1")
                .email("user1@example.com")
                .isActive(true)
                .build();

        User user2 = User.builder()
                .userId(2L)
                .username("user2")
                .email("user2@example.com")
                .isActive(true)
                .build();

        List<User> users = Arrays.asList(user1, user2);

        // When
        List<UserResponse> responses = userMapper.toResponseList(users);

        // Then
        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals(1L, responses.get(0).getUserId());
        assertEquals(2L, responses.get(1).getUserId());
    }

    @Test
    @DisplayName("Should return empty list for empty input")
    void shouldReturnEmptyListForEmptyInput() {
        // When
        List<UserResponse> responses = userMapper.toResponseList(List.of());

        // Then
        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }

    @Test
    @DisplayName("Should handle null user in toResponse")
    void shouldHandleNullUser() {
        // When/Then
        assertThrows(NullPointerException.class, () -> userMapper.toResponse(null));
    }
}
