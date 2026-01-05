package com.blogsite.blog_command_service.repository;

import com.blogsite.blog_common.model.entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository; import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {


    Optional<Category> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
    /**
     * Find all active categories.
     */
    List<Category> findByIsActiveTrue();
}