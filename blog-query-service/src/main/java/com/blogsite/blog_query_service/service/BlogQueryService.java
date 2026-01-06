package com.blogsite.blog_query_service.service;


import com.blogsite.blog_query_service.dto.BlogSearchRequest;
import com.blogsite.blog_query_service.dto.BlogWithAuthorResponse;
import com.blogsite.blog_common.model.dto.response.PagedResponse;
import java.util.List;
/**
 * Service interface for blog query operations.
 * Read-only operations for the query side of CQRS.
 */
public interface BlogQueryService {
    /**
     * Get blogs by category.
     */
    PagedResponse<BlogWithAuthorResponse> getBlogsByCategory(String category, int page, int size);

    /**
     * Search blogs with multiple criteria (category, date range, etc.).
     */
    PagedResponse<BlogWithAuthorResponse> searchBlogs(BlogSearchRequest searchRequest);

    /**
     * Get a single blog by ID.
     */
    BlogWithAuthorResponse getBlogById(String postId);

    /**
     * Get blogs by author ID.
     */
    PagedResponse<BlogWithAuthorResponse> getBlogsByAuthor(Long authorId, int page, int size);

    /**
     * Get all blogs by author name/email.
     */
    List<BlogWithAuthorResponse> getBlogsByAuthorName(String authorName);

    /**
     * Get all available categories.
     */
    List<String> getAllCategories();
}