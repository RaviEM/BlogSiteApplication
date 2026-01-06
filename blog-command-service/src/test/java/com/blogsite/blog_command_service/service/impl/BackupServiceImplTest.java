package com.blogsite.blog_command_service.service.impl;

import com.blogsite.blog_command_service.repository.BlogPostRepository;
import com.blogsite.blog_common.model.entity.BlogPost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BackupServiceImpl Tests")
class BackupServiceImplTest {

    @Mock
    private BlogPostRepository blogPostRepository;

    @InjectMocks
    private BackupServiceImpl backupService;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        // Set backup directory to temp directory for testing
        ReflectionTestUtils.setField(backupService, "backupDirectory", tempDir.toString());
        ReflectionTestUtils.setField(backupService, "backupThreshold", 10000L);
    }

    @Test
    @DisplayName("Should return true when backup is needed")
    void shouldReturnTrueWhenBackupNeeded() {
        // Given
        when(blogPostRepository.count()).thenReturn(15000L);

        // When
        boolean result = backupService.isBackupNeeded();

        // Then
        assertTrue(result);
        verify(blogPostRepository).count();
    }

    @Test
    @DisplayName("Should return false when backup is not needed")
    void shouldReturnFalseWhenBackupNotNeeded() {
        // Given
        when(blogPostRepository.count()).thenReturn(5000L);

        // When
        boolean result = backupService.isBackupNeeded();

        // Then
        assertFalse(result);
        verify(blogPostRepository).count();
    }

    @Test
    @DisplayName("Should return record count")
    void shouldReturnRecordCount() {
        // Given
        when(blogPostRepository.count()).thenReturn(7500L);

        // When
        long count = backupService.getRecordCount();

        // Then
        assertEquals(7500L, count);
        verify(blogPostRepository).count();
    }

    @Test
    @DisplayName("Should return threshold")
    void shouldReturnThreshold() {
        // When
        long threshold = backupService.getThreshold();

        // Then
        assertEquals(10000L, threshold);
    }

    @Test
    @DisplayName("Should perform backup successfully")
    void shouldPerformBackupSuccessfully() {
        // Given
        BlogPost post1 = BlogPost.builder()
                .postId("post1")
                .blogName("Test Blog 1")
                .content("Content 1")
                .build();
        BlogPost post2 = BlogPost.builder()
                .postId("post2")
                .blogName("Test Blog 2")
                .content("Content 2")
                .build();
        List<BlogPost> posts = Arrays.asList(post1, post2);
        when(blogPostRepository.findAll()).thenReturn(posts);

        // When
        String backupFile = backupService.performBackup();

        // Then
        assertNotNull(backupFile);
        assertTrue(backupFile.contains("blog_posts_backup"));
        assertTrue(backupFile.endsWith(".json"));
        verify(blogPostRepository).findAll();
        
        // Verify file was created
        File file = new File(backupFile);
        assertTrue(file.exists());
    }

    @Test
    @DisplayName("Should create backup directory if it doesn't exist")
    void shouldCreateBackupDirectoryIfNotExists() {
        // Given
        Path newBackupDir = tempDir.resolve("new-backup-dir");
        ReflectionTestUtils.setField(backupService, "backupDirectory", newBackupDir.toString());
        when(blogPostRepository.findAll()).thenReturn(List.of());

        // When
        String backupFile = backupService.performBackup();

        // Then
        assertNotNull(backupFile);
        assertTrue(newBackupDir.toFile().exists());
    }

    @Test
    @DisplayName("Should handle empty blog posts list")
    void shouldHandleEmptyBlogPostsList() {
        // Given
        when(blogPostRepository.findAll()).thenReturn(List.of());

        // When
        String backupFile = backupService.performBackup();

        // Then
        assertNotNull(backupFile);
        File file = new File(backupFile);
        assertTrue(file.exists());
    }

    @Test
    @DisplayName("Should throw RuntimeException when backup fails")
    void shouldThrowRuntimeExceptionWhenBackupFails() {
        // Given
        ReflectionTestUtils.setField(backupService, "backupDirectory", "/invalid/path/that/does/not/exist");
        when(blogPostRepository.findAll()).thenReturn(List.of());

        // When/Then
        assertThrows(RuntimeException.class, () -> backupService.performBackup());
    }
}
