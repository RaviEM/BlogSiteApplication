package com.blogsite.blog_common.model.dto.request;

import com.fasterxml.jackson. annotation.JsonProperty; import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok. AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok. NoArgsConstructor;
import java.util.List;
/**
 * DTO for creating or updating a blog post.
 * Contains validation rules as per business requirements.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogPostRequest {
    @NotBlank (message = "Blog name is mandatory")
    private String blogName; @Size(min = 20, message = "Blog name should be minimum 20 characters")
    @JsonProperty("article")
    @NotBlank (message = "Article is mandatory")
    private String content;
    @NotBlank (message = "Category is mandatory")
    @Size(min = 20, message = "Category should be minimum 20 characters")
    private String category;
    private List<String> tagIds;
}