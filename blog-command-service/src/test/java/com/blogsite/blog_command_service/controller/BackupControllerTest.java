package com.blogsite.blog_command_service.controller;

import com.blogsite.blog_command_service.service.BackupService;
import com.blogsite.blog_common.model.dto.response.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BackupController Tests")
class BackupControllerTest {

    @Mock
    private BackupService backupService;

    @InjectMocks
    private BackupController backupController;

    @Test
    @DisplayName("Should get backup status successfully")
    void shouldGetBackupStatusSuccessfully() {
        // Given
        when(backupService.getRecordCount()).thenReturn(5000L);
        when(backupService.getThreshold()).thenReturn(10000L);
        when(backupService.isBackupNeeded()).thenReturn(false);

        // When
        ResponseEntity<ApiResponse<Map<String, Object>>> response = backupController.getBackupStatus();

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertNotNull(response.getBody().getData());
        
        Map<String, Object> data = response.getBody().getData();
        assertEquals(5000L, data.get("recordCount"));
        assertEquals(10000L, data.get("threshold"));
        assertEquals(false, data.get("backupNeeded"));
        assertTrue(data.containsKey("recordsUntilBackup"));
        
        verify(backupService).getRecordCount();
        verify(backupService).getThreshold();
        verify(backupService).isBackupNeeded();
    }

    @Test
    @DisplayName("Should trigger backup successfully")
    void shouldTriggerBackupSuccessfully() {
        // Given
        String backupFile = "/path/to/backup.json";
        when(backupService.performBackup()).thenReturn(backupFile);
        when(backupService.getRecordCount()).thenReturn(1000L);

        // When
        ResponseEntity<ApiResponse<Map<String, Object>>> response = backupController.triggerBackup();

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertNotNull(response.getBody().getData());
        
        Map<String, Object> data = response.getBody().getData();
        assertEquals(backupFile, data.get("backupFile"));
        assertEquals(1000L, data.get("recordCount"));
        assertEquals("SUCCESS", data.get("status"));
        
        verify(backupService).performBackup();
        verify(backupService).getRecordCount();
    }

    @Test
    @DisplayName("Should handle backup failure")
    void shouldHandleBackupFailure() {
        // Given
        when(backupService.performBackup()).thenThrow(new RuntimeException("Backup failed"));

        // When
        ResponseEntity<ApiResponse<Map<String, Object>>> response = backupController.triggerBackup();

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("Backup failed"));
        
        verify(backupService).performBackup();
    }

    @Test
    @DisplayName("Should calculate recordsUntilBackup correctly when backup needed")
    void shouldCalculateRecordsUntilBackupWhenBackupNeeded() {
        // Given
        when(backupService.getRecordCount()).thenReturn(15000L);
        when(backupService.getThreshold()).thenReturn(10000L);
        when(backupService.isBackupNeeded()).thenReturn(true);

        // When
        ResponseEntity<ApiResponse<Map<String, Object>>> response = backupController.getBackupStatus();

        // Then
        Map<String, Object> data = response.getBody().getData();
        assertEquals(0L, data.get("recordsUntilBackup")); // Should be 0 when threshold exceeded
    }
}
