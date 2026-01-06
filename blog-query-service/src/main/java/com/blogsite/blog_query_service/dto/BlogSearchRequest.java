package com.blogsite.blog_query_service.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogSearchRequest {
    /**
     * Category to filter by (optional).
     */
    private String category;
    /**
     * Start date for filtering blogs created after this date (optional).
     */
    private LocalDate startDate;
    /**
     * End date for filtering blogs created before this date (optional).
     */
    private LocalDate endDate;
    /**
     * Keyword to search in blog name (optional).
     */
    private String keyword;
    /**
     * Author ID to filter by (optional).
     */
    private Long authorId;
    /**
     * Page number (0-indexed).
     */
    @Builder.Default
    private int page = 0;
    /**
     * Page size.
     */
    @Builder.Default
    private int size = 10;
}