package com.assignment.employee_batch.batch.config;

import com.assignment.employee_batch.batch.listener.JobCompletionListener;
import com.assignment.employee_batch.batch.processor.EmployeeProcessor;
import com.assignment.employee_batch.batch.reader.ExcelEmployeeReader;
import com.assignment.employee_batch.batch.writer.EmployeeWriter;
import com.assignment.employee_batch.dto.EmployeeDto;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.core.configuration.annotation.StepScope;

import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

    @Bean
    public Job employeeJob(JobRepository jobRepository,
                           Step employeeStep,
                           JobCompletionListener listener) {

        return new JobBuilder("employeeJob", jobRepository)
                .listener(listener)
                .start(employeeStep)
                .build();
    }

    @Bean
    public Step employeeStep(JobRepository jobRepository,
                             PlatformTransactionManager transactionManager,
                             ItemReader<EmployeeDto> reader,
                             EmployeeProcessor processor,
                             EmployeeWriter writer,
                             @Qualifier("csvReader") FlatFileItemReader<EmployeeDto> csvReader) {

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

    @Bean
    @StepScope
    public ItemStreamReader<EmployeeDto> reader(
            @Value("#{jobParameters['fileType']}") String fileType,
            @Qualifier("csvReader") FlatFileItemReader<EmployeeDto> csvReader,
            ExcelEmployeeReader excelReader) {

        System.out.println("File type: " + fileType);

        if ("csv".equalsIgnoreCase(fileType)) {
            return csvReader;
        } else if ("xls".equalsIgnoreCase(fileType) || "xlsx".equalsIgnoreCase(fileType)) {
            return excelReader;
        } else {
            throw new RuntimeException("Invalid file type: " + fileType);
        }
    }
}