package com.angola_argentina_portal.interfaces;

import java.time.LocalDate;

public interface DocumentTableProjection {
    
    int getPkDocument();

    String getDocumentType();

    String getFileName();

    long getFileSize();

    LocalDate getUploadDate();

    String getUploadedBy();
}
