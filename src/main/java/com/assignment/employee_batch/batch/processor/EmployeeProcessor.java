package com.assignment.employee_batch.batch.processor;


import com.assignment.employee_batch.constants.AppConstants;
import com.assignment.employee_batch.dto.EmployeeDto;
import com.assignment.employee_batch.repository.EmployeeRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EmployeeProcessor  implements ItemProcessor<EmployeeDto, EmployeeDto> {

    private final EmployeeRepository repository;

    public EmployeeProcessor(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public EmployeeDto process(EmployeeDto item) {

        if (item.getEmpId() == null || item.getEmpId().trim().isEmpty()) {
            item.setStatus(AppConstants.RECORD_ERROR);
            item.setErrorMessage("EmpId is missing");
            return item;
        }

        if (item.getName() == null || item.getName().trim().isEmpty()) {
            item.setStatus(AppConstants.RECORD_ERROR);
            item.setErrorMessage("Name is missing");
            return item;
        }

        if (item.getEmail() == null ||
                !item.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

            item.setStatus(AppConstants.RECORD_ERROR);
            item.setErrorMessage("Invalid email format");
            return item;
        }


        Optional<?> existing = repository.findByEmpId(item.getEmpId());

        if (existing.isPresent()) {
            item.setStatus(AppConstants.RECORD_EXISTS);
            return item;
        }

        item.setStatus(AppConstants.RECORD_VALID);
        return item;
    }
}
