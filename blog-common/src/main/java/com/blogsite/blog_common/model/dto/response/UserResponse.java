package com.blogsite.blog_common.model.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
/**
 * DTO for user response data.
 * Excludes sensitive information like password.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude. Include.NON_NULL)
public class UserResponse {
    private Long userId;
    private String username;
    private String email;
    private List<String> postIds;
    private LocalDateTime createdAt;
    private Boolean isActive;
}