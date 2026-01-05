package com.blogsite.blog_common.model.entity;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
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

@Document(collection = "blog_posts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogPost {
    @Id
    private String postId;
    @NotBlank(message = "Blog name is mandatory")
    @Size(min = 20, message = "Blog name should be minimum 20 characters")
    @Field("blog_name")
    @Indexed
    private String blogName;
    @NotBlank(message = "Content is mandatory")
    @Field("content")
    private String content;
    @NotBlank(message = "Category is mandatory")
    @Size(min = 20, message = "Category should be minimum 20 characters")
    @Field("category")
    @Indexed
    private String category;
    @Field("category_id")
    private String categoryId;
    @Field("author_id")
    @Indexed
    private Long authorId;
    @Field("author_name")
    private String authorName;
    @Field("author_email")
    private String authorEmail;
    @Field("comment_ids")
    @Builder.Default
    private List<String> commentIds = new ArrayList<>();
    @Field("tag_ids")
    @Builder.Default
    private List<String> tagIds = new ArrayList<>();
    @Field("like_ids")
    @Builder.Default
    private List<String> likeIds = new ArrayList<>();
    @Field("created_at")
    private LocalDateTime createdAt;
    @Field("updated_at")
    private LocalDateTime updatedAt;
    @Field("is_published")
    @Builder.Default
    private Boolean isPublished = true;
    @Field("view_count")
    @Builder.Default
    private Long viewCount = 0L;
    @Field("like_count")
    @Builder.Default
    private Long likeCount = 0L;
    @Field("comment_count")
    @Builder.Default
    private Long commentCount = 0L;
}