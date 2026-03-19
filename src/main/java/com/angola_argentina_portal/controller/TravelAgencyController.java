package com.angola_argentina_portal.controller;

import java.io.Serializable;
import java.util.List;

import org.primefaces.model.file.UploadedFile;

import com.angola_argentina_portal.io.Assistant;
import com.angola_argentina_portal.io.FileImage;
import com.angola_argentina_portal.lazy.AirlineLazyModel;
import com.angola_argentina_portal.lazy.TravelAgencyLazyModel;
import com.angola_argentina_portal.model.TravelAgency;
import com.angola_argentina_portal.service.TravelAgencyService;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class TravelAgencyController implements Serializable {

    private static final long serialVersionUID = 1L;

    private TravelAgency agency = new TravelAgency();

    private TravelAgencyLazyModel lazyModel;

    private UploadedFile logoUpload;

    private List<TravelAgency> destinations;

    @Inject
    private TravelAgencyService service;

    public String loadTravelAgenciesPage() {
        try {
            lazyModel = new TravelAgencyLazyModel(service);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao processar",
                            e.getMessage()));
            e.printStackTrace();
        }
        return "/travel-agencies.xhtml?faces-redirect=true";
    }

    public String load() {
        try {
            lazyModel = new TravelAgencyLazyModel(service);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao processar",
                            e.getMessage()));
            e.printStackTrace();
        }
        return "/management/travel-agencies.xhtml?faces-redirect=true";
    }

    public void add() {

        try {

            FileImage acessImage = new FileImage();
            Assistant assistant = new Assistant();

            String newNameFile = "default.png";

            if (logoUpload != null) {

                newNameFile = "0" +
                        assistant.novoNome(logoUpload.getContentType());

                acessImage.salvarArquivo(
                        logoUpload,
                        "travel_agencies_images",
                        newNameFile);
            }

            agency.setLogoUrl(newNameFile);

            service.save(agency);

            agency = new TravelAgency();

            load();

            addMessage(FacesMessage.SEVERITY_INFO,
                    "Travel Agency",
                    "Saved successfully");

        } catch (Exception e) {

            addMessage(FacesMessage.SEVERITY_ERROR,
                    "Error saving Travel Agency",
                    e.getMessage());
        }
    }

    public void update() {

        try {

            service.update(agency);

            agency = new TravelAgency();

            load();

            addMessage(FacesMessage.SEVERITY_INFO,
                    "Travel Agency",
                    "Updated successfully");

        } catch (Exception e) {

            addMessage(FacesMessage.SEVERITY_ERROR,
                    "Travel Agency",
                    e.getMessage());
        }
    }

    public void delete(Long id) {

        try {

            service.delete(id);
            
            load();

            addMessage(FacesMessage.SEVERITY_INFO,
                    "Travel Agency",
                    "Deleted successfully");

        } catch (Exception e) {

            addMessage(FacesMessage.SEVERITY_ERROR,
                    "Travel Agency",
                    e.getMessage());
        }
    }

    private void addMessage(FacesMessage.Severity severity,
            String title,
            String message) {

        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(severity, title, message));
    }

    // getters and setters

    public TravelAgencyLazyModel getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(TravelAgencyLazyModel lazyModel) {
        this.lazyModel = lazyModel;
    }

    public TravelAgency getAgency() {
        return agency;
    }

    public void setAgency(TravelAgency agency) {
        this.agency = agency;
    }

    public UploadedFile getLogoUpload() {
        return logoUpload;
    }

    public void setLogoUpload(UploadedFile logoUpload) {
        this.logoUpload = logoUpload;
    }

    public List<TravelAgency> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<TravelAgency> destinations) {
        this.destinations = destinations;
    }

}