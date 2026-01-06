package com.blogsite.blog_user_service.service.impl;

import com.blogsite.blog_common.exception.DuplicateResourceException;
import com.blogsite.blog_common.exception.ResourceNotFoundException;
import com.blogsite.blog_common.exception.UnauthorizedException;
import com.blogsite.blog_common.exception.ValidationException;
import com.blogsite.blog_common.model.dto.request.LoginRequest;
import com.blogsite.blog_common.model.dto.request.UserRegistrationRequest;
import com.blogsite.blog_common.model.dto.response.AuthResponse;
import com.blogsite.blog_common.model.dto.response.UserResponse;
import com.blogsite.blog_common.model.entity.User;
import com.blogsite.blog_user_service.mapper.UserMapper;
import com.blogsite.blog_user_service.repository.UserRepository;
import com.blogsite.blog_user_service.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserServiceImpl Tests")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRegistrationRequest registrationRequest;
    private User user;
    private UserResponse userResponse;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        registrationRequest = UserRegistrationRequest.builder()
                .userName("testuser")
                .userEmail("test@example.com")
                .password("Password123")
                .build();

        user = User.builder()
                .userId(1L)
                .username("testuser")
                .email("test@example.com")
                .password("encodedPassword")
                .isActive(true)
                .build();

        userResponse = UserResponse.builder()
                .userId(1L)
                .username("testuser")
                .email("test@example.com")
                .isActive(true)
                .build();

        loginRequest = LoginRequest.builder()
                .userEmail("test@example.com")
                .password("Password123")
                .build();
    }

    @Nested
    @DisplayName("Register User Tests")
    class RegisterUserTests {
        @Test
        @DisplayName("Should register user successfully")
        void shouldRegisterUserSuccessfully() {
            // Given
            when(userRepository.existsByEmail(anyString())).thenReturn(false);
            when(userRepository.existsByUsername(anyString())).thenReturn(false);
            when(userMapper.toEntity(any(UserRegistrationRequest.class))).thenReturn(user);
            when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
            when(userRepository.save(any(User.class))).thenReturn(user);
            when(userMapper.toResponse(any(User.class))).thenReturn(userResponse);

            // When
            UserResponse result = userService.registerUser(registrationRequest);

            // Then
            assertNotNull(result);
            assertEquals("testuser", result.getUsername());
            assertEquals("test@example.com", result.getEmail());
            verify(userRepository).existsByEmail("test@example.com");
            verify(userRepository).existsByUsername("testuser");
            verify(passwordEncoder).encode("Password123");
            verify(userRepository).save(any(User.class));
        }

        @Test
        @DisplayName("Should throw DuplicateResourceException when email already exists")
        void shouldThrowExceptionWhenEmailExists() {
            // Given
            when(userRepository.existsByEmail(anyString())).thenReturn(true);

            // When/Then
            assertThrows(DuplicateResourceException.class, () -> userService.registerUser(registrationRequest));
            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Should throw DuplicateResourceException when username already exists")
        void shouldThrowExceptionWhenUsernameExists() {
            // Given
            when(userRepository.existsByEmail(anyString())).thenReturn(false);
            when(userRepository.existsByUsername(anyString())).thenReturn(true);

            // When/Then
            assertThrows(DuplicateResourceException.class, () -> userService.registerUser(registrationRequest));
            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Should throw ValidationException for invalid email")
        void shouldThrowExceptionForInvalidEmail() {
            // Given
            registrationRequest.setUserEmail("invalid-email");

            // When/Then
            assertThrows(ValidationException.class, () -> userService.registerUser(registrationRequest));
            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Should throw ValidationException for invalid password")
        void shouldThrowExceptionForInvalidPassword() {
            // Given
            registrationRequest.setPassword("short");

            // When/Then
            assertThrows(ValidationException.class, () -> userService.registerUser(registrationRequest));
            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Should throw ValidationException for password without digits")
        void shouldThrowExceptionForPasswordWithoutDigits() {
            // Given
            registrationRequest.setPassword("PasswordOnly");

            // When/Then
            assertThrows(ValidationException.class, () -> userService.registerUser(registrationRequest));
            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Should throw ValidationException for password without letters")
        void shouldThrowExceptionForPasswordWithoutLetters() {
            // Given
            registrationRequest.setPassword("12345678");

            // When/Then
            assertThrows(ValidationException.class, () -> userService.registerUser(registrationRequest));
            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Should throw ValidationException for null password")
        void shouldThrowExceptionForNullPassword() {
            // Given
            registrationRequest.setPassword(null);

            // When/Then
            assertThrows(ValidationException.class, () -> userService.registerUser(registrationRequest));
            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Should throw ValidationException for blank username")
        void shouldThrowExceptionForBlankUsername() {
            // Given
            registrationRequest.setUserName("");

            // When/Then
            assertThrows(ValidationException.class, () -> userService.registerUser(registrationRequest));
            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Should throw ValidationException for null username")
        void shouldThrowExceptionForNullUsername() {
            // Given
            registrationRequest.setUserName(null);

            // When/Then
            assertThrows(ValidationException.class, () -> userService.registerUser(registrationRequest));
            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Should throw ValidationException for null request")
        void shouldThrowExceptionForNullRequest() {
            // When/Then
            assertThrows(ValidationException.class, () -> userService.registerUser(null));
            verify(userRepository, never()).save(any(User.class));
        }
    }

    @Nested
    @DisplayName("Login User Tests")
    class LoginUserTests {
        @Test
        @DisplayName("Should login user successfully")
        void shouldLoginUserSuccessfully() {
            // Given
            when(userRepository.findActiveUserByEmail(anyString())).thenReturn(Optional.of(user));
            when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
            when(jwtService.generateToken(anyString(), anyLong(), anyString())).thenReturn("jwt-token");
            when(jwtService.getExpirationTime()).thenReturn(3600000L);
            when(userMapper.toResponse(any(User.class))).thenReturn(userResponse);

            // When
            AuthResponse result = userService.loginUser(loginRequest);

            // Then
            assertNotNull(result);
            assertEquals("jwt-token", result.getToken());
            verify(userRepository).findActiveUserByEmail("test@example.com");
            verify(passwordEncoder).matches("Password123", "encodedPassword");
            verify(jwtService).generateToken("test@example.com", 1L, "testuser");
        }

        @Test
        @DisplayName("Should throw UnauthorizedException when user not found")
        void shouldThrowExceptionWhenUserNotFound() {
            // Given
            when(userRepository.findActiveUserByEmail(anyString())).thenReturn(Optional.empty());

            // When/Then
            assertThrows(UnauthorizedException.class, () -> userService.loginUser(loginRequest));
            verify(passwordEncoder, never()).matches(anyString(), anyString());
        }

        @Test
        @DisplayName("Should throw UnauthorizedException when password is incorrect")
        void shouldThrowExceptionWhenPasswordIncorrect() {
            // Given
            when(userRepository.findActiveUserByEmail(anyString())).thenReturn(Optional.of(user));
            when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

            // When/Then
            assertThrows(UnauthorizedException.class, () -> userService.loginUser(loginRequest));
            verify(jwtService, never()).generateToken(anyString(), anyLong(), anyString());
        }
    }

    @Nested
    @DisplayName("Get User By ID Tests")
    class GetUserByIdTests {
        @Test
        @DisplayName("Should return user when found")
        void shouldReturnUserWhenFound() {
            // Given
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(userMapper.toResponse(any(User.class))).thenReturn(userResponse);

            // When
            UserResponse result = userService.getUserById(1L);

            // Then
            assertNotNull(result);
            assertEquals(1L, result.getUserId());
            verify(userRepository).findById(1L);
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when user not found")
        void shouldThrowExceptionWhenUserNotFound() {
            // Given
            when(userRepository.findById(1L)).thenReturn(Optional.empty());

            // When/Then
            assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(1L));
        }
    }

    @Nested
    @DisplayName("Get User By Email Tests")
    class GetUserByEmailTests {
        @Test
        @DisplayName("Should return user when found")
        void shouldReturnUserWhenFound() {
            // Given
            when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
            when(userMapper.toResponse(any(User.class))).thenReturn(userResponse);

            // When
            UserResponse result = userService.getUserByEmail("test@example.com");

            // Then
            assertNotNull(result);
            assertEquals("test@example.com", result.getEmail());
            verify(userRepository).findByEmail("test@example.com");
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when user not found")
        void shouldThrowExceptionWhenUserNotFound() {
            // Given
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

            // When/Then
            assertThrows(ResourceNotFoundException.class, () -> userService.getUserByEmail("test@example.com"));
        }
    }

    @Nested
    @DisplayName("Get All Active Users Tests")
    class GetAllActiveUsersTests {
        @Test
        @DisplayName("Should return list of active users")
        void shouldReturnListOfActiveUsers() {
            // Given
            User user2 = User.builder()
                    .userId(2L)
                    .username("user2")
                    .email("user2@example.com")
                    .isActive(true)
                    .build();
            List<User> users = Arrays.asList(user, user2);
            when(userRepository.findAllActiveUsers()).thenReturn(users);
            when(userMapper.toResponseList(users)).thenReturn(Arrays.asList(userResponse, userResponse));

            // When
            List<UserResponse> result = userService.getAllActiveUsers();

            // Then
            assertNotNull(result);
            assertEquals(2, result.size());
            verify(userRepository).findAllActiveUsers();
        }

        @Test
        @DisplayName("Should return empty list when no active users")
        void shouldReturnEmptyListWhenNoActiveUsers() {
            // Given
            when(userRepository.findAllActiveUsers()).thenReturn(List.of());
            when(userMapper.toResponseList(anyList())).thenReturn(List.of());

            // When
            List<UserResponse> result = userService.getAllActiveUsers();

            // Then
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("Deactivate User Tests")
    class DeactivateUserTests {
        @Test
        @DisplayName("Should deactivate user successfully")
        void shouldDeactivateUserSuccessfully() {
            // Given
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(userRepository.save(any(User.class))).thenReturn(user);

            // When
            userService.deactivateUser(1L);

            // Then
            assertFalse(user.getIsActive());
            verify(userRepository).findById(1L);
            verify(userRepository).save(user);
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when user not found")
        void shouldThrowExceptionWhenUserNotFound() {
            // Given
            when(userRepository.findById(1L)).thenReturn(Optional.empty());

            // When/Then
            assertThrows(ResourceNotFoundException.class, () -> userService.deactivateUser(1L));
            verify(userRepository, never()).save(any(User.class));
        }
    }

    @Nested
    @DisplayName("Search Users Tests")
    class SearchUsersTests {
        @Test
        @DisplayName("Should return list of users matching keyword")
        void shouldReturnMatchingUsers() {
            // Given
            List<User> users = Arrays.asList(user);
            when(userRepository.searchByUsername("test")).thenReturn(users);
            when(userMapper.toResponseList(users)).thenReturn(Arrays.asList(userResponse));

            // When
            List<UserResponse> result = userService.searchUsers("test");

            // Then
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(userRepository).searchByUsername("test");
        }

        @Test
        @DisplayName("Should return empty list when no users match")
        void shouldReturnEmptyListWhenNoMatch() {
            // Given
            when(userRepository.searchByUsername(anyString())).thenReturn(List.of());
            when(userMapper.toResponseList(anyList())).thenReturn(List.of());

            // When
            List<UserResponse> result = userService.searchUsers("nonexistent");

            // Then
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should handle null keyword in search")
        void shouldHandleNullKeyword() {
            // Given
            when(userRepository.searchByUsername(null)).thenReturn(List.of());
            when(userMapper.toResponseList(anyList())).thenReturn(List.of());

            // When
            List<UserResponse> result = userService.searchUsers(null);

            // Then
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should handle empty keyword in search")
        void shouldHandleEmptyKeyword() {
            // Given
            when(userRepository.searchByUsername("")).thenReturn(List.of());
            when(userMapper.toResponseList(anyList())).thenReturn(List.of());

            // When
            List<UserResponse> result = userService.searchUsers("");

            // Then
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }
}
