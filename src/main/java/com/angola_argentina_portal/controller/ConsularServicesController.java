package com.angola_argentina_portal.controller;

import java.io.IOException;
import java.io.Serializable;

import org.primefaces.model.LazyDataModel;

import com.angola_argentina_portal.dto.ConsularServicesDTO;
import com.angola_argentina_portal.lazy.CosularServiceLazyModel;
import com.angola_argentina_portal.lazy.NewsLazyModel;
import com.angola_argentina_portal.model.ConsularServices;
import com.angola_argentina_portal.model.News;
import com.angola_argentina_portal.service.ConsularService;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class ConsularServicesController implements Serializable {

    private static final long serialVersionUID = 1L;

    private ConsularServices sconsularServices = new ConsularServices();
    private Long selectedId;
    @Inject
    private ConsularService consularService;

    private CosularServiceLazyModel lazyModel;

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
    FacesContext.getCurrentInstance()
            .addMessage(null, new FacesMessage(severity, summary, detail));
}

    public String loadConsular() {
        try {

            lazyModel = new CosularServiceLazyModel(consularService);

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao processar notícias",
                            e.getMessage()));
            e.printStackTrace();
        }
        return "/management/consular-services.xhtml?faces-redirect=true";
    }

    public String loadConsularPage() {
        try {

            lazyModel = new CosularServiceLazyModel(consularService);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao processar notícias",
                            e.getMessage()));
            e.printStackTrace();
        }
        return "/consular-services/news.xhtml?faces-redirect=true";
    }

    public CosularServiceLazyModel getLazyModel() {
        if (lazyModel == null) {
            lazyModel = new CosularServiceLazyModel(consularService);
        }
        return lazyModel;
    }

    public void save() throws IOException {
        consularService.save(sconsularServices);
        sconsularServices = new ConsularServices();
        lazyModel = new CosularServiceLazyModel(consularService);
    }
    
    /* ---------------- CRUD ---------------- */

    // Abre a página ou dialog para editar um serviço
    public void openEditDialog() {
        if (selectedId == null || selectedId == 0) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Nenhum serviço selecionado!", "");
            return;
        }

        // ConsularServicesDTO dto = consularService.().stream()
        //         .filter(n -> n.getId().equals(selectedId))
        //         .findFirst()
        //         .orElse(null);

        // if (dto != null) {
        //     editDto = new ConsularServicesDTO();
        //     editDto.setId(dto.getId());
        //     editDto.setServiceName(dto.getServiceName());
        //     editDto.setRequirements(dto.getRequirements());
        //     editDto.setFees(dto.getFees());
        //     editDto.setPrice(dto.getPrice());
        //     editDto.setDetails(dto.getDetails());
        //     editDto.setStatus(dto.getStatus());
        //     editDto.setAvailableDays(dto.getAvailableDays());
        //     editDto.setOnlineBooking(dto.isOnlineBooking());
        // }
    }

    public Long getSelectedId() {
        return this.selectedId;
    }

    public void setSelectedId(Long selectedId) {
        this.selectedId = selectedId;
    }

    public ConsularService getConsularService() {
        return this.consularService;
    }

    public void setConsularService(ConsularService consularService) {
        this.consularService = consularService;
    }

    public void setLazyModel(CosularServiceLazyModel lazyModel) {
        this.lazyModel = lazyModel;
    }

}
