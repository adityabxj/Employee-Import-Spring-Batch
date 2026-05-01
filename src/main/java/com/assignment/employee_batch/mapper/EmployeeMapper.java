package com.assignment.employee_batch.mapper;

import com.assignment.employee_batch.dto.EmployeeDto;
import com.assignment.employee_batch.entity.Employee;

public class EmployeeMapper {

    public static Employee toEntity(EmployeeDto dto) {
        Employee emp = new Employee();
        emp.setEmpId(dto.getEmpId());
        emp.setName(dto.getName());
        emp.setEmail(dto.getEmail());
        return emp;
    }

}
