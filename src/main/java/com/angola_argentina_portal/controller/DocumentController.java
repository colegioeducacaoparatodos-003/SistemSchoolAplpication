package com.angola_argentina_portal.controller;

<<<<<<< HEAD
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
=======
>>>>>>> b55ce8ee43bc8acf20586db32ebb4276593b8c35
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

import com.angola_argentina_portal.dto.DocumentTableDTO;
import com.angola_argentina_portal.lazy.DocumentLazyModel;
import com.angola_argentina_portal.model.Document;
import com.angola_argentina_portal.service.DocumentService;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class DocumentController implements Serializable {


    private static final long serialVersionUID = 1L;

    private Document document = new Document();

    private LazyDataModel<DocumentTableDTO> lazyModel;

<<<<<<< HEAD
    private String referenceType;

    private int referenceId;

=======
>>>>>>> b55ce8ee43bc8acf20586db32ebb4276593b8c35
    private StreamedContent fileToDownload;

    @Inject
    private DocumentService service;

    @Inject
    private UserController loginController;

<<<<<<< HEAD
    public void prepare(String refType, int refId) {

        this.referenceType = refType;
        this.referenceId = refId;

        this.lazyModel =
                new DocumentLazyModel(service, referenceType, referenceId);
    }

    public String loadDocument(String refType, int refId) {

        try {

            this.referenceType = refType;
            this.referenceId = refId;

            lazyModel =
                    new DocumentLazyModel(service, referenceType, referenceId);

=======
    // ================== PREPARAR ==================
    /*
     * public void prepare(String refType, int refId) {
     * this.referenceType = refType;
     * this.referenceId = refId;
     * 
     * // Inicializa a LazyDataModel
     * this.lazyModel = new DocumentLazyModel(service, referenceType, referenceId);
     * }
     */

    // Método loadDocument para Renderização
    public String loadDocument() {
        try {
            lazyModel = new DocumentLazyModel(service);
>>>>>>> b55ce8ee43bc8acf20586db32ebb4276593b8c35
        } catch (Exception e) {

            FacesContext.getCurrentInstance()
                    .addMessage(null,
                            new FacesMessage(
                                    FacesMessage.SEVERITY_ERROR,
                                    "Erro ao processar",
                                    e.getMessage()));

        }

        return "/management/documents.xhtml?faces-redirect=true";
    }

<<<<<<< HEAD
=======
    public void add() {

        try {

            service.save(document);

            lazyModel = new DocumentLazyModel(service);

            document = new Document();

            addMessage(FacesMessage.SEVERITY_INFO,
                    "Document",
                    "Document uploaded successfully");

        } catch (Exception e) {
            e.printStackTrace();

            document = new Document();

            addMessage(FacesMessage.SEVERITY_ERROR,
                    "Document",
                    e.getMessage());
        }
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(severity, summary, detail));
    }

    // ================== UPLOAD ==================
>>>>>>> b55ce8ee43bc8acf20586db32ebb4276593b8c35
    public void upload() {

        try {

            UploadedFile uploaded = document.getUploadedFile();

            if (uploaded == null) {

                FacesContext.getCurrentInstance()
                        .addMessage(null,
                                new FacesMessage(
                                        FacesMessage.SEVERITY_WARN,
                                        "Aviso",
                                        "Selecione um arquivo"));

                return;
            }

<<<<<<< HEAD
            document.setFileName(uploaded.getFileName());
            document.setContentType(uploaded.getContentType());
            document.setFileSize(uploaded.getSize());

            document.setReferenceType(referenceType);
            document.setReferenceId(referenceId);

=======
            // Cria FileDocument
            FileDocument fileDoc = new FileDocument();
            fileDoc.setFileName(uploaded.getFileName());
            fileDoc.setContentType(uploaded.getContentType());
            fileDoc.setSize(uploaded.getSize());
            fileDoc.setCreatedAt(LocalDateTime.now());
            fileDoc.setData(uploaded.getContent());
>>>>>>> b55ce8ee43bc8acf20586db32ebb4276593b8c35
            document.setUploadDate(LocalDate.now());

            // document.setFkUser(loginController.getLoggedUserId());

            service.upload(document);

            document = new Document();

            FacesContext.getCurrentInstance()
                    .addMessage(null,
                            new FacesMessage(
                                    FacesMessage.SEVERITY_INFO,
                                    "Sucesso",
                                    "Arquivo enviado"));

        } catch (Exception e) {

            e.printStackTrace();

            FacesContext.getCurrentInstance()
                    .addMessage(null,
                            new FacesMessage(
                                    FacesMessage.SEVERITY_ERROR,
                                    "Erro",
                                    e.getMessage()));
        }
    }

    public void prepareDownload(int documentId) {

        Document doc = service.findById(documentId);

<<<<<<< HEAD
        fileToDownload = DefaultStreamedContent.builder()
                .name(doc.getFileName())
                .contentType(doc.getContentType())
                .stream(() -> {
                    try {
                        return new FileInputStream(doc.getFilePath());
                    } catch (Exception e) {
                        return null;
                    }
                })
                .build();
=======
        /*
         * if (doc.getFileDocument() != null && doc.getFileDocument().getData() != null)
         * {
         * fileToDownload = DefaultStreamedContent.builder()
         * .name(doc.getFileDocument().getFileName())
         * .contentType(doc.getFileDocument().getContentType())
         * .stream(() -> new ByteArrayInputStream(doc.getFileDocument().getData()))
         * .build();
         * }
         */
>>>>>>> b55ce8ee43bc8acf20586db32ebb4276593b8c35
    }

    public LazyDataModel<DocumentTableDTO> getLazyModel() {
        return lazyModel;
    }

    public Document getDocument() {
        return document;
    }

<<<<<<< HEAD
    public StreamedContent getFileToDownload() {
        return fileToDownload;
    }
=======
    // ================== SETTERS ==================
    public void setDocument(Document document) {
        this.document = document;
    }

>>>>>>> b55ce8ee43bc8acf20586db32ebb4276593b8c35
}
