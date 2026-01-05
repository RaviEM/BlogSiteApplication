package com.blogsite.blog_command_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
@ComponentScan(basePackages = {
        "com.blogsite.blog_command_service",
        "com.blogsite.blog_common"
})
@EnableMongoRepositories(basePackages = "com.blogsite.blog_command_service.repository")
@EnableScheduling
public class BlogCommandServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogCommandServiceApplication.class, args);
	}

}
