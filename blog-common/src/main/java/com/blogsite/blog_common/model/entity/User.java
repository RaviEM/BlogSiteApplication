package com.blogsite.blog_common.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok. AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 * Tag entity representing a tag/label for blog posts.
 * Stored in MongoDB database.
 * Used for tagging and filtering blog posts.
 */
@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType. IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @NotBlank (message = "Username is mandatory")
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Pattern(regexp = ".*@.*\\.com$", message = "Email must contain '@' and end with '.com")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank (message = "Password is mandatory")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[a-zA-Z]) (?=.*[0-9]).*$", message = "Password must be alphanumeric") @Column(name = "password", nullable = false)
    private String password;
    /**
     * List of blog post IDs authored by this user.
     * Stored as a comma-separated string in MySQL, references MongoDB Blog Post documents. */
    @ElementCollection
    @CollectionTable(name = "user_posts", joinColumns = @JoinColumn(name = "user_id")) @Column(name = "post_id")
    @Builder.Default
    private List<String> postIds = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_active") @Builder.Default
    private Boolean isActive = true;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }


     public void addPost(String postId) {
         if (postIds == null) {
         postIds = new ArrayList<>();
         postIds.add(postId);
         }
     }

    public void removePost (String postId) {
            if (postIds != null) {
                postIds.remove(postId);
            }
        }
}