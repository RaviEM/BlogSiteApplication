package com.blogsite.blog_common.model.dto.response;


import com.blogsite.blog_common.model.entity.Notification.NotificationType;
import com.fasterxml.jackson. annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
/**
 * DTO for notification response data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude. Include.NON_NULL)
public class NotificationResponse {
    private String notificationId;
    private Long userId;
    private String message;
    private LocalDateTime timestamp;
    private Boolean isRead;
    private NotificationType notificationType;
    private String referenceId;
    private String referenceType;
    private LocalDateTime createdAt;
}