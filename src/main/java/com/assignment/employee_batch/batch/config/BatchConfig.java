package com.assignment.employee_batch.batch.config;

import com.assignment.employee_batch.batch.processor.EmployeeProcessor;
import com.assignment.employee_batch.batch.writer.EmployeeWriter;
import com.assignment.employee_batch.dto.EmployeeDto;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import com.assignment.employee_batch.batch.listener.JobCompletionListener;


@Configuration
public class BatchConfig {

    // ================= JOB =================

    @Bean
    public Job employeeJob(JobRepository jobRepository,
                           Step employeeStep,
                           JobCompletionListener listener) {

        return new JobBuilder("employeeJob", jobRepository)
                .listener(listener)
                .start(employeeStep)
                .build();
    }

    // ================= STEP =================

    @Bean
    public Step employeeStep(JobRepository jobRepository,
                             PlatformTransactionManager transactionManager,
                             org.springframework.batch.item.ItemReader<EmployeeDto> reader,
                             EmployeeProcessor processor,
                             EmployeeWriter writer) {

        return new StepBuilder("employeeStep", jobRepository)
                .<EmployeeDto, EmployeeDto>chunk(100, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .listener(writer)
                .faultTolerant()
                .skip(Exception.class)
                .skipLimit(100)
                .build();
    }
}
