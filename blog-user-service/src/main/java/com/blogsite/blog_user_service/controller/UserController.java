package com.blogsite.blog_user_service.controller;


import com.blogsite.blog_common.constant.ApiConstants;
import com.blogsite.blog_common.model.dto.request.LoginRequest;
import com.blogsite.blog_common.model.dto.request.UserRegistrationRequest;
import com.blogsite.blog_common.model.dto.response.ApiResponse;
import com.blogsite.blog_common.model.dto.response. AuthResponse;
import com.blogsite.blog_common.model.dto.response.UserResponse;
import com.blogsite.blog_user_service.service.UserService;
import io.swagger.v3.oas. annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation. Valid;
import lombok. RequiredArgsConstructor;
import lombok. extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 *REST Controller for User operations.
 * Handles user registration, authentication, and management.
 */
@RestController
@RequestMapping (ApiConstants.USER_BASE)
@RequiredArgsConstructor
@Slf4j
@Tag (name = "User Management", description = "APIs for user registration and authentication") public class UserController {
    private final UserService userService;

    @PostMapping(ApiConstants.USER_REGISTER)
    @Operation(summary = "Register a new user", description = "Creates a new user account with username, email, and password")
    public ResponseEntity<ApiResponse<UserResponse>> registerUser(
            @Valid @RequestBody UserRegistrationRequest request) {
        log.info("Received registration request for email: {}", request.getUserEmail());

        UserResponse response = userService.registerUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, ApiConstants.USER_REGISTERED_SUCCESS));
    }

    @PostMapping(ApiConstants.USER_LOGIN)
    @Operation(summary = "User login", description = "Authenticates user and returns JWT token")
    public ResponseEntity<ApiResponse<AuthResponse>> loginUser(
            @Valid @RequestBody LoginRequest request) {
        log.info("Received login request for email: {}", request.getUserEmail());

        AuthResponse response = userService.loginUser(request);

        return ResponseEntity.ok(ApiResponse.success(response, ApiConstants.USER_LOGIN_SUCCESS));
    }


    @GetMapping(ApiConstants.USER_GET_ALL)
    @Operation(summary = "Get all users", description = "Retrieves all active users")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        log.info("Received request to get all users");

        List<UserResponse> users = userService.getAllActiveUsers();

        return ResponseEntity.ok(ApiResponse.success(users, "Users retrieved successfully"));
    }

    // Use a numeric-only path variable to avoid matching reserved paths like "register"


    @GetMapping("/{userId:\\d+}")
    @Operation(summary = "Get user by ID", description = "Retrieves user details by user ID")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long userId) {
        log.info("Received request to get user with ID: {}", userId);
        UserResponse user = userService.getUserById(userId);

        return ResponseEntity.ok(ApiResponse.success(user, "User retrieved successfully"));
    }

    // Constrain userId to digits to prevent accidental matches (e.g., "register")
    @DeleteMapping(ApiConstants.USER_DELETE + "/{userId:\\d+}")
    @Operation(summary = "Delete user", description = "Deactivates a user account (soft delete)")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long userId) {
        log.info("Received request to delete user with ID: {}", userId);

        userService.deactivateUser(userId);

        return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
    }

    @GetMapping("/search")
    @Operation(summary = "Search users", description = "Searches users by username keyword")
    public ResponseEntity<ApiResponse<List<UserResponse>>> searchUsers(
            @RequestParam String keyword) {
        log.info("Received search request with keyword: {}", keyword);
        List<UserResponse> users = userService.searchUsers(keyword);
        return ResponseEntity.ok(ApiResponse.success(users, "Search completed successfully"));
    }
}