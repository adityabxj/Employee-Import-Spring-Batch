package com.assignment.employee_batch.batch.reader;


import com.assignment.employee_batch.dto.EmployeeDto;
import org.apache.poi.ss.usermodel.*;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;


@Component
@StepScope
public class ExcelEmployeeReader
    implements ItemReader<EmployeeDto>, ItemStream {

        @Value("#{jobParameters['filePath']}")
        private String filePath;

        private Iterator<Row> rowIterator;
        private Workbook workbook;
        private Sheet sheet;

        private boolean isHeaderSkipped = false;

        // 🔷 Read method (called for each record)
        @Override
        public EmployeeDto read() {

            if (rowIterator == null || !rowIterator.hasNext()) {
                return null; // signals end of file
            }

            // Skip header
            if (!isHeaderSkipped) {
                rowIterator.next();
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

            return dto;
        }

        // 🔷 Open file (called before reading starts)
        @Override
        public void open(ExecutionContext executionContext) {

            try {
                InputStream inputStream = new FileInputStream(filePath);
                workbook = WorkbookFactory.create(inputStream);
                sheet = workbook.getSheetAt(0);
                rowIterator = sheet.iterator();
            } catch (Exception e) {
                throw new RuntimeException("Error opening Excel file", e);
            }
        }

        // 🔷 Close file
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

        @Override
        public void update(ExecutionContext executionContext) {
            // not needed for now
        }

        // 🔷 Helper method to safely read cell value
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
