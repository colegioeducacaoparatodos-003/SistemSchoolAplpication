package com.angola_argentina_portal.controller;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.time.LocalDate;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;

import com.angola_argentina_portal.dto.DocumentTableDTO;
import com.angola_argentina_portal.lazy.DocumentLazyModel;
import com.angola_argentina_portal.model.Document;
import com.angola_argentina_portal.service.DocumentService;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class DocumentController implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Document document = new Document();

    private LazyDataModel<DocumentTableDTO> lazyModel;

    private String referenceType;
    private int referenceId;

    private StreamedContent fileToDownload;

    @Inject
    private DocumentService service;

    @Inject
    private UserController loginController;

    // ================== PREPARAR ==================
    public void prepare(String refType, int refId) {
        this.referenceType = refType;
        this.referenceId = refId;

        // Inicializa a LazyDataModel
        this.lazyModel = new DocumentLazyModel(service, referenceType, referenceId);
    }

    // ================== UPLOAD ==================
    public void upload() {
        try {
            document.setReferenceType(referenceType);
            document.setReferenceId(referenceId);
            document.setUploadDate(LocalDate.now());
            // document.setFkUser(loginController.getLoggedUserId());

            // Salva no banco (assumindo que o arquivo já está associado ao FileDocument)
            service.upload(document);

            // Reset
            document = new Document();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================== DOWNLOAD ==================
    public void prepareDownload(int documentId) {
        Document doc = service.findById(documentId);

        if (doc.getFileDocument() != null && doc.getFileDocument().getData() != null) {
            fileToDownload = DefaultStreamedContent.builder()
                    .name(doc.getFileDocument().getFileName())
                    .contentType(doc.getFileDocument().getContentType())
                    .stream(() -> new ByteArrayInputStream(doc.getFileDocument().getData()))
                    .build();
        }
    }

    public StreamedContent getFileToDownload() {
        return fileToDownload;
    }

    // ================== GETTERS ==================
    public LazyDataModel<DocumentTableDTO> getLazyModel() {
        return lazyModel;
    }

    public Document getDocument() {
        return document;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public int getReferenceId() {
        return referenceId;
    }

    // ================== SETTERS ==================
    public void setDocument(Document document) {
        this.document = document;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public void setReferenceId(int referenceId) {
        this.referenceId = referenceId;
    }
}
