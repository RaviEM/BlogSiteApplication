package com.blogsite.blog_common.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
/**
 * DTO for admin response data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude. Include.NON_NULL) public class AdminResponse {
    private Long adminId;
    private String username;
    private String email;
    private Long userId;
    private LocalDateTime createdAt;
    private Boolean isActive;
}