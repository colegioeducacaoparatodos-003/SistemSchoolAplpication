package com.angola_argentina_portal.controller;

import java.io.Serializable;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import org.primefaces.model.LazyDataModel;

import com.angola_argentina_portal.dto.ConsularServiceTableDTO;
import com.angola_argentina_portal.lazy.ConsularServiceLazyModel;
import com.angola_argentina_portal.lazy.NewsLazyModel;
import com.angola_argentina_portal.model.ConsularService;
import com.angola_argentina_portal.service.ConsularServiceService;

@Named
@ViewScoped
public class ConsularServiceController implements Serializable {

    private static final long serialVersionUID = 1L;

    private ConsularService consularService = new ConsularService();
    private LazyDataModel<ConsularServiceTableDTO> lazyModel;

    @Inject
    private ConsularServiceService service;

    public void add() {
        try {

            service.save(consularService);

            // lazyModel = new ConsularServiceLazyModel(service);
            consularService = new ConsularService();

            addMessage(FacesMessage.SEVERITY_INFO,
                    "Consular Service",
                    "Saved successfully");

        } catch (Exception e) {
            e.printStackTrace();

            consularService = new ConsularService();

            addMessage(FacesMessage.SEVERITY_ERROR,
                    "Consular Service",
                    e.getMessage());
        }
    }

    public String loadConsularService() {
        try {

            lazyModel = new ConsularServiceLazyModel(service);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao processar notícias",
                            e.getMessage()));
            e.printStackTrace();
        }
        return "/management/consular-services.xhtml?faces-redirect=true";
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(severity, summary, detail));
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public ConsularService getConsularService() {
        return consularService;
    }

    public void setConsularService(ConsularService consularService) {
        this.consularService = consularService;
    }

    public LazyDataModel<ConsularServiceTableDTO> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<ConsularServiceTableDTO> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public ConsularServiceService getService() {
        return service;
    }

    public void setService(ConsularServiceService service) {
        this.service = service;
    }

    // getters and setters
}