package com.assignment.employee_batch.scheduler;

import com.assignment.employee_batch.entity.BatchJobTracker;
import com.assignment.employee_batch.repository.BatchJobTrackerRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class FileCleanupScheduler {

    private final BatchJobTrackerRepository repository;

    public FileCleanupScheduler(BatchJobTrackerRepository repository) {
        this.repository = repository;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void deleteOldFiles() {

        LocalDateTime cutoff = LocalDateTime.now().minusHours(24);

        List<BatchJobTracker> jobs =
                repository.findByCreatedAtBefore(cutoff);

        for (BatchJobTracker job : jobs) {

            deleteFile(job.getSuccessFilePath());
            deleteFile(job.getErrorFilePath());

            repository.delete(job);
        }
    }

    private void deleteFile(String path) {
        try {
            if (path != null && Files.exists(Paths.get(path))) {
                Files.delete(Paths.get(path));
                System.out.println("Deleted file: " + path);
            }
        } catch (Exception e) {
            System.out.println("Delete failed: " + e.getMessage());
        }
    }
}