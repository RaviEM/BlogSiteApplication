package com.blogsite.blog_command_service.service.impl;


import com.blogsite.blog_command_service.repository.CategoryRepository;
import com.blogsite.blog_command_service.service.CategoryService;
import com.blogsite.blog_common.exception.ResourceNotFoundException;
import com.blogsite.blog_common.model.entity. Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
/**
 * Implementation of CategoryService.
 * Auto-creates categories based on user blog post requests.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Category getOrCreateCategory(String categoryName) {
        log.info("Getting or creating category: {}", categoryName);
        // Check if category already exists (case-insensitive)
        return categoryRepository.findByNameIgnoreCase(categoryName)
                .orElseGet(() -> {
                    log.info("Category '{}' not found, creating new one", categoryName);

                    Category newCategory = Category.builder()
                            .name(categoryName)
                            .description("Auto-created category")
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .isActive(true)
                            .postCount(0L)
                            .build();

                    Category saved = categoryRepository.save(newCategory);
                    log.info("Category created with ID: {}", saved.getCategoryId());

                    return saved;
                });
    }

    @Override
    @Transactional
    public void incrementPostCount(String categoryId, String postId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + categoryId));
        category.addPost(postId);
        category.setUpdatedAt(LocalDateTime.now());
        categoryRepository.save(category);

    }

    @Override
    @Transactional
    public void decrementPostCount(String categoryId, String postId) {

        categoryRepository.findById(categoryId).ifPresent(
                category -> {
                    category.removePost(postId);
                    category.setUpdatedAt(LocalDateTime.now());
                    categoryRepository.save(category);
                });
    }
}
