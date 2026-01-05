package com.blogsite.blog_command_service.mapper;


import com.blogsite.blog_common.model.dto.request.BlogPostRequest;
import com.blogsite.blog_common.model. dto. response.BlogPostResponse;
import com.blogsite.blog_common.model.entity. BlogPost;
import org.springframework. stereotype. Component;
import java.util.ArrayList;
/**
 * Mapper for converting between Blog Post entity and DTOS. */
@Component
public class BlogPostMapper {
    /**
     * Convert Blog PostRequest to BlogPost entity.
     */
    public BlogPost toEntity (BlogPostRequest request) {
        if (request == null) {
            return null;
        }
        return BlogPost.builder()
                .blogName (request.getBlogName())
                .content(request.getContent())
                .category(request.getCategory())
                .tagIds (request.getTagIds() != null ? request.getTagIds(): new ArrayList<>()) .commentIds (new ArrayList<>())
                .likeIds (new ArrayList<>())
                .build();
    }
    /**
     * Convert Blog Post entity to BlogPostResponse.
     */
    public BlogPostResponse toResponse (BlogPost entity) {
        if (entity == null) {
            return null;
        }
        return BlogPostResponse.builder()
                .postId(entity.getPostId())
                .blogName (entity.getBlogName())
                .content (entity.getContent())
                .category(entity.getCategory())
                .categoryId(entity.getCategoryId())
                .authorId(entity.getAuthorId())
                .authorName(entity.getAuthorName())
                .tagIds (entity.getTagIds())
                .createdAt (entity.getCreatedAt())
                .updatedAt (entity.getUpdatedAt())
                .isPublished (entity.getIsPublished())
                .viewCount (entity.getViewCount())
                . likeCount (entity.getLikeCount())
                .commentCount(entity.getCommentCount())
                .build();
    }
}