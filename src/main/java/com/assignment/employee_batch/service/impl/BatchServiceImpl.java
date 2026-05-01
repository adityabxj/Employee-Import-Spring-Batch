package com.assignment.employee_batch.service.impl;


import com.assignment.employee_batch.entity.BatchJobTracker;
import com.assignment.employee_batch.repository.BatchJobTrackerRepository;
import com.assignment.employee_batch.service.BatchService;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;

@Service
public class BatchServiceImpl implements BatchService {

    private final BatchJobTrackerRepository repository;

    public BatchServiceImpl(BatchJobTrackerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Map<String,String> getStatus(String batchId) {

        BatchJobTracker tracker = repository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Batch not found"));

        return Map.of(
                "status", tracker.getStatus(),
                "successFile", encode(tracker.getSuccessFilePath()),
                "errorFile", encode(tracker.getErrorFilePath())
        );
    }

    private String encode(String path) {
        try {
            if (path == null) return null;
            byte[] file = Files.readAllBytes(Paths.get(path));
            return Base64.getEncoder().encodeToString(file);
        } catch (Exception e) {
            return null;
        }
    }
}
