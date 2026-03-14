package com.angola_argentina_portal.controller;

import java.io.Serializable;

import org.primefaces.model.file.UploadedFile;

import com.angola_argentina_portal.io.Assistant;
import com.angola_argentina_portal.io.FileImage;
import com.angola_argentina_portal.lazy.AirlineLazyModel;
import com.angola_argentina_portal.model.Airline;
import com.angola_argentina_portal.service.AirlineService;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class AirlineController implements Serializable {

    private Airline airline = new Airline();

    private AirlineLazyModel lazyModel;

    private UploadedFile logoUpload;

    @Inject
    private AirlineService service;

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
                        "airlines",
                        newNameFile);
            }

            airline.setLogoUrl(newNameFile);

            service.save(airline);

            airline = new Airline();

            load();

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_INFO,
                            "Airline",
                            "Saved successfully"));

        } catch (Exception e) {

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            "Airline",
                            e.getMessage()));
        }
    }

    public void load() {
        lazyModel = new AirlineLazyModel(service);
    }

    public Airline getAirline() {
        return airline;
    }

    public AirlineLazyModel getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(AirlineLazyModel lazyModel) {
        this.lazyModel = lazyModel;
    }

    public void setLogoUpload(UploadedFile logoUpload) {
        this.logoUpload = logoUpload;
    }

    public UploadedFile getLogoUpload() {
        return logoUpload;
    }

    // getters and setters
}