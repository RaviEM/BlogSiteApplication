package com.blogsite.blog_common.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok. NoArgsConstructor;
import java.time.LocalDateTime;

 @Data
 @Builder
 @NoArgsConstructor
 @AllArgsConstructor
 @JsonInclude(JsonInclude. Include.NON_NULL) public class LikeResponse {
     private String likeId;
     private Long userId;
     private String username;
     private String postId;
     private LocalDateTime createdAt;
 }