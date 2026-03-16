package com.angola_argentina_portal.controller;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

import com.angola_argentina_portal.dto.DocumentTableDTO;
import com.angola_argentina_portal.lazy.DestinationLazyModel;
import com.angola_argentina_portal.lazy.DocumentLazyModel;
import com.angola_argentina_portal.model.Document;
import com.angola_argentina_portal.model.FileDocument;
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

    private String referenceType;
    private int referenceId;

    private StreamedContent fileToDownload;

    @Inject
    private DocumentService service;

    @Inject
    private UserController loginController;

    // public String loadDocumentPage() {
    //     try {
    //         lazyModel = new LazyDataModel(service);
    //     } catch (Exception e) {
    //         FacesContext.getCurrentInstance().addMessage(null,
    //                 new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao processar",
    //                         e.getMessage()));
    //         e.printStackTrace();
    //     }
    //     return "/management/docment.xhtml?faces-redirect=true";
    // }

    // ================== PREPARAR ==================
    public void prepare(String refType, int refId) {
        this.referenceType = refType;
        this.referenceId = refId;

        // Inicializa a LazyDataModel
        this.lazyModel = new DocumentLazyModel(service, referenceType, referenceId);
    }


       public String loadDocument() {
        try {
          // lazyModel = new DocumentLazyModel(service);
          
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao processar",
                            e.getMessage()));
            e.printStackTrace();
        }
        return "/management/documents.xhtml?faces-redirect=true";
    }

    // ================== UPLOAD ==================
    public void upload() {
        try {
            UploadedFile uploaded = document.getUploadedFile();

            if (uploaded == null) {
                FacesContext.getCurrentInstance()
                        .addMessage(null,
                                new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "Selecione um arquivo!"));
                return;
            }

            // Cria FileDocument
            FileDocument fileDoc = new FileDocument();
            fileDoc.setFileName(uploaded.getFileName());
            fileDoc.setContentType(uploaded.getContentType());
            fileDoc.setSize(uploaded.getSize());
            fileDoc.setCreatedAt(LocalDateTime.now());
            fileDoc.setData(uploaded.getContent());

            // Associa à Document
            document.setFileDocument(fileDoc);

            document.setReferenceType(referenceType);
            document.setReferenceId(referenceId);
            document.setUploadDate(LocalDate.now());
            // document.setFkUser(loginController.getLoggedUserId());

            // Salva no banco
            service.upload(document);

            // Reset
            document = new Document();
            FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Arquivo enviado!"));

        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro",
                            "Falha no upload: " + e.getMessage()));
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
