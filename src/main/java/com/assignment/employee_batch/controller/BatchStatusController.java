package com.assignment.employee_batch.controller;


import com.assignment.employee_batch.dto.ApiResponse;
import com.assignment.employee_batch.service.BatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class BatchStatusController {


    private final BatchService batchService;

    public BatchStatusController(BatchService batchService) {
        this.batchService = batchService;
    }

    @GetMapping("/status/{batchId}")
    public ResponseEntity<ApiResponse<Map<String,String>>> status(@PathVariable String batchId) {

        Map<String,String> data = batchService.getStatus(batchId);

        return ResponseEntity.ok(
                new ApiResponse<>("SUCCESS", "Batch status fetched", data)
        );
    }
}
