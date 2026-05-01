package com.assignment.employee_batch.service.impl;


import com.assignment.employee_batch.service.FileService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.util.Base64;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private final JobLauncher jobLauncher;
    private final Job employeeJob;

    public FileServiceImpl(JobLauncher jobLauncher, Job employeeJob) {
        this.jobLauncher = jobLauncher;
        this.employeeJob = employeeJob;
    }

    @Override
    public String uploadFile(String base64, String fileType) {

        try {
            // 🔥 FIX: remove base64 prefix if present
            if (base64.contains(",")) {
                base64 = base64.split(",")[1];
            }

            byte[] decoded = Base64.getDecoder().decode(base64);

            File file = File.createTempFile("emp_", "." + fileType);
            Files.write(file.toPath(), decoded);

            String batchId = UUID.randomUUID().toString();

            JobParameters params = new JobParametersBuilder()
                    .addString("filePath", file.getAbsolutePath())
                    .addString("batchId", batchId)
                    .addString("fileType", fileType)
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            System.out.println("JOB TRIGGERED");

            jobLauncher.run(employeeJob, params);

            return batchId;

        } catch (Exception e) {
            throw new RuntimeException("Error uploading file", e);
        }
    }
}
