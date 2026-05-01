package com.assignment.employee_batch.repository;

import com.assignment.employee_batch.entity.BatchJobTracker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BatchJobTrackerRepository
        extends JpaRepository<BatchJobTracker, String> {

    List<BatchJobTracker> findByCreatedAtBefore(LocalDateTime time);
}