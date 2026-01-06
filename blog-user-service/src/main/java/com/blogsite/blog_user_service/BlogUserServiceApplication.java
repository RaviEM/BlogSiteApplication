package com.blogsite.blog_user_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {
		MongoAutoConfiguration.class,
		MongoDataAutoConfiguration.class
})
@ComponentScan(basePackages = {
		"com.blogsite.blog_user_service",
		"com.blogsite.blog_common"
})
@EntityScan(basePackages = "com.blogsite.blog_common.model.entity")
public class BlogUserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogUserServiceApplication.class, args);
	}

}
