package com.angola_argentina_portal.model;

import java.time.LocalDate;

import org.primefaces.model.file.UploadedFile;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;


@Entity
@Table(name = "document")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pkDocument;

    private String referenceType;
    // ASSET | CONTRACT | EMPLOYEE | PAYROLL | OTHER

    private int referenceId;

    private String documentType;
    // CONTRACT | INVOICE | MANUAL | PHOTO | REPORT

    private String fileName;
    private String filePath;
    private String contentType;

    private long fileSize;

    private LocalDate uploadDate;

    private int fkUser;

    @Transient
    private UploadedFile uploadedFile;

    public Document() {
        super();
    }

    public int getPkDocument() {
        return pkDocument;
    }

    public void setPkDocument(int pkDocument) {
        this.pkDocument = pkDocument;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public int getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(int referenceId) {
        this.referenceId = referenceId;
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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
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

    public int getFkUser() {
        return fkUser;
    }


}
