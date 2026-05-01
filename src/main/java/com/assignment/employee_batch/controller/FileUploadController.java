package com.assignment.employee_batch.controller;


import com.assignment.employee_batch.dto.ApiResponse;
import com.assignment.employee_batch.dto.UploadRequest;
import com.assignment.employee_batch.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class FileUploadController {


    private final FileService fileService;

    public FileUploadController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<Map<String, String>>> upload(@RequestBody UploadRequest request) {

        if (request.getFile() == null || request.getFile().trim().isEmpty()) {
            throw new RuntimeException("File is required");
        }

        if (request.getFileType() == null ||
                !(request.getFileType().equalsIgnoreCase("csv") ||
                        request.getFileType().equalsIgnoreCase("xls") ||
                        request.getFileType().equalsIgnoreCase("xlsx"))) {

            throw new RuntimeException("Invalid file type. Only csv/xls/xlsx allowed");
        }

        String batchId = fileService.uploadFile(
                request.getFile(),
                request.getFileType()
        );

        return ResponseEntity.ok(
                new ApiResponse<>("SUCCESS", "Batch started",
                        Map.of("batchId", batchId))
        );
    }
}
