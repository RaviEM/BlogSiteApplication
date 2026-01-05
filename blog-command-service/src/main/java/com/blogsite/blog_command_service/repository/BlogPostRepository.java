package com.blogsite.blog_command_service.repository;


import com.blogsite.blog_common.model.entity.BlogPost;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework. stereotype.Repository;
import java.util.Optional;
/**
 * MongoDB Repository for BlogPost entity.
 * Handles CRUD operations for blog posts.
 */
@Repository
public interface BlogPostRepository extends MongoRepository<BlogPost, String> {

/**
 * Check if a post with the given blog name already exists for the author.
 */
boolean existsByBlogNameAndAuthorId(String blogName, Long authorId);
/**
 * Find a post by blog name and author ID.
 */
Optional<BlogPost> findByBlogNameAndAuthorId(String blogName, Long authorId);
}