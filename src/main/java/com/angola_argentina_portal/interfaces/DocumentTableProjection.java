package com.angola_argentina_portal.interfaces;

import java.time.LocalDate;

public interface DocumentTableProjection {

    int getPkDocument();

    String getDocumentType();

    String getFileName();

    String getContentType();

    long getFileSize();

    LocalDate getUploadDate();

}