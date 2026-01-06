package com.blogsite.blog_query_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(exclude = {
		DataSourceAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class
})
@ComponentScan(basePackages = {
		"com.blogsite.blog_query_service",
		"com.blogsite.blog_common"
})
@EnableMongoRepositories(basePackages = "com.blogsite.blog_query_service.repository")
public class BlogQueryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogQueryServiceApplication.class, args);
	}

}
