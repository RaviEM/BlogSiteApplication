package com.blogsite.blog_query_service.controller;


import com.blogsite.blog_common.model.dto.response.ApiResponse;
import com.blogsite.blog_common.model.dto.response.PagedResponse;
import com.blogsite.blog_query_service.dto.BlogSearchRequest;
import com.blogsite.blog_query_service.dto.BlogWithAuthorResponse;
import com.blogsite.blog_query_service.service.BlogQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations. Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok. RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate; import java.util.List;

@RestController
@RequestMapping("/api/v1.0/blogsite/blogs")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Blog Query", description = "APIs for searching and listing blog posts")
public class BlogQueryController {
    private final BlogQueryService blogQueryService;

    @GetMapping("/info/{category}")
    @Operation(summary = "Get blogs by category", description = "Retrieves blogs filtered by category")
    public ResponseEntity<ApiResponse<PagedResponse<BlogWithAuthorResponse>>> getBlogsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("GET /blogs/info/{} - page: {}, size: {}", category, page, size);

        PagedResponse<BlogWithAuthorResponse> response = blogQueryService.getBlogsByCategory (category, page, size);

        return ResponseEntity.ok(ApiResponse.success(response, "Blogs retrieved successfully"));
    }

    /**
     * Search blogs with category and date range using path parameters.
     * GET /api/v1.0/blogsite/blogs/get/{category}/{startDate}/{endDate}
     * <p>
     * Use "all" for category to search all categories.
     * Date format: yyyy-MM-dd
     */
    @GetMapping("/get/{category}/{startDate}/{endDate}")
    @Operation(summary = "Search blogs by category and date range",
            description = "Search blogs using path parameters. Use 'all' for category to include all categories.")
    public ResponseEntity<ApiResponse<PagedResponse<BlogWithAuthorResponse>>> searchBlogsByPath(
            @Parameter(description = "Category to filter by (use 'all' for all categories)")
            @PathVariable String category,
            @Parameter(description = "Start date (yyyy-MM-dd)")
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date (yyyy-MM-dd)")
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("GET /blogs/get/{}/{}/{} - page: {}, size: {}", category, startDate, endDate, page, size);

        String categoryFilter = "all".equalsIgnoreCase(category) ? null : category;
        BlogSearchRequest searchRequest = BlogSearchRequest.builder()
                .category(categoryFilter)
                .startDate(startDate)
                .endDate(endDate)
                .page(page)
                .size(size)
                .build();

        PagedResponse<BlogWithAuthorResponse> response = blogQueryService.searchBlogs(searchRequest);

        return ResponseEntity.ok(ApiResponse.success(response, "Search completed succesfully"));
    }

    @GetMapping("/{postId}")
    @Operation(summary = "Get blog by ID", description = "Retrieves a single blog post by its ID")
    public ResponseEntity<ApiResponse<BlogWithAuthorResponse>> getBlogById(
            @PathVariable String postId) {
        log.info("GET /blogs/{}", postId);

        BlogWithAuthorResponse response = blogQueryService.getBlogById(postId);
        return ResponseEntity.ok(ApiResponse.success(response, "Blog retrieved successfully"));
    }

    @GetMapping("/author/{authorId}")
    @Operation(summary = "Get blogs by author", description = "Retrieves all blogs by a specific author")
    public ResponseEntity<ApiResponse<PagedResponse<BlogWithAuthorResponse>>> getBlogsByAuthor(
            @PathVariable Long authorId,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        log.info("GET /blogs/author/{} - page: {}, size: {}", authorId, page, size);

        PagedResponse<BlogWithAuthorResponse> response = blogQueryService.getBlogsByAuthor(authorId, page, size);

        return ResponseEntity.ok(ApiResponse.success(response, "Blogs retrieved successfully"));
    }

    @GetMapping("/categories")
    @Operation(summary = "Get all categories", description = "Retrieves list of all available blog categories")
    public ResponseEntity<ApiResponse<List<String>>> getAllCategories() {
        log.info("GET /blogs/categories");

        List<String> categories = blogQueryService.getAllCategories();

        return ResponseEntity.ok(ApiResponse.success(categories, "Categories retrieved successfully"));
    }
}