package com.angola_argentina_portal.controller;

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

    private StreamedContent fileToDownload;

    @Inject
    private DocumentService service;

    @Inject
    private UserController loginController;

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

            document.setFileName(uploaded.getFileName());
            document.setContentType(uploaded.getContentType());
            document.setFileSize(uploaded.getSize());


            // Cria FileDocument
            /*
             * FileDocument fileDoc = new FileDocument();
             * fileDoc.setFileName(uploaded.getFileName());
             * fileDoc.setContentType(uploaded.getContentType());
             * fileDoc.setSize(uploaded.getSize());
             * fileDoc.setCreatedAt(LocalDateTime.now());
             * fileDoc.setData(uploaded.getContent());
             * document.setUploadDate(LocalDate.now());
             */
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
    }

    public LazyDataModel<DocumentTableDTO> getLazyModel() {
        return lazyModel;
    }

    public Document getDocument() {
        return document;
    }

    public StreamedContent getFileToDownload() {
        return fileToDownload;
    }
    // ================== SETTERS ==================
    public void setDocument(Document document) {
        this.document = document;
    }

}
