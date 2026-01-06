package com.blogsite.blog_query_service.service.impl;


import com.blogsite.blog_query_service.dto.BlogSearchRequest;
import com.blogsite.blog_query_service.dto.BlogWithAuthorResponse;
import com.blogsite.blog_query_service.repository.BlogPostQueryRepository;
import com.blogsite.blog_query_service.service.BlogQueryService;
import com.blogsite.blog_common.exception.ResourceNotFoundException;
import com.blogsite.blog_common.model.dto.response.PagedResponse;
import com.blogsite.blog_common.model.entity.BlogPost;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
    private final MongoTemplate mongoTemplate;

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

                        // First, verify database connection and collection
                        String databaseName = mongoTemplate.getDb().getName();
                        log.info("Connected to MongoDB database: {}", databaseName);
                        
                        // Check total documents in collection
                        long totalDocuments = mongoTemplate.getCollection("blog_posts").countDocuments();
                        log.info("Total documents in 'blog_posts' collection: {}", totalDocuments);
                        
                        // Check if there are any published blogs at all
                        Query allPublishedQuery = new Query();
                        allPublishedQuery.addCriteria(Criteria.where("is_published").is(true));
                        long totalPublished = mongoTemplate.count(allPublishedQuery, BlogPost.class);
                        log.info("Total published blogs in database: {}", totalPublished);
                        
                        // Get all unique author names for debugging
                        if (totalDocuments > 0) {
                            Query allDocsQuery = new Query();
                            allDocsQuery.fields().include("author_name", "is_published", "blog_name");
                            allDocsQuery.limit(10);
                            List<BlogPost> allDocs = mongoTemplate.find(allDocsQuery, BlogPost.class);
                            log.info("Sample documents (first 10):");
                            for (BlogPost doc : allDocs) {
                                log.info("  - author_name: '{}', is_published: {}, blog_name: '{}'", 
                                        doc.getAuthorName(), doc.getIsPublished(), doc.getBlogName());
                            }
                        }
                        
                        // Query by author name (try multiple approaches)
                        // First, let's query without filters to see what we have
                        Query testQuery = new Query();
                        testQuery.limit(1);
                        BlogPost sample = mongoTemplate.findOne(testQuery, BlogPost.class);
                        if (sample != null) {
                            log.info("Sample blog post found - author_name: '{}', is_published: {}, author_id: {}", 
                                    sample.getAuthorName(), sample.getIsPublished(), sample.getAuthorId());
                        }
                        
                        Query query = new Query();
                        
                        // Try exact case-insensitive match first
                        String cleanAuthorName = authorName.trim();
                        String escapedAuthorName = java.util.regex.Pattern.quote(cleanAuthorName);
                        query.addCriteria(Criteria.where("author_name")
                                .regex("^" + escapedAuthorName + "$", "i")); // Case-insensitive exact match
                        query.addCriteria(Criteria.where("is_published").is(true));
                        
                        // Log the actual query being executed
                        log.debug("MongoDB query: author_name regex: '^{}$' (case-insensitive), is_published: true", escapedAuthorName);
                        
                        List<BlogPost> blogs = mongoTemplate.find(query, BlogPost.class);
                        log.info("Found {} blogs for author name '{}' (exact match)", blogs.size(), authorName);
                        
                        // If no results, try without exact match (partial match)
                        if (blogs.isEmpty()) {
                            log.warn("No exact matches found, trying partial match...");
                            Query partialQuery = new Query();
                            partialQuery.addCriteria(Criteria.where("author_name")
                                    .regex(authorName.trim(), "i")); // Case-insensitive partial match
                            partialQuery.addCriteria(Criteria.where("is_published").is(true));
                            blogs = mongoTemplate.find(partialQuery, BlogPost.class);
                            log.info("Found {} blogs for author name '{}' (partial match)", blogs.size(), authorName);
                        }
                        
                        // If still no results, try without is_published filter to see all blogs by this author
                        if (blogs.isEmpty()) {
                            log.warn("No published blogs found, checking all blogs (including unpublished)...");
                            Query allQuery = new Query();
                            allQuery.addCriteria(Criteria.where("author_name")
                                    .regex("^" + escapedAuthorName + "$", "i"));
                            List<BlogPost> allBlogs = mongoTemplate.find(allQuery, BlogPost.class);
                            log.info("Found {} total blogs (published + unpublished) for author name '{}'", 
                                    allBlogs.size(), authorName);
                            
                            // Log sample author names from database for debugging
                            if (allBlogs.isEmpty()) {
                                Query sampleQuery = new Query();
                                sampleQuery.limit(5);
                                List<BlogPost> samples = mongoTemplate.find(sampleQuery, BlogPost.class);
                                log.warn("Sample author names in database: {}", 
                                        samples.stream()
                                                .map(BlogPost::getAuthorName)
                                                .collect(Collectors.toList()));
                            }
                        }

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