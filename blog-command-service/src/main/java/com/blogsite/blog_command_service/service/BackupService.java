package com.blogsite.blog_command_service.service;

public interface BackupService {
    boolean isBackupNeeded();

    long getRecordCount();

    long getThreshold();

    String performBackup();
}
