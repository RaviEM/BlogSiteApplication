package com.blogsite.blog_common.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {

    @NotBlank(message = "Comment content is mandatory")
    private String content;

    @NotBlank(message = "Post ID is mandatory")
    private String postId;

    private String parentCommentId;
}
