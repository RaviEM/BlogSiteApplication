package com.blogsite.blog_common.model.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * DTO for authentication response containing JWT token.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude. Include.NON_NULL)
public class AuthResponse {
    private String token;
    private String tokenType;
    private Long expiresIn;
    private UserResponse user;
 public static AuthResponse of (String token, Long expiresIn, UserResponse user) {
     return AuthResponse.builder()
             .token (token)
             .tokenType("Bearer")
             .expiresIn(expiresIn)
             .user(user)
             .build();
    }

}