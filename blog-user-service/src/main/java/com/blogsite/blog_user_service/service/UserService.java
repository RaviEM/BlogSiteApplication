package com.blogsite.blog_user_service.service;



import com.blogsite.blog_common.model. dto. request. LoginRequest;
import com.blogsite.blog_common.model. dto. request.UserRegistrationRequest;
import com.blogsite.blog_common.model. dto. response. AuthResponse;
import com.blogsite.blog_common.model. dto. response.UserResponse;
import java.util.List;
/**
 * Service interface for User operations.
 */
public interface UserService {
    /**
     * Register a new user.
     *
     * @param request User registration details
     * @return Created user response
     */
    UserResponse registerUser(UserRegistrationRequest request);

    /**
     * Authenticate user and return JWT token.
     *
     * @param request Login credentials
     * @return Authentication response with token
     */
    AuthResponse loginUser(LoginRequest request);

    /**
     * Get user by ID.
     *
     * @param userId User ID
     * @return User response
     */
    UserResponse getUserById(Long userId);

    /**
     * Get user by email.
     *
     * @param email User email
     * @return User response
     */
    UserResponse getUserByEmail(String email);

    /**
     * Get all active users.
     *
     * @return List of active users
     */
    List<UserResponse> getAllActiveUsers();

    /**
     * Deactivate user (soft delete).
     *
     * @param userId User ID to deactivate
     */
    void deactivateUser(Long userId);

    /**
     * Search users by username.
     *
     * @param keyword Search keyword
     * @return List of matching users
     */
    List<UserResponse> searchUsers(String keyword);
}