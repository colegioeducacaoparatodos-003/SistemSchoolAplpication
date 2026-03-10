package com.angola_argentina_portal.dto;

import java.time.LocalDate;

public class DocumentTableDTO {

    private int pkDocument;
    private String documentType;
    private String fileName;
    private long fileSize;
    private LocalDate uploadDate;
    private String uploadedBy;

    public DocumentTableDTO(
            int pkDocument,
            String documentType,
            String fileName,
            long fileSize,
            LocalDate uploadDate,
            String uploadedBy) {

        this.pkDocument = pkDocument;
        this.documentType = documentType;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.uploadDate = uploadDate;
        this.uploadedBy = uploadedBy;
    }

    public int getPkDocument() {
        return pkDocument;
    }

    public void setPkDocument(int pkDocument) {
        this.pkDocument = pkDocument;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public LocalDate getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDate uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

}
