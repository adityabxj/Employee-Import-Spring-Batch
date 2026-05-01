package com.assignment.employee_batch.batch.reader;


import com.assignment.employee_batch.dto.EmployeeDto;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class CsvEmployeeReader {

    @Bean
    @StepScope
    public FlatFileItemReader<EmployeeDto> reader(
            @Value("#{jobParameters['filePath']}") String filePath) {

        return new FlatFileItemReaderBuilder<EmployeeDto>()
                .name("employeeCsvReader")
                .resource(new FileSystemResource(filePath))
                .delimited()
                .names("empId", "name", "email") // must match CSV columns
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(EmployeeDto.class);
                }})
                .linesToSkip(1) // skip header
                .build();
    }

}
