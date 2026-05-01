package com.assignment.employee_batch.util;

import java.util.Base64;

public class Base64Util {

    // 🔷 Decode Base64 → byte[]
    public static byte[] decode(String base64) {
        return Base64.getDecoder().decode(base64);
    }

    // 🔷 Encode byte[] → Base64
    public static String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    private Base64Util() {}
}
