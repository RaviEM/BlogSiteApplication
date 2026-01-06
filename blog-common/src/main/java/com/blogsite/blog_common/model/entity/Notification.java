package com.blogsite.blog_common.model.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "notifications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    private String notificationId;
    /**
     * Reference to the user who receives the notification (User entity in MySQL).
     * Stores the userId from the User entity.
     */
    @Field("user_id")
    @Indexed
    private Long userId;

    @Field("message")
    private String message;

    @Field("timestamp")
    private LocalDateTime timestamp;

    @Field("is_read")
    @Builder.Default
    private Boolean isRead = false;

    /**
     * Type of notification (e.g., COMMENT, LIKE, FOLLOW, SYSTEM).
     */
    @Field("notification_type")
    @Indexed
    private NotificationType notificationType;

    /**
     * Optional reference to related entity (e.g., postId, commentId).
     */
    @Field("reference_id")
    private String referenceId;

    /**
     * Optional reference type (e.g., "POST", "COMMENT").
     */
    @Field("reference_type")
    private String referenceType;

    @Field("created_at")
    private LocalDateTime createdAt;

    /**
     * Enum for notification types.
     */
    public enum NotificationType {
        COMMENT,
        LIKE,
        FOLLOW,
        MENTION,
        SYSTEM,
        NEW_POST
    }

    /**
     * Marks the notification as read.
     */
    public void markAsRead() {
        this.isRead = true;
    }
}