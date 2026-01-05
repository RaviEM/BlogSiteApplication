package com.blogsite.blog_common.constant;



public final class ApiConstants {
    private ApiConstants() {
        // Prevent instantiation
    }

    // API Version
    public static final String API_VERSION = "v1.0";
    public static final String BASE_PATH = "/api/" + API_VERSION + "/blogsite";
    // User Service Endpoints
    public static final String USER_BASE = BASE_PATH + "/user";
    public static final String USER_REGISTER = "/register";
    public static final String USER_LOGIN = "/login";
    public static final String USER_GET_ALL = "/getall";
    public static final String USER_DELETE = "/delete";
    // Blog Service Endpoints (Command - under /user)
    public static final String BLOG_COMMAND_BASE = USER_BASE;
    public static final String BLOG_ADD = "/blogs/add";
    public static final String BLOG_DELETE = "/delete";
    // Blog Service Endpoints (Query)
    public static final String BLOG_BASE = BASE_PATH + "/blog";
    public static final String BLOG_INFO = "/info";
    public static final String BLOG_GET = "/get";
    // Category Endpoints
    // /api/v1.0/blogsite/user // POST /user/blogs/add/{blogName} // DELETE /user/delete/{blogName}
    public static final String CATEGORY_BASE = BASE_PATH + "/categories";
    // Comment Endpoints
    public static final String COMMENT_BASE = BASE_PATH + "/comments";
    // Tag Endpoints
    public static final String TAG_BASE = BASE_PATH + "/tags";
    // Like Endpoints
    public static final String LIKE_BASE = BASE_PATH + "/likes";
    // Notification Endpoints
    public static final String NOTIFICATION_BASE = BASE_PATH + "/notifications";
    // Full Paths
    public static final String FULL_USER_REGISTER = USER_BASE + USER_REGISTER;
    public static final String FULL_USER_LOGIN = USER_BASE + USER_LOGIN;
    public static final String FULL_USER_GET_ALL = USER_BASE + USER_GET_ALL;
    public static final String FULL_BLOG_ADD = USER_BASE + BLOG_ADD;
    // Validation Constants
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MIN_TITLE_LENGTH = 20;
    public static final int MIN_BLOG_NAME_LENGTH = MIN_TITLE_LENGTH; // Alias for backward compatibility
    public static final int MIN_CATEGORY_LENGTH = 20;
    public static final int MIN_CATEGORY_NAME_LENGTH = MIN_CATEGORY_LENGTH; // Alias for backward compatibility
    public static final int MIN_ARTICLE_WORDS = 1000;
    // Pagination Defaults
    public static final int DEFAULT_PAGE_NUMBER = 0;
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 100;


    // Messages - Success
    public static final String USER_REGISTERED_SUCCESS = "User registered successfully";
    public static final String USER_LOGIN_SUCCESS = "Login successful";
    public static final String BLOG_CREATED_SUCCESS = "Blog post created successfully";
    public static final String BLOG_ADDED_SUCCESS = BLOG_CREATED_SUCCESS; // Alias
    public static final String BLOG_DELETED_SUCCESS = "Blog post deleted successfully";
    public static final String BLOG_UPDATED_SUCCESS = "Blog post updated successfully";
    public static final String CATEGORY_CREATED_SUCCESS = "Category created successfully";
    public static final String CATEGORY_DELETED_SUCCESS = "Category deleted successfully";
    public static final String COMMENT_CREATED_SUCCESS = "Comment added successfully";
    public static final String COMMENT_DELETED_SUCCESS = "Comment deleted successfully";
    public static final String TAG_CREATED_SUCCESS = "Tag created successfully";
    public static final String LIKE_ADDED_SUCCESS = "Like added successfully";
    public static final String LIKE_REMOVED_SUCCESS = "Like removed successfully";
    // Messages - Error
    public static final String USER_NOT_FOUND = "User not found";
    public static final String ADMIN_NOT_FOUND = "Admin not found";
    public static final String BLOG_NOT_FOUND = "Blog post not found";

    public static final String CATEGORY_NOT_FOUND = "Category not found";
    public static final String COMMENT_NOT_FOUND = "Comment not found";
    public static final String TAG_NOT_FOUND = "Tag not found";
    public static final String EMAIL_ALREADY_EXISTS = "Email already exists";
    public static final String USERNAME_ALREADY_EXISTS = "Username already exists";
    public static final String CATEGORY_ALREADY_EXISTS = "Category already exists";
    public static final String TAG_ALREADY_EXISTS = "Tag already exists";
    public static final String INVALID_CREDENTIALS = "Invalid email or password";
    public static final String ARTICLE_MIN_WORDS_ERROR = "Article must have at least " + MIN_ARTICLE_WORDS + "words";
    public static final String ALREADY_LIKED = "You have already liked this post";
    public static final String NOT_LIKED = "You have not liked this post";
    // Database backup threshold
    public static final long BACKUP_THRESHOLD_ROWS = 10000;

}