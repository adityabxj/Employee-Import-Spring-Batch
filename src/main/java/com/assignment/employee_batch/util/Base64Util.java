package com.assignment.employee_batch.util;

import java.util.Base64;

public class Base64Util {


    public static byte[] decode(String base64) {

        if (base64 == null || base64.trim().isEmpty()) {
            throw new RuntimeException("Base64 string is empty");
        }

        try {

            if (base64.contains(",")) {
                base64 = base64.substring(base64.indexOf(",") + 1);
            }


            base64 = base64.replaceAll("\\s+", "");


            return Base64.getDecoder().decode(base64);

        } catch (Exception e) {
            throw new RuntimeException("Invalid Base64 format", e);
        }
    }


    public static String encode(byte[] data) {

        if (data == null || data.length == 0) {
            throw new RuntimeException("Data is empty");
        }

        return Base64.getEncoder().encodeToString(data);
    }

    private Base64Util() {}
}