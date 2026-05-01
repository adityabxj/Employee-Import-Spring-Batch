package com.assignment.employee_batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class EmployeeBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeBatchApplication.class, args);
	}

}
