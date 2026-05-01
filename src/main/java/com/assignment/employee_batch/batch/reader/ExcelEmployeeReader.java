package com.assignment.employee_batch.batch.reader;

import com.assignment.employee_batch.dto.EmployeeDto;
import org.apache.poi.ss.usermodel.*;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;

@Component
@StepScope
public class ExcelEmployeeReader implements ItemStreamReader<EmployeeDto> {

    @Value("#{jobParameters['filePath']}")
    private String filePath;

    private Iterator<Row> rowIterator;
    private Workbook workbook;

    private boolean isHeaderSkipped = false;

    @Override
    public void open(ExecutionContext executionContext) {
        try {
            System.out.println("Opening Excel file: " + filePath);

            InputStream inputStream = new FileInputStream(filePath);
            workbook = WorkbookFactory.create(inputStream);
            rowIterator = workbook.getSheetAt(0).iterator();

        } catch (Exception e) {
            throw new RuntimeException("Error opening Excel file", e);
        }
    }

    @Override
    public EmployeeDto read() {

        if (rowIterator == null) {
            throw new RuntimeException("Excel not initialized");
        }

        if (!isHeaderSkipped && rowIterator.hasNext()) {
            rowIterator.next(); // skip header
            isHeaderSkipped = true;
        }

        if (!rowIterator.hasNext()) {
            return null;
        }

        Row row = rowIterator.next();

        EmployeeDto dto = new EmployeeDto();
        dto.setEmpId(getCellValue(row.getCell(0)));
        dto.setName(getCellValue(row.getCell(1)));
        dto.setEmail(getCellValue(row.getCell(2)));

        System.out.println("Reading row: " + dto.getEmpId());

        return dto;
    }

    @Override
    public void update(ExecutionContext executionContext) {}

    @Override
    public void close() {
        try {
            if (workbook != null) {
                workbook.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error closing Excel file", e);
        }
    }

    private String getCellValue(Cell cell) {

        if (cell == null) return null;

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return null;
        }
    }
}