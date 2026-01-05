package com.blogsite.blog_command_service.repository;

import com.blogsite.blog_common.model.entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository; import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    /**
     * Find category by name (case-insensitive).
     /*
     Optional<Category> findByNameIgnoreCase(String name);
     /**
     * Check if category with name exists (case-insensitive). */
    boolean existsByNameIgnoreCase(String name);
    /**
     * Find all active categories.
     */
    List<Category> findByIsActiveTrue();
}