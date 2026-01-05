package com.blogsite.blog_command_service.scheduler;

import com.blogsite.blog_command_service.service.BackupService;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation. Scheduled;
import org.springframework.stereotype.Component;
/**
 *Scheduled job for automatic backup operations.
 * Checks record count periodically and triggers backup when threshold (10,000) is exfeeded.
 * Records are NOT deleted only backed up to JSON files.
 */
@Component
@RequiredArgsConstructor
@Size
@Slf4j
public class BackupScheduler {
    private final BackupService backupService;
    @Value("${backup.enabled:true}")
    private boolean backupEnabled;
    /**
     * Check record count every hour and trigger backup if threshold is exceeded.
     * Cron: 0 0 ✶ ✶ ✶ ✶ = Every hour at minute 0
     */
    @Scheduled (cron = "${backup.schedule.check: 0 0✶✶ ✶ ✶}")
    public void checkAndBackup() {
        if (!backupEnabled) {
            log.debug("Backup is disabled");
            return;
        }
        log.info("Running scheduled backup check...");
        long recordCount = backupService.getRecordCount();
        log.info("Current record count: {} (Threshold: {})", recordCount, backupService.getThreshold());
        if (backupService.isBackupNeeded()) {
            log.info("Backup threshold exceeded. Initiating backup...");
            try {
                String backupFile = backupService.performBackup();
                log.info("Scheduled backup completed: {}", backupFile);
            } catch (Exception e) {
                log.error("Scheduled backup failed: {}", e.getMessage(), e);
            }
        } else {
            log.info("Backup not needed yet. Current: {}, Threshold: {}", recordCount, backupService.getThreshold());
        }
    }
}