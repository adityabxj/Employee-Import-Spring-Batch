package com.assignment.employee_batch.batch.listener;

import com.assignment.employee_batch.entity.BatchJobTracker;
import com.assignment.employee_batch.repository.BatchJobTrackerRepository;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionListener implements JobExecutionListener {

    private final BatchJobTrackerRepository trackerRepository;

    public JobCompletionListener(BatchJobTrackerRepository trackerRepository) {
        this.trackerRepository = trackerRepository;
    }


    @Override
    public void afterJob(JobExecution jobExecution) {

        String batchId = jobExecution.getJobParameters().getString("batchId");
        String inputFilePath = jobExecution.getJobParameters().getString("filePath");

        if (batchId == null) {
            throw new RuntimeException("BatchId is missing in job parameters");
        }


        String successFilePath =
                jobExecution.getExecutionContext().getString("successFilePath");

        String errorFilePath =
                jobExecution.getExecutionContext().getString("errorFilePath");

        // 🔷 Update DB
        BatchJobTracker tracker =
                trackerRepository.findById(batchId).orElse(null);

        if (tracker != null) {

            if (jobExecution.getStatus().isUnsuccessful()) {
                tracker.setStatus("FAILED");
            } else {
                tracker.setStatus("COMPLETED");
            }

            tracker.setSuccessFilePath(successFilePath);
            tracker.setErrorFilePath(errorFilePath);

            trackerRepository.save(tracker);
        }


        try {
            if (inputFilePath != null) {
                java.nio.file.Files.deleteIfExists(
                        java.nio.file.Paths.get(inputFilePath)
                );
            }
        } catch (Exception e) {
            System.out.println("Failed to delete temp file: " + e.getMessage());
        }
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {

        String batchId = jobExecution.getJobParameters().getString("batchId");

        if (batchId == null) {
            throw new RuntimeException("BatchId missing");
        }

        BatchJobTracker tracker = new BatchJobTracker();
        tracker.setBatchId(batchId);
        tracker.setStatus("STARTED");

        trackerRepository.save(tracker);
    }
}