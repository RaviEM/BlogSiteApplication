package com.blogsite.blog_command_service.service.impl;


import com.blogsite.blog_command_service.mapper.BlogPostMapper;
import com.blogsite.blog_command_service.repository.BlogPostRepository;
import com.blogsite.blog_command_service.service.BlogPostService;
import com.blogsite.blog_command_service.service.CategoryService;
import com.blogsite.blog_common.model.dto.response.BlogPostResponse;
import com.blogsite.blog_common.exception.DuplicateResourceException;
import com.blogsite.blog_common.exception.ResourceNotFoundException;
import com.blogsite.blog_common.exception.ValidationException;
import com.blogsite.blog_common.model.dto.request.BlogPostRequest;
import com.blogsite.blog_common.model.entity.BlogPost;
import com.blogsite.blog_common.model.entity.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction. annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlogPostServiceImpl implements BlogPostService {
    private final BlogPostRepository blogPostRepository;
    private final BlogPostMapper blogPostMapper;
    private final CategoryService categoryService;
    private static final int MIN_CONTENT_WORDS = 1000;
    private static final int MIN_BLOG_NAME_LENGTH = 20;
    private static final int MIN_CATEGORY_LENGTH = 20;

    @Override
    @Transactional
    public BlogPostResponse createBlogPost(BlogPostRequest request, Long authorId, String authorName, String authorEmail) {
        log.info("Creating blog post for author: {} (ID: {})", authorName, authorId);

        // Validate blog name length
        if (request.getBlogName() == null || request.getBlogName().length() < MIN_BLOG_NAME_LENGTH) {
            throw new ValidationException("Blog name must be at least " + MIN_BLOG_NAME_LENGTH + " characters");
        }

        // Validate category length
        if (request.getCategory() == null || request.getCategory().length() < MIN_CATEGORY_LENGTH) {
            throw new ValidationException("Category must be at least " + MIN_CATEGORY_LENGTH + " characters");
        }

        // Validate content word count
        validateContentWordCount(request.getContent());

        // Check for duplicate blog name by same author
        if (blogPostRepository.existsByBlogNameAndAuthorId(request.getBlogName(), authorId)) {
            throw new DuplicateResourceException("Blog post with this name already exists for this author");
        }
        // Get or create category (auto-creates if new)

        Category category = categoryService.getOrCreateCategory(request.getCategory());

        // Create blog post entity
        BlogPost blogPost = blogPostMapper.toEntity(request);
        blogPost.setCategoryId(category.getCategoryId());
        blogPost.setAuthorId(authorId);
        blogPost.setAuthorName(authorName);
        blogPost.setAuthorEmail(authorEmail);
        blogPost.setCreatedAt(LocalDateTime.now());
        blogPost.setUpdatedAt(LocalDateTime.now());
        blogPost.setIsPublished(true);
        blogPost.setViewCount(0L);
        blogPost.setLikeCount(0L);
        blogPost.setCommentCount(0L);

        // Save to MongoDB
        BlogPost savedPost = blogPostRepository.save(blogPost);
        log.info("Blog post created with ID: {}", savedPost.getPostId());

        // Update category post count
        categoryService.incrementPostCount(category.getCategoryId(), savedPost.getPostId());
        return blogPostMapper.toResponse(savedPost);
    }

    @Override
    @Transactional
    public void deleteBlogPostByName(String blogName, Long authorId) {
        log.info("Deleting blog post by name: {} by author: {}", blogName, authorId);

        // Find the post by name and author
        BlogPost blogPost = blogPostRepository.findByBlogNameAndAuthorId(blogName, authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Blog post not found with name: " + blogName));

        // Decrement category post count
        if (blogPost.getCategoryId() != null) {
            categoryService.decrementPostCount(blogPost.getCategoryId(), blogPost.getPostId());
        }

        // Hard delete
        blogPostRepository.delete(blogPost);
        log.info("Blog post deleted by name: {}", blogName);
    }

    private void validateContentWordCount(String content) {
        if (content == null || content.isBlank()) {
            throw new ValidationException("Content is mandatory");
        }
        String[] words = content.trim().split("\\\s+");
        if (words.length < MIN_CONTENT_WORDS) {
            throw new ValidationException(
                    "Content must have at least " + MIN_CONTENT_WORDS + "words. Current word count: " + words.length
            );
        }
    }
}