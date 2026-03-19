package com.angola_argentina_portal.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

import com.angola_argentina_portal.dto.DocumentTableDTO;
import com.angola_argentina_portal.lazy.DocumentLazyModel;
import com.angola_argentina_portal.model.Document;
import com.angola_argentina_portal.service.DocumentService;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

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

    private Integer selectedId;
    private String documentType;
    private List<DocumentTableDTO> documents;

    // Método loadDocument para Renderização
    public String loadDocument() {
        try {
            lazyModel = new DocumentLazyModel(service);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao processar",
                            e.getMessage()));
            e.printStackTrace();
        }
        return "/management/documents.xhtml?faces-redirect=true";
    }

    // Método loadDocument para Renderização com a lista de documentos
    public String loadDocumentPage() {
        try {
            lazyModel = new DocumentLazyModel(service);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao processar",
                            e.getMessage()));
            e.printStackTrace();
        }
        return "/documents.xhtml?faces-redirect=true";
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
                                new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "Selecione um arquivo!"));
                return;
            }

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

    // ================== List for Type ==================
    public void documentType() {
        try {

        } catch (Exception e) {

        }
    }

    // ================== DOWNLOAD ==================
    /*
     * public void downloadDocument() {
     * FacesContext facesContext = FacesContext.getCurrentInstance();
     * 
     * Map<String, Object> parameters = new HashMap<>();
     * parameters.put("DOCUMENT_ID", selectedId);
     * 
     * try {
     * 
     * Document document = service.findById(selectedId);
     * 
     * facesContext.getExternalContext().setResponseContentType(document.
     * getDocumentType());
     * facesContext.getExternalContext().setResponseHeader(
     * "Content-Disposition",
     * "attachment; filename=" + document.getFileName());
     * } catch (Exception e) {
     * System.out.println("Falha ao fazer o download" + selectedId);
     * }
     * 
     * }
     */

    public void downloadDocument() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext ec = facesContext.getExternalContext();

        try {

            Document document = service.findById(selectedId);

            String basePath = FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getRealPath("/documents_files");

            File file = new File(basePath, document.getFileName() + ".pdf");

            System.out.println("FULL PATH: " + file.getAbsolutePath());
            System.out.println("EXISTS: " + file.exists());

            if (!file.exists()) {
                throw new RuntimeException("Ficheiro não encontrado!");
            }

            try (InputStream fileStream = new FileInputStream(file);
                    OutputStream out = ec.getResponseOutputStream()) {

                ec.responseReset();

                ec.setResponseContentType(document.getContentType());
                ec.setResponseContentLength((int) file.length());

                ec.setResponseHeader(
                        "Content-Disposition",
                        "attachment; filename=\"" + document.getFileName() + ".pdf\"");

                // equivalente ao JasperExportManager
                byte[] buffer = new byte[1024];
                int bytesRead;

                while ((bytesRead = fileStream.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }

                out.flush();

                facesContext.responseComplete();
            }

        } catch (Exception e) {
            e.printStackTrace();
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

    // ================== SETTERS ==================
    public void setDocument(Document document) {
        this.document = document;
    }

    public Integer getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(Integer selectedId) {
        this.selectedId = selectedId;
    }

    public void setLazyModel(LazyDataModel<DocumentTableDTO> lazyModel) {
        this.lazyModel = lazyModel;
    }
    public void setFileToDownload(StreamedContent fileToDownload) {
        this.fileToDownload = fileToDownload;
    }

    public DocumentService getService() {
        return this.service;
    }

    public void setService(DocumentService service) {
        this.service = service;
    }

    public UserController getLoginController() {
        return this.loginController;
    }

    public void setLoginController(UserController loginController) {
        this.loginController = loginController;
    }

    public String getSearchDocumentType() {
        return this.documentType;
    }

    public void setSearchDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public List<DocumentTableDTO> getDocuments() {
        return this.documents;
    }

    public void setDocuments(List<DocumentTableDTO> documents) {
        this.documents = documents;
    }

}