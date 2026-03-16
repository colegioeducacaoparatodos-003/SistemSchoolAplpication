package com.angola_argentina_portal.controller;

import java.time.LocalDate;

import org.primefaces.model.LazyDataModel;

import com.angola_argentina_portal.dto.DocumentTableDTO;
import com.angola_argentina_portal.model.Document;
import com.angola_argentina_portal.service.DocumentService;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class DocumentController {

    private Document document = new Document();

    private LazyDataModel<DocumentTableDTO> lazyModel;
    private int referenceId;

    @Inject
    private DocumentService service;

    @Inject
    private UserController loginController;

    public String loadDocumentPage() {
        try {
            // loadLazy();
        } catch (Exception e) {
            // FacesMessageUtil.errorMessage("Erro " + e.getMessage());
            e.printStackTrace();
        }
        return "/management/documents.xhtml?faces-redirect=true";
    }

    public void prepare(String refType, int refId) {
        // this.referenceType = refType;
        // this.referenceId = refId;

        // this.lazyModel = new DocumentLazyModel(
        // service, referenceType, referenceId);
    }

    public void save() {
        try {
            // document.setReferenceType(referenceType);
            document.setReferenceId(referenceId);
            document.setUploadDate(LocalDate.now());
            // document.setFkUser(loginController.getLoggedUserId());

            // aqui assumes que o ficheiro já foi salvo no filesystem
            service.save(document);
            document = new Document();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LazyDataModel<DocumentTableDTO> getLazyModel() {
        return lazyModel;
    }

    public Document getDocument() {
        return document;
    }

}
