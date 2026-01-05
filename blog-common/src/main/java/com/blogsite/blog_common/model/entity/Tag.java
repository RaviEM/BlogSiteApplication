package com.blogsite.blog_common.model.entity;


import jakarta.validation.constraints.NotBlank;
import lombok. AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 * Tag entity representing a tag/label for blog posts.
 * Stored in MongoDB database.
 * Used for tagging and filtering blog posts.
 */
@Document(collection = "tags")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
    @Id
    private String tagId;
    @NotBlank (message = "Tag name is mandatory") @Field("name")
    @Indexed(unique = true)
    private String name;
    /**
     * List of blog post IDs associated with this tag. */
    @Field("post_ids")
    @Builder.Default
    private List<String> postIds = new ArrayList<>();
    @Field("created_at")
    private LocalDateTime createdAt;
    @Field("updated_at")
    private LocalDateTime updatedAt;
    @Field("post_count")
    @Builder.Default
    private Long postCount = 0L;
    /**
     * Adds a post ID to the tag.
     */
    public void addPost(String postId) {
    }
    /**
     if (postIds == null) {
     }
     postIds = new ArrayList<>();
     if (!postIds.contains(postId)) { postIds.add(postId);
     postCount = (long) postIds.size();
     * Removes a post ID from the tag.
     */
    public void removePost (String postId) {
        if (postIds != null) {
            postIds.remove(postId);
            postCount = (long) postIds.size();
        }
    }
}