package com.blogsite.blog_common.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints. Pattern;
import jakarta.validation.constraints.Size;
import lombok. AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * DTO for user registration request.
 * Contains validation rules as per business requirements.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequest {
    @NotBlank(message = "Username is mandatory")
    private String userName;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Pattern(regexp = ".*@.*\\.com$", message = "Email must contain '@' and end with '.com'")
    private String userEmail;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message
            =
            "Password must be at least 8 characters")
    @Pattern(regexp = "^(?=.* [a-zA-Z]) (?=.*[0-9]).*$", message = "Password must be alphanumeric")
    private String password;
}