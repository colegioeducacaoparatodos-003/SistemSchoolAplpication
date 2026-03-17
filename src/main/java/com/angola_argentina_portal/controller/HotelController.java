package com.angola_argentina_portal.controller;

import java.io.Serializable;
import java.util.List;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.file.UploadedFile;

import com.angola_argentina_portal.dto.HotelTableDTO;
import com.angola_argentina_portal.io.Assistant;
import com.angola_argentina_portal.io.FileImage;
import com.angola_argentina_portal.lazy.HotelLazyModel;
import com.angola_argentina_portal.model.Hotel;
import com.angola_argentina_portal.service.HotelService;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class HotelController implements Serializable {

    private static final long serialVersionUID = 1L;

    private Hotel hotel = new Hotel();

    private HotelLazyModel lazyModel;

    private UploadedFile imageUpload;
    private List<Hotel> hotels;

    @Inject
    private HotelService service;

    public String load() {
        try {
            lazyModel = new HotelLazyModel(service);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao processar",
                            e.getMessage()));
            e.printStackTrace();
        }
        return "/management/hotel.xhtml?faces-redirect=true";
    }

    public String loadHotelsPage() {
        hotels = service.findAll();
        return "/hotel.xhtml?faces-redirect=true";
    }

    public void add() {

        try {

            FileImage acessImage = new FileImage();
            Assistant assistant = new Assistant();

            String newNameFile = "default.png";

            if (imageUpload != null) {

                newNameFile = "0" +
                        assistant.novoNome(imageUpload.getContentType());

                acessImage.salvarArquivo(
                        imageUpload,
                        "hotel_images",
                        newNameFile);
            }

            hotel.setImageUrl(newNameFile);

            service.save(hotel);

            hotel = new Hotel();

            load();

            addMessage(FacesMessage.SEVERITY_INFO,
                    "Hotel",
                    "Saved successfully");

        } catch (Exception e) {

            addMessage(FacesMessage.SEVERITY_ERROR,
                    "Hotel",
                    e.getMessage());
        }
    }

    public void update() {

        try {

            service.update(hotel);

            hotel = new Hotel();

            load();

            addMessage(FacesMessage.SEVERITY_INFO,
                    "Hotel",
                    "Updated successfully");

        } catch (Exception e) {

            addMessage(FacesMessage.SEVERITY_ERROR,
                    "Hotel",
                    e.getMessage());
        }
    }

    public void delete(Long id) {

        try {

            service.delete(id);

            load();

            addMessage(FacesMessage.SEVERITY_INFO,
                    "Hotel",
                    "Deleted successfully");

        } catch (Exception e) {

            addMessage(FacesMessage.SEVERITY_ERROR,
                    "Hotel",
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

    public LazyDataModel<HotelTableDTO> getLazyModel() {
        return lazyModel;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public UploadedFile getImageUpload() {
        return imageUpload;
    }

    public void setImageUpload(UploadedFile imageUpload) {
        this.imageUpload = imageUpload;
    }

    public void setLazyModel(HotelLazyModel lazyModel) {
        this.lazyModel = lazyModel;
    }

    public List<Hotel> getHotels() {
        return hotels;
    }

    public void setHotels(List<Hotel> hotels) {
        this.hotels = hotels;
    }

}