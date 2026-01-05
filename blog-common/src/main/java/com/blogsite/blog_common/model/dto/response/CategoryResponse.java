package com.blogsite.blog_common.model.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude; import lombok. AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

 @Data
 @Builder
 @NoArgsConstructor
 @AllArgsConstructor
 @JsonInclude(JsonInclude. Include.NON_NULL)
     public class CategoryResponse {
     private String categoryId;
     private String name;
     private String description;
     private Long postCount;
     private LocalDateTime createdAt;
     private LocalDateTime updatedAt;
     private Boolean isActive;
 }