
CREATE DATABASE IF NOT EXISTS blogsite_users;
USE blogsite_users;
-- USER TABLE
CREATE TABLE IF NOT EXISTS users (
user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
username VARCHAR(50) NOT NULL UNIQUE,
email VARCHAR(100) NOT NULL UNIQUE,
password VARCHAR(255) NOT NULL,
is_active BOOLEAN DEFAULT TRUE,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
-- Indexes for faster queries
INDEX idx_users_username (username),
INDEX idx_users_email (email),
INDEX idx_users_is_active (is_active),
INDEX idx_users_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
--USER_POSTS TABLE (For storing post IDs - references MongoDB)
--Managed by JPA @ElementCollection
CREATE TABLE IF NOT EXISTS user_posts (
user_id BIGINT NOT NULL,
post_id VARCHAR(50) NOT NULL,
--Primary key on both columns (JPA ElementCollection style)
PRIMARY KEY (user_id, post_id),
--Foreign key constraint
CONSTRAINT fk_user_posts_user_id
FOREIGN KEY (user_id) REFERENCES users (user_id)
ON DELETE CASCADE ON UPDATE CASCADE,
--Indexes
INDEX idx_user_posts_post_id (post_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
--ADMIN TABLE
CREATE TABLE IF NOT EXISTS admins (
admin_id BIGINT AUTO_INCREMENT PRIMARY KEY,
username VARCHAR(50) NOT NULL UNIQUE,
email VARCHAR(100) NOT NULL UNIQUE,
user_id BIGINT,
is_active BOOLEAN DEFAULT TRUE,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
--Foreign key constraint linking to users table
CONSTRAINT fk_admins_user_id
FOREIGN KEY (user_id) REFERENCES users (user_id)
ON DELETE SET NULL ON UPDATE CASCADE,
-- Indexes
INDEX idx_admins_username (username), INDEX idx_admins_email (email),
INDEX idx_admins_user_id (user_id),
INDEX idx_admins_is_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
--SAMPLE DATA (Optional for testing)
--Password: Admin@123 (should be BCrypt hashed in real application)
INSERT INTO users (username, email, password) VALUES ('admin', 'admin@blogsite.com', '$2a$10$...');
INSERT INTO admins (username, email, user id) VALUES ('admin', 'admin@blogsite.com, LAST INSERT_ID());
