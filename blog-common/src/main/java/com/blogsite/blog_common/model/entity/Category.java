package com.blogsite.blog_common.model.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 * Category entity representing a blog category in the Blog Site application.
 Stored in MongoDB database
/**
 * Category entity representing a blog category in the Blog Site application. * Stored in MongoDB database.
 * Used for organizing and filtering blog posts.
 */
@Document(collection = "categories")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    private String categoryId;
    @NotBlank (message = "Category name is mandatory")
    @Size(min = 20, message = "Category name should be minimum 20 characters") @Field("name")
    @Indexed (unique = true)
    private String name;
    @Field("description")
    private String description;
    /**
     * List of blog post IDs in this category.
     */
    @Field("post_ids")
    @Builder.Default
    private List<String> postIds = new ArrayList<>();

    @Field("created_at")
    private LocalDateTime createdAt;

    @Field("updated_at")
    private LocalDateTime updatedAt;

    @Field("is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Field("post_count")
    @Builder.Default
    private Long postCount = 0L;

    /**
     * Adds a post ID to the category.
     */
    public void addPost(String postId) {
        if (postIds == null) {
        }
        postIds = new ArrayList<>();
        if (!postIds.contains(postId)) {
            postIds.add(postId);
            postCount = (long) postIds.size();
        }
    }

        public void removePost(String postId){
            if (postIds != null) {
                postIds.remove(postId);
                postCount = (long) postIds.size();
            }
        }
    }