package com.angola_argentina_portal.controller;

import java.io.Serializable;

import org.primefaces.PrimeFaces;
import org.primefaces.model.LazyDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.angola_argentina_portal.dto.GovernmentDTO;
import com.angola_argentina_portal.lazy.DocumentLazyModel;
import com.angola_argentina_portal.lazy.GovernmentLazyModel;
import com.angola_argentina_portal.model.Government;
import com.angola_argentina_portal.service.GovernmentService;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class GovernmentController implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private Government government = new Government();

    private LazyDataModel<GovernmentDTO> lazyModel;

    @Inject
    private GovernmentService governmentService;

    private Integer selectedId;
    private String selectedType;

    // Método loadDocument para Renderização com a lista de documentos
    public String loadGovernmentPage() {
        try {
            lazyModel = new GovernmentLazyModel(governmentService);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao processar",
                            e.getMessage()));
            e.printStackTrace();
        }
        return "/embassadors.xhtml?faces-redirect=true";
    }
    
    public void add() {

        try {

            governmentService.save(government);

            lazyModel = new GovernmentLazyModel(governmentService);

            government = new Government();

        } catch (Exception e) {
            e.printStackTrace();
            government = new Government();
        }
    }

    
}
