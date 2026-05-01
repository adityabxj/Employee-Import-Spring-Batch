package com.assignment.employee_batch.dto;

public class UploadRequest {

    private String file;      // base64
    private String fileType;  // csv or xls

    public UploadRequest() {}

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
