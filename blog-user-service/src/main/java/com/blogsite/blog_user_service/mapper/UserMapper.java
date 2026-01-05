package com.blogsite.blog_user_service.mapper;


import com.blogsite.blog_common.model.dto.request.UserRegistrationRequest;
import com.blogsite.blog_common.model.dto.response.UserResponse;
import com.blogsite.blog_common.model.entity.User;
import org.springframework. stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;
/**
 * Mapper class for converting between User entity and DTOS.
 */
@Component
public class UserMapper {
    /**
     * Convert UserRegistrationRequest to User entity.
     */
    public User toEntity (UserRegistrationRequest request) {
        return User.builder()
                .username(request.getUserName()).email(request.getUserEmail())
                .password (request.getPassword()) // Will be encoded in service .isActive(true)
                .build();
    }
    /**
     * Convert User entity to UserResponse DTO.
     */
    public UserResponse toResponse (User user) { return UserResponse.builder()
            .userId(user.getUserId()) .username(user.getUsername()) .email(user.getEmail())
            .postIds (user.getPostIds()) .createdAt(user.getCreatedAt()) .isActive(user.getIsActive())
            .build();
    }

    public List<UserResponse> toResponseList (List<User> users) { return users.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }
}