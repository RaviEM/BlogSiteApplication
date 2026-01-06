package com.blogsite.blog_command_service.controller;


import com.blogsite.blog_command_service.service.BackupService;
import com.blogsite.blog_common.model.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok. RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1.0/blogsite/admin/backup")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Backup Management", description = "APIs for backup operations") public class BackupController {
    private final BackupService backupService;


    @GetMapping("/status")
    @Operation(summary = "Get backup status", description = "Returns current record count and backup threshold status")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getBackupStatus() {
        log.info("GET /admin/backup/status");

        long recordCount = backupService.getRecordCount();
        long threshold = backupService.getThreshold();
        boolean backupNeeded = backupService.isBackupNeeded();
        Map<String, Object> status = new HashMap<>();
        status.put("recordCount", recordCount);
        status.put("threshold", threshold);
        status.put("backupNeeded", backupNeeded);
        status.put("records UntilBackup", Math.max(0, threshold - recordCount));
        return ResponseEntity.ok(ApiResponse.success(status, "Backup status retrieved"));
    }

    @PostMapping("/trigger")
    @Operation(summary = "Trigger backup", description = "Manually triggers a backup of all blog posts to JSON file")
    public ResponseEntity<ApiResponse<Map<String, Object>>> triggerBackup() {
        log.info("POST /admin/backup/trigger - Manual backup requested");
        try {
            String backupFile = backupService.performBackup();
            Map<String, Object> result = new HashMap<>();
            result.put("backupFile", backupFile);
            result.put("recordCount", backupService.getRecordCount());
            result.put("status", "SUCCESS");
            return ResponseEntity.ok(ApiResponse.success(result, "Backup completed successfully"));
        } catch (Exception e) {
            log.error("Manual backup failed: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Backup failed: " + e.getMessage()));
        }
    }
}