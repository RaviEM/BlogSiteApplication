package com.blogsite.blog_query_service.service.impl;


import com.blogsite.blog_query_service.dto.BlogSearchRequest;
import com.blogsite.blog_query_service.dto.BlogWithAuthorResponse;
import com.blogsite.blog_query_service.repository. BlogPostQueryRepository;
import com.blogsite.blog_query_service.service.BlogQueryService;
import com.blogsite.blog_common.exception. ResourceNotFoundException;
import com.blogsite.blog_common.model. dto. response.PagedResponse;
import com.blogsite.blog_common.model.entity. BlogPost;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain. Page;
import org.springframework.data.domain. PageRequest;
import org.springframework.data.domain. Pageable;
import org.springframework.data.domain. Sort;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
/**
 * Implementation of BlogQueryService.
 * Handles read operations for blogs.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BlogQueryServiceImpl implements BlogQueryService {
    private final BlogPostQueryRepository blogPostRepository;

    @Override
    public PagedResponse<BlogWithAuthorResponse> getBlogsByCategory(String category, int page, int size) {
        log.info("Fetching blogs by category: {} - page: {}, size: {}", category, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<BlogPost> blogPage = blogPostRepository.findByCategoryIgnoreCaseAndIsPublishedTrue(category, pageable);
        return toPagedResponse(blogPage);
    }

    @Override
    public PagedResponse<BlogWithAuthorResponse> searchBlogs(BlogSearchRequest searchRequest) {
        log.info("Searching blogs with criteria: {}", searchRequest);
        Pageable pageable = PageRequest.of(
                searchRequest.getPage(),
                searchRequest.getSize(),
                Sort.by(Sort.Direction.DESC, "createdAt")
        );
        Page<BlogPost> blogPage;

        // Determine which query to use based on provided criteria
        if (searchRequest.getCategory() != null && searchRequest.getStartDate() != null && searchRequest.getEndDate() != null) { // Category + Date Range
            LocalDateTime startDateTime = searchRequest.getStartDate().atStartOfDay();
            LocalDateTime endDateTime = searchRequest.getEndDate().atTime(LocalTime.MAX);
            blogPage = blogPostRepository.findByCategoryAndDateRange(
                    searchRequest.getCategory(),
                    startDateTime,
                    endDateTime, pageable
            );
        } else if (searchRequest.getCategory() != null) {
            // Category only
            blogPage = blogPostRepository.findByCategoryIgnoreCaseAndIsPublishedTrue(
                    searchRequest.getCategory(),
                    pageable
            );
        } else if (searchRequest.getStartDate() != null && searchRequest.getEndDate() != null) { // Date Range only
            LocalDateTime startDateTime = searchRequest.getStartDate().atStartOfDay();
            LocalDateTime endDateTime = searchRequest.getEndDate().atTime(LocalTime.MAX);
            blogPage = blogPostRepository.findByIsPublishedTrueAndCreatedAtBetween(
                    startDateTime, endDateTime, pageable
            );
        } else if (searchRequest.getAuthorId() != null) {
            // Author only
            blogPage = blogPostRepository.findByAuthorIdAndIsPublishedTrue(
                    searchRequest.getAuthorId(),
                    pageable
            );
        } else {
            blogPage = blogPostRepository.findByIsPublishedTrue(pageable);
        }
        return toPagedResponse(blogPage);
    }

// No specific criteria - return all

        @Override
        public BlogWithAuthorResponse getBlogById(String postId) {
            log.info("Fetching blog by ID: {}", postId);
            BlogPost blogPost = blogPostRepository.findById(postId)
                    .orElseThrow(() -> new ResourceNotFoundException("Blog post not found with ID: " + postId));

            if (!blogPost.getIsPublished()){
                throw new ResourceNotFoundException("Blog post not found with ID: " + postId);
            }

            return toResponse(blogPost);
        }

        @Override
            public PagedResponse <BlogWithAuthorResponse> getBlogsByAuthor(Long authorId, int page, int size) {
                log.info("Fetching blogs by author ID: {} - page: {}, size: {}", authorId, page, size);
                Pageable pageable
                        =
                        PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
                Page<BlogPost> blogPage = blogPostRepository.findByAuthorIdAndIsPublishedTrue(authorId, pageable);
                return toPagedResponse(blogPage);
    }

                    @Override
            public List<BlogWithAuthorResponse> getBlogsByAuthorName(String authorName) {
                        log.info("Fetching all blogs by author name: {}", authorName);
                        List<BlogPost> blogs = blogPostRepository.findByAuthorNameAndIsPublishedTrue(authorName);
                        return blogs.stream()
                                .map(this::toResponse)
                                .collect(Collectors.toList());
                    }

                @Override
                public List<String> getAllCategories() {
                    log.info("Fetching all categories");
                    return blogPostRepository.findDistinctCategories()
                            .stream()
                            .map(BlogPost::getCategory)
                            .distinct()
                            .sorted()
                            .collect (Collectors.toList());
                }

                private BlogWithAuthorResponse toResponse (BlogPost blogPost) {
                    return BlogWithAuthorResponse.builder().postId(blogPost.getPostId())
                            .blogName(blogPost.getBlogName()).article(blogPost.getContent()).category(blogPost.getCategory()).tagIds(blogPost.getTagIds())
                            .createdAt(blogPost.getCreatedAt())
                            .updatedAt(blogPost.getUpdatedAt())
                            .wordCount(calculateWordCount(blogPost.getContent()))
                            .author(BlogWithAuthorResponse.AuthorDetails.builder()
                                    .authorName(blogPost.getAuthorName())
                                    .authorEmail(blogPost.getAuthorEmail())
                                    .build())
                                    .build();
                }

                private Integer calculateWordCount(String content) {
                    if (content == null || content.isBlank()) {
                        return 0;
                    }
                    String plainText = content. replaceAll("<[^>]*>", "").trim();
                    if (plainText.isEmpty()) {
                        return 0;
                    }
                    return plainText.split("\\s+").length;
                }
/**
 * Convert Page<BlogPost> to PagedResponse.
 */
                private PagedResponse<BlogWithAuthorResponse> toPagedResponse(Page<BlogPost> page) {
                    List<BlogWithAuthorResponse> content = page.getContent()
                            .stream()
                            .map(this::toResponse)
                            .collect(Collectors.toList());

                    return PagedResponse.<BlogWithAuthorResponse>builder()
                            .content(content)
                            .page(page.getNumber())
                            .size(page.getSize())
                            .totalElements(page.getTotalElements()).totalPages(page.getTotalPages())
                            .last(page.isLast())
                            .build();
                }
}