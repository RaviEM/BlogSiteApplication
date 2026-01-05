package com.blogsite.blog_command_service.service;

import com.blogsite.blog_common.model.dto.request.BlogPostRequest;
import com.blogsite.blog_common.model.dto.response.BlogPostResponse;

public interface BlogPostService {

    BlogPostResponse createBlogPost(BlogPostRequest request, Long authorId, String authorName, String authorEmail);

    void deleteBlogPostByName(String blogName, Long authorId);
}
