package com.assignment.employee_batch.util;

import java.util.Base64;

public class Base64Util {

    // 🔥 Decode Base64 → byte[]
    public static byte[] decode(String base64) {

        if (base64 == null || base64.trim().isEmpty()) {
            throw new RuntimeException("Base64 string is empty");
        }

        try {
            // 🔥 STEP 1: Remove prefix if present
            // Example: data:application/vnd.ms-excel;base64,XXXXX
            if (base64.contains(",")) {
                base64 = base64.substring(base64.indexOf(",") + 1);
            }

            // 🔥 STEP 2: Remove whitespace/newlines
            base64 = base64.replaceAll("\\s+", "");

            // 🔥 STEP 3: Decode safely
            return Base64.getDecoder().decode(base64);

        } catch (Exception e) {
            throw new RuntimeException("Invalid Base64 format", e);
        }
    }

    // 🔥 Encode byte[] → Base64
    public static String encode(byte[] data) {

        if (data == null || data.length == 0) {
            throw new RuntimeException("Data is empty");
        }

        return Base64.getEncoder().encodeToString(data);
    }

    private Base64Util() {}
}