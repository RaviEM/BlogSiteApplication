package com.blogsite.blog_command_service.controller;


import com.blogsite.blog_command_service.service.BlogPostService;
import com.blogsite.blog_command_service.service.JwtService;
import com.blogsite.blog_common.constant.ApiConstants;
import com.blogsite.blog_common.model.dto.request.BlogPostRequest;
import com.blogsite.blog_common.model.dto.response.ApiResponse;
import com.blogsite.blog_common.model.dto.response.BlogPostResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstants.BLOG_COMMAND_BASE)
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Blog Post Commands", description = "APIS for creating and deleting blog posts") @SecurityRequirement(name = "bearerAuth")
public class BlogPostController {
    private final BlogPostService blogPostService;
    private final JwtService jwtService;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @PostMapping(ApiConstants.BLOG_ADD + "/{blogName}")
    @Operation(summary = "Add a new blog post", description = "Creates a new blog post. Requires authentication.")
    public ResponseEntity<ApiResponse<BlogPostResponse>> addBlogPost(
            @PathVariable String blogName,
            @Valid @RequestBody BlogPostRequest request, HttpServletRequest httpRequest) {
        String token = extractToken(httpRequest);
        Long authorId = jwtService.extractUserId(token);
        String authorEmail = jwtService.extractEmail(token);
        String authorName = jwtService.extractUsername(token);

        if (authorName == null || authorName.isEmpty()) {
            authorName = authorEmail;
            log.warn("Username not found in token, falling back to email: {}", authorName);
        }

        log.info("POST /user/blogs/add/{} - Creating blog post for user: {} (ID: {})", blogName, authorName, authorId);
        if (request.getBlogName() == null || request.getBlogName().isEmpty()) {
            request.setBlogName(blogName);
        }

        BlogPostResponse response = blogPostService.createBlogPost (request, authorId, authorName, authorEmail);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, ApiConstants.BLOG_ADDED_SUCCESS));
    }

    /**
     * Delete a blog post by blog name.
     * DELETE /api/v1.0/blogsite/user/delete/{blogName}
     */
    @DeleteMapping(ApiConstants.BLOG_DELETE + "/{blogName}")
    @Operation(summary = "Delete a blog post", description = "Deletes a blog post by name. Only the author can delete.")
    public ResponseEntity<ApiResponse<Void>> deleteBlogPost(@PathVariable String blogName, HttpServletRequest httpRequest) {
        Long authorId = jwtService.extractUserId(extractToken(httpRequest));
        log.info("DELETE /user/delete/{) - by author: {}", blogName, authorId);
        blogPostService.deleteBlogPostByName(blogName, authorId);
        return ResponseEntity.ok(ApiResponse.success(ApiConstants.BLOG_DELETED_SUCCESS));
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        throw new IllegalArgumentException("Invalid or missing Authorization header");
    }
}