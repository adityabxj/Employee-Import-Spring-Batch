package com.assignment.employee_batch.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {


    public static File createTempFile(byte[] data, String fileType) {
        try {
            File file = File.createTempFile("emp_", "." + fileType);
            Files.write(file.toPath(), data);
            return file;
        } catch (Exception e) {
            throw new RuntimeException("Error creating temp file", e);
        }
    }


    public static byte[] readFile(String path) {
        try {
            return Files.readAllBytes(Paths.get(path));
        } catch (Exception e) {
            throw new RuntimeException("Error reading file", e);
        }
    }


    public static void deleteFile(String path) {
        try {
            Files.deleteIfExists(Paths.get(path));
        } catch (Exception e) {
            System.out.println("File deletion failed: " + e.getMessage());
        }
    }

    private FileUtil() {}
}
