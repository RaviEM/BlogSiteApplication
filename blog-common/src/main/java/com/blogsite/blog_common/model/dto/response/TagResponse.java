package com.blogsite.blog_common.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
/**
 * DTO for tag response data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude. Include.NON_NULL)
public class TagResponse {
    private String tagId;
    private String name;
    private Long postCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}