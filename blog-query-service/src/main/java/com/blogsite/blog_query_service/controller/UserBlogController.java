package com.blogsite.blog_query_service.controller;


import com.blogsite.blog_query_service.dto.BlogWithAuthorResponse;
import com.blogsite.blog_query_service.service.BlogQueryService;
import com.blogsite.blog_common.model.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * REST Controller for user-specific blog query operations.
 * Endpoint: /api/v1.0/blogsite/user/getall
 */
@RestController
@RequestMapping("/api/v1.0/blogsite/user")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Blog Query", description = "APIs for fetching user's blog posts")
public class UserBlogController {
    private final BlogQueryService blogQueryService;

    /**
     * Get all blogs by author name.
     * GET /api/v1.0/blogsite/user/getall?authorName=
     */
    @GetMapping("/getall")
    @Operation(summary = "Get all blogs by author",
            description = "Fetches all the blogs of the user by author name")
    public ResponseEntity<ApiResponse<List<BlogWithAuthorResponse>>> getAllBlogsByAuthor(@Parameter(description = "Author name/email to filter by", required = true) @RequestParam String authorName) {
        log.info("GET /user/getall?authorName={}", authorName);
        List<BlogWithAuthorResponse> blogs = blogQueryService.getBlogsByAuthorName(authorName);
        return ResponseEntity.ok(ApiResponse.success(blogs, "Blogs retrieved successfully"));
    }
}
