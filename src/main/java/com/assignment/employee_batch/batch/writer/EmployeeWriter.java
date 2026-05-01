package com.assignment.employee_batch.batch.writer;

import com.assignment.employee_batch.constants.AppConstants;
import com.assignment.employee_batch.dto.EmployeeDto;
import com.assignment.employee_batch.entity.Employee;
import com.assignment.employee_batch.mapper.EmployeeMapper;
import com.assignment.employee_batch.repository.EmployeeRepository;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.io.FileWriter;

@Component
public class EmployeeWriter implements ItemWriter<EmployeeDto>, StepExecutionListener {

    private final EmployeeRepository repository;

    private StepExecution stepExecution;

    private FileWriter successWriter;
    private FileWriter errorWriter;

    public EmployeeWriter(EmployeeRepository repository) {
        this.repository = repository;
    }

    // ✅ INIT EVERYTHING HERE (IMPORTANT)
    @Override
    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;

        try {
            String batchId =
                    stepExecution.getJobParameters().getString("batchId");

            String successFile = "success_" + batchId + ".csv";
            String errorFile = "error_" + batchId + ".csv";

            // 🔥 SAVE CONTEXT HERE (CORRECT PLACE)
            stepExecution.getExecutionContext().put("successFilePath", successFile);
            stepExecution.getExecutionContext().put("errorFilePath", errorFile);

            // create files
            successWriter = new FileWriter(successFile, true);
            errorWriter = new FileWriter(errorFile, true);

            // headers
            successWriter.write("empId,name,email\n");
            errorWriter.write("empId,name,email,error\n");

        } catch (Exception e) {
            throw new RuntimeException("Error initializing writer", e);
        }
    }

    @Override
    public void write(Chunk<? extends EmployeeDto> items) {

        try {
            for (EmployeeDto dto : items) {

                if (AppConstants.RECORD_VALID.equals(dto.getStatus())) {

                    Employee emp = EmployeeMapper.toEntity(dto);
                    repository.save(emp);

                    successWriter.write(format(dto));

                } else if (AppConstants.RECORD_EXISTS.equals(dto.getStatus())) {

                    successWriter.write(format(dto));

                } else if (AppConstants.RECORD_ERROR.equals(dto.getStatus())) {

                    errorWriter.write(formatError(dto));
                }
            }

            successWriter.flush();
            errorWriter.flush();

        } catch (Exception e) {
            throw new RuntimeException("Error writing files", e);
        }
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {

        try {
            if (successWriter != null) {
                successWriter.close();
            }
            if (errorWriter != null) {
                errorWriter.close();
            }

            String successFile = null;
            String errorFile = null;

            if (stepExecution.getExecutionContext().containsKey("successFilePath")) {
                successFile = stepExecution.getExecutionContext()
                        .getString("successFilePath");
            }

            if (stepExecution.getExecutionContext().containsKey("errorFilePath")) {
                errorFile = stepExecution.getExecutionContext()
                        .getString("errorFilePath");
            }

            if (successFile != null) {
                stepExecution.getJobExecution().getExecutionContext()
                        .put("successFilePath", successFile);
            }

            if (errorFile != null) {
                stepExecution.getJobExecution().getExecutionContext()
                        .put("errorFilePath", errorFile);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error in afterStep", e);
        }

        return ExitStatus.COMPLETED;
    }
    private String format(EmployeeDto dto) {
        return dto.getEmpId() + "," + dto.getName() + "," + dto.getEmail() + "\n";
    }

    private String formatError(EmployeeDto dto) {
        return dto.getEmpId() + "," + dto.getName() + "," +
                dto.getEmail() + "," + dto.getErrorMessage() + "\n";
    }
}