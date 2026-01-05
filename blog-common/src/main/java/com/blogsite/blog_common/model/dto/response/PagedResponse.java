package com.blogsite.blog_common.model.dto.response;


import com.fasterxml.jackson. annotation.JsonInclude;
import lombok. AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for notification response data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude. Include.NON_NULL)
public class PagedResponse<T> {
    private List<T> conten;
    private int page;
    private int size;

}
