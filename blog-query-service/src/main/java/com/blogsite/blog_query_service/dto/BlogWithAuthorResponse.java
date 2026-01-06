package com.blogsite.blog_query_service.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
/**
 * DTO for blog post response with author details.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude. Include.NON_NULL)
public class BlogWithAuthorResponse {
    // Blog details
    private String postId;
    private String blogName;
    private String article;
    private String category;
    private List<String> tagIds;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer wordCount;
    // Author details
    private AuthorDetails author;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthorDetails {
        private String authorName;
        private String authorEmail;
    }
}