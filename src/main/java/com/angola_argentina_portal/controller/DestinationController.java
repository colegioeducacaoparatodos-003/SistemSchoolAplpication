package com.angola_argentina_portal.controller;

import java.io.Serializable;
import java.util.List;

import org.primefaces.model.file.UploadedFile;

import com.angola_argentina_portal.io.Assistant;
import com.angola_argentina_portal.io.FileImage;
import com.angola_argentina_portal.lazy.DestinationLazyModel;
import com.angola_argentina_portal.lazy.NewsLazyModel;
import com.angola_argentina_portal.model.Destination;
import com.angola_argentina_portal.service.DestinationService;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class DestinationController implements Serializable {

    private static final long serialVersionUID = 1L;

    private Destination destination = new Destination();

    private DestinationLazyModel lazyModel;

    private UploadedFile imageUpload;

    private List<Destination> destinations;

    @Inject
    private DestinationService service;

    public String loadDestinationPage() {
        destinations = service.findAll();
        return "/destination.xhtml?faces-redirect=true";
    }

    public List<Destination> getDestinations() {
        return destinations;

    }

    public String load() {
        try {

            lazyModel = new DestinationLazyModel(service);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao processar",
                            e.getMessage()));
            e.printStackTrace();
        }
        return "/management/destination.xhtml?faces-redirect=true";
    }

    public void add() {

        try {

            FileImage acessImage = new FileImage();
            Assistant assistant = new Assistant();

            String newNameFile = "default.png";

            if (imageUpload != null) {

                newNameFile = "0" + assistant.novoNome(imageUpload.getContentType());

                acessImage.salvarArquivo(
                        imageUpload,
                        "destination_images",
                        newNameFile);
            }

            destination.setImageUrl(newNameFile);

            service.save(destination);

            destination = new Destination();

            load();

            addMessage(FacesMessage.SEVERITY_INFO,
                    "Destination",
                    "Saved successfully");

        } catch (Exception e) {

            addMessage(FacesMessage.SEVERITY_ERROR,
                    "Destination",
                    e.getMessage());
        }
    }

    public void update() {

        try {

            service.update(destination);

            destination = new Destination();

            load();

            addMessage(FacesMessage.SEVERITY_INFO,
                    "Destination",
                    "Updated successfully");

        } catch (Exception e) {

            addMessage(FacesMessage.SEVERITY_ERROR,
                    "Destination",
                    e.getMessage());
        }
    }

    public void delete(Long id) {

        try {

            service.delete(id);

            load();

            addMessage(FacesMessage.SEVERITY_INFO,
                    "Destination",
                    "Deleted successfully");

        } catch (Exception e) {

            addMessage(FacesMessage.SEVERITY_ERROR,
                    "Destination",
                    e.getMessage());
        }
    }

    private void addMessage(FacesMessage.Severity severity,
            String title,
            String message) {

        FacesContext.getCurrentInstance()
                .addMessage(null,
                        new FacesMessage(severity, title, message));
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public UploadedFile getImageUpload() {
        return imageUpload;
    }

    public void setImageUpload(UploadedFile imageUpload) {
        this.imageUpload = imageUpload;
    }

    public DestinationService getService() {
        return service;
    }

    public void setService(DestinationService service) {
        this.service = service;
    }

    public DestinationLazyModel getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(DestinationLazyModel lazyModel) {
        this.lazyModel = lazyModel;
    }

    // getters and setters
}