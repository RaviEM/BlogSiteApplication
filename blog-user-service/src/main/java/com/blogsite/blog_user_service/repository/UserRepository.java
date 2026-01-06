package com.blogsite.blog_user_service.repository;

import com.blogsite.blog_common.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
/**
 * Repository interface for User entity.
 * Uses Spring Data JPA with custom @Query methods for complex queries. */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Find user by email address.
     */
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    /**
     * Find active user by email (for login).
     */
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.isActive = true")
    Optional<User> findActiveUserByEmail(@Param("email") String email);

    /**
     * Check if email already exists.
     */
    boolean existsByEmail(String email);

    /**
     * Check if username already exists.
     */
    boolean existsByUsername(String username);

    /**
     * Find all active users.
     */
    @Query("SELECT u FROM User u WHERE u.isActive = true ORDER BY u.createdAt DESC")
    List<User> findAllActiveUsers();

    /**
     * Count active users.
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.isActive = true")
    long countActiveUsers();

    /**
     * Find users by username containing (search).
     */
    @Query("SELECT u FROM User u WHERE u.username LIKE CONCAT('%', :keyword, '%') AND u.isActive = true")
    List<User> searchByUsername(@Param("keyword") String keyword);
}