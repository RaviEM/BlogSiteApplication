package com.blogsite.blog_command_service.service.impl;


import com.blogsite.blog_command_service.repository.BlogPostRepository;
import com.blogsite.blog_command_service.service.BackupService;
import com.blogsite.blog_common.model.entity.BlogPost;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class BackupServiceImpl implements BackupService {
    private final BlogPostRepository blogPostRepository;
    private final ObjectMapper objectMapper;

    @Value("${backup. threshold: 10000}")
    private long backupThreshold;
    @Value("${backup.directory: ./backups}")
    private String backupDirectory;

    public BackupServiceImpl(BlogPostRepository blogPostRepository) {
        this.blogPostRepository = blogPostRepository;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    public boolean isBackupNeeded() {
        long count = getRecordCount();
        boolean needed = count >= backupThreshold;
        if (needed) {
            log.warn("Backup threshold reached! Current count: {}, Threshold: {}", count, backupThreshold);
        }
        return needed;
    }

    @Override
    public long getRecordCount() {
        return blogPostRepository.count();
    }

    @Override
    public long getThreshold() {
        return backupThreshold;
    }

    @Override
    public String performBackup() {
        log.info("Starting backup process...");
        try {
            // Ensure backup directory exists
            Path backupPath = Paths.get(backupDirectory);
            if (!Files.exists(backupPath)) {
                Files.createDirectories(backupPath);
                log.info("Created backup directory: {}", backupDirectory);
            }

            // Generate backup filename with timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = String.format("blog_posts_backup_%s.json", timestamp);
            File backupFile = new File(backupDirectory, filename);

            // Fetch all blog posts
            List<BlogPost> allPosts = blogPostRepository.findAll();
            log.info("Fetched {} blog posts for backup", allPosts.size());

            // Write to JSON file
            objectMapper.writeValue(backupFile, allPosts);
            String absolutePath = backupFile.getAbsolutePath();
            log.info("Backup completed successfully. File: () () records)", absolutePath, allPosts.size());

            return absolutePath;
        } catch (IOException e) {
            log.error("Failed to perform backup: {}", e.getMessage(), e);
            throw new RuntimeException("Backup failed: " + e.getMessage(), e);
        }
    }
}