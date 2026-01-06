package com.blogsite.blog_query_service.repository;


import com.blogsite.blog_common.model.entity.BlogPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype. Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BlogPostQueryRepository extends MongoRepository<BlogPost, String> {

    Page<BlogPost> findByCategoryIgnoreCaseAndIsPublishedTrue(String category, Pageable pageable);

    /**
     * Find published posts by category and date range with pagination.
     */
    @Query("{ 'category': { $regex: ?0, $options: 'i' }, 'is_published': true, 'created_at': {$gte: ?1, $lte: ?2 } }")
    Page<BlogPost> findByCategoryAndDateRange(String category, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    /**
     * Find all published posts with pagination.
     */
    Page<BlogPost> findByIsPublishedTrue(Pageable pageable);

    Page<BlogPost> findByIsPublishedTrueAndCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    /**
     * Find posts by author ID with pagination.
     */
    Page<BlogPost> findByAuthorIdAndIsPublishedTrue(Long authorId, Pageable pageable);

    /**
     * Find all published posts by author name/email.
     * Uses case-insensitive matching for author name.
     * Note: This method uses partial matching. For exact matching, use the custom implementation.
     */
    @Query(value = "{ 'author_name': { $regex: ?0, $options: 'i' }, 'is_published': true }")
    List<BlogPost> findByAuthorNameAndIsPublishedTrue(String authorName);

    /**
     * Get all distinct categories.
     */
    @Query(value = "{ 'is_published': true }", fields = "{ 'category': 1}")
    List<BlogPost> findDistinctCategories();
}