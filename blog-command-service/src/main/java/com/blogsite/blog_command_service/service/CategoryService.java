package com.blogsite.blog_command_service.service;

import com.blogsite.blog_common.model.entity.Category;

public interface CategoryService {
    Category getOrCreateCategory(String categoryName);

    void incrementPostCount(String categoryId, String postId);

    void decrementPostCount(String categoryId, String postId);
}
