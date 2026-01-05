package com.blogsite.blog_common.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation. Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index. Indexed;
import org.springframework.data.mongodb.core.mapping. Document;
import org.springframework.data.mongodb.core.mapping. Field;
import java.time.LocalDateTime;

@Document(collection = "likes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@CompoundIndexes({
        @CompoundIndex(name = "user_post_unique", def = "{'user_id': 1, 'post_id': 1}", unique = true)
})
public class Like {
    @Id
    private String likeId;
    /**
     * Reference to the user who liked the post (User entity in MySQL).
     * Stores the userId from the User entity.
     */
    @Field("user_id")
    @Indexed
    private Long userId;
    /**
     * Reference to the blog post that was liked.
     */
    @Field("post_id")
    @Indexed
    private String postId;

    @Field("created_at")
    private LocalDateTime createdAt;
}