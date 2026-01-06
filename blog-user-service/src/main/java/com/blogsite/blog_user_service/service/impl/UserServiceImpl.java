package com.blogsite.blog_user_service.service.impl;


import com.blogsite.blog_common.exception. DuplicateResourceException;
import com.blogsite.blog_common.exception.ResourceNotFoundException;
import com.blogsite.blog_common.exception.UnauthorizedException;
import com.blogsite.blog_common.exception.ValidationException;
import com.blogsite.blog_common.model.dto.request.LoginRequest;
import com.blogsite.blog_common.model.dto.request.UserRegistrationRequest;
import com.blogsite.blog_common.model.dto.response. AuthResponse;
import com.blogsite.blog_common.model.dto.response.UserResponse;
import com.blogsite.blog_common.model.entity.User;
import com.blogsite.blog_common.util.ValidationUtil;
import com.blogsite.blog_user_service.mapper.UserMapper;
import com.blogsite.blog_user_service.repository.UserRepository;
import com.blogsite.blog_user_service.service.JwtService;
import com.blogsite.blog_user_service.service.UserService;
import lombok. RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * Implementation of UserService.
 * Handles user registration, authentication, and management. */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public UserResponse registerUser(UserRegistrationRequest request) {
        log.info("Registering new user with email: {}", request.getUserEmail());
        // Validate request
        validateRegistrationRequest(request);
        // Check for duplicate email
        if (userRepository.existsByEmail(request.getUserEmail())) {
            throw new DuplicateResourceException("User", "email", request.getUserEmail());
        }
        // Check for duplicate username
        if (userRepository.existsByUsername(request.getUserName())) {
            throw new DuplicateResourceException("User", "username", request.getUserName());
        }
        // Create user entity
        User user = userMapper.toEntity(request);
        // Encode password
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        // Save user
        User savedUser = userRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getUserId());
        return userMapper.toResponse(savedUser);
    }

    @Override
    public AuthResponse loginUser(LoginRequest request) {
        log.info("Login attempt for email: {}", request.getUserEmail());
        // Find user by email
        User user = userRepository.findActiveUserByEmail(request.getUserEmail())
                .orElseThrow(UnauthorizedException::invalidCredentials);
        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Invalid password attempt for email: {}", request.getUserEmail());
            throw UnauthorizedException.invalidCredentials();
        }

        // Generate JWT token (includes username for blog author name)
        String token = jwtService.generateToken(user.getEmail(), user.getUserId(), user.getUsername());
        log.info("User logged in successfully: {}", user.getEmail());
        return AuthResponse.of(token, jwtService.getExpirationTime(), userMapper.toResponse(user));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long userId) {
        log.debug("Fetching user by ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {
        log.debug("Fetching user by email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllActiveUsers() {
        log.debug("Fetching all active users");
        List<User> users = userRepository.findAllActiveUsers();
        return userMapper.toResponseList(users);
    }

    @Override
    public void deactivateUser(Long userId) {
        log.info("Deactivating user with ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        user.setIsActive(false);
        userRepository.save(user);
        log.info("User deactivated successfully: ()", userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> searchUsers(String keyword) {
        log.debug("Searching users with keyword: ()", keyword);
        List<User> users = userRepository.searchByUsername(keyword);
        return userMapper.toResponseList(users);
    }


    /**
     * Validate user registration request.
     */
    private void validateRegistrationRequest(UserRegistrationRequest request) {
        ValidationException errors = new ValidationException("Validation failed");
// Validate email format
        if (!ValidationUtil.isValidEmail(request.getUserEmail())) {
            errors.addError("userEmail", "Email must contain '@' and end with '.com'");
        }
// Validate password
        if (!ValidationUtil.isValidPassword(request.getPassword())) {
            errors.addError("password", "Password must be at least 8 characters and include a letter and a digit");
        }
// Validate username
        if (request.getUserName() == null || request.getUserName().isBlank()) {
            errors.addError("userName", "Username is mandatory");
            if (errors.hasError()) {
                throw errors;
            }
        }
    }
}
