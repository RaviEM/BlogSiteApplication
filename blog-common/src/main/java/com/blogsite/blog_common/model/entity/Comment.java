package com.blogsite.blog_common.model.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.LocalDateTime;
/**
 * Comment entity representing a comment on a blog post.
 * Stored in MongoDB database.
 * Links to Blog Post (postId) and User (authorId).
 */
@Document(collection = "comments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    private String commentId;
    @NotBlank(message = "Comment content is mandatory")
    @Field("content")
    private String content;
    /**
     * Reference to the author (User entity in MySQL).
     * Stores the userId from the User entity.
     */
    @Field("author_id")
    @Indexed
    private Long authorId;
    @Field("author_name")
    private String authorName;
    /**
     * Reference to the blog post this comment belongs to.
     */
    @Field("post_id")
    @Indexed
    private String postId;
    @Field("timestamp")
    private LocalDateTime timestamp;
    @Field("created_at")
    private LocalDateTime createdAt;
    @Field("updated_at")
    private LocalDateTime updatedAt;
    @Field("is_active")
    @Builder.Default
    private Boolean isActive = true;
/**
 * Optional parent comment ID for nested/reply comments.
 */
    @Field("parent_comment_id")
    private String parentCommentId;
}