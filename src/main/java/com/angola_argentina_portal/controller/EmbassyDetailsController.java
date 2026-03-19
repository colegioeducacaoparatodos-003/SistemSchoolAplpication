package com.angola_argentina_portal.controller;

import java.io.Serializable;

import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named
@ViewScoped
public class EmbassyDetailsController implements Serializable {

    private String country;

    private String name;
    private String image;
    private String location;

    public void loadData() {

        if (country == null) {
            return;
        }

        switch (country) {
            case "argentina":
                name = FacesContext.getCurrentInstance()
                        .getApplication()
                        .evaluateExpressionGet(
                                FacesContext.getCurrentInstance(),
                                "#{msg.diplomaticMissionArgentina}",
                                String.class);
                image = "argentina-place.jpg";
                location = "Luanda, Angola";
                break;

            case "chile":
                name = "Missão Diplomática no Chile";
                image = "chile-place.jpg";
                location = "Luanda, Angola";
                break;

            case "bolivia":
                name = "Missão Diplomática na Bolívia";
                image = "/resources/imgs/bolivia-place.jpg";
                location = "Luanda, Angola";
                break;
        }
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    // getters
}