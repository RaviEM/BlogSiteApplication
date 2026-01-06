package com.blogsite.blog_user_service.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation. Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password. PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
/**
 * Security configuration for User Service.
 * Configures which endpoints are public and which require authentication.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    /**
     * Public endpoints that don't require authentication.
     */
    private static final String[] PUBLIC_ENDPOINTS ={
        // User registration and login
        "/api/v1.0/blogsite/user/register",
                "/api/v1.0/blogsite/user/login",
        // Swagger/OpenAPI documentation
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/v3/api-docs/**",
        "/api-docs/**",
// Actuator health endpoint
        "/actuator/health",
        "/actuator/info",
// Error page
        "/error"
    };

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        http
            // Disable CSRF for REST APIS
                .csrf(csrf ->csrf.disable())
            // Configure authorization
                .authorizeHttpRequests (auth -> auth
                .requestMatchers (PUBLIC_ENDPOINTS). permitAll()
                .anyRequest().authenticated()
                )
                // Stateless session (for JWT)
                .sessionManagement (session -> session
                        .sessionCreationPolicy (SessionCreationPolicy. STATELESS)
                )
                // Add JWT filter before Username PasswordAuthenticationFilter
                        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    /**
     * Password encoder for hashing passwords.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}