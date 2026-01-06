package com.blogsite.blog_common.model.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
/**
 * DTO for blog post response data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude. Include.NON_NULL)
public class BlogPostResponse {
    private String postId;
    private String blogName;
    private String content;
    private String category;
    private String categoryId;
    private Long authorId;
    private String authorName;
    private List<String> tagIds;
    private List<String> tagNames;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isPublished;
    private Long viewCount;
    private Long likeCount;
    private Long commentCount;

}