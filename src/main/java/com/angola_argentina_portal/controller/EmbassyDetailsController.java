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

    private String title;
    private String info;
    private String cooperationDate;

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
                title = FacesContext.getCurrentInstance()
                        .getApplication()
                        .evaluateExpressionGet(
                                FacesContext.getCurrentInstance(),
                                "#{msg.titleArgentina}",
                                String.class);
                info = FacesContext.getCurrentInstance()
                        .getApplication()
                        .evaluateExpressionGet(
                                FacesContext.getCurrentInstance(),
                                "#{msg.informationArgentina}",
                                String.class);
                cooperationDate = FacesContext.getCurrentInstance()
                        .getApplication()
                        .evaluateExpressionGet(
                                FacesContext.getCurrentInstance(),
                                "#{msg.cooperationArgentina}",
                                String.class);
                break;

            case "chile":
                name = FacesContext.getCurrentInstance()
                        .getApplication()
                        .evaluateExpressionGet(
                                FacesContext.getCurrentInstance(),
                                "#{msg.diplomaticMissionChile}",
                                String.class);
                image = "chile-place.jpg";
                location = "Luanda, Angola";
                title = FacesContext.getCurrentInstance()
                        .getApplication()
                        .evaluateExpressionGet(
                                FacesContext.getCurrentInstance(),
                                "#{msg.titleChile}",
                                String.class);
                info = FacesContext.getCurrentInstance()
                        .getApplication()
                        .evaluateExpressionGet(
                                FacesContext.getCurrentInstance(),
                                "#{msg.informationChile}",
                                String.class);
                cooperationDate = FacesContext.getCurrentInstance()
                        .getApplication()
                        .evaluateExpressionGet(
                                FacesContext.getCurrentInstance(),
                                "#{msg.cooperationChile}",
                                String.class);
                break;

            case "bolivia":
                name = FacesContext.getCurrentInstance()
                        .getApplication()
                        .evaluateExpressionGet(
                                FacesContext.getCurrentInstance(),
                                "#{msg.diplomaticMissionBolivia}",
                                String.class);
                image = "bolivia-place.jpg";
                location = "Luanda, Angola";
                title = FacesContext.getCurrentInstance()
                        .getApplication()
                        .evaluateExpressionGet(
                                FacesContext.getCurrentInstance(),
                                "#{msg.titleBolivia}",
                                String.class);
                info = FacesContext.getCurrentInstance()
                        .getApplication()
                        .evaluateExpressionGet(
                                FacesContext.getCurrentInstance(),
                                "#{msg.informationBolivia}",
                                String.class);
                cooperationDate = FacesContext.getCurrentInstance()
                        .getApplication()
                        .evaluateExpressionGet(
                                FacesContext.getCurrentInstance(),
                                "#{msg.cooperationBolivia}",
                                String.class);
                break;

            case "uruguay":
                name = FacesContext.getCurrentInstance()
                        .getApplication()
                        .evaluateExpressionGet(
                                FacesContext.getCurrentInstance(),
                                "#{msg.diplomaticMissionUruguay}",
                                String.class);
                image = "uruguay-place.jpg";
                location = "Luanda, Angola";
                title = FacesContext.getCurrentInstance()
                        .getApplication()
                        .evaluateExpressionGet(
                                FacesContext.getCurrentInstance(),
                                "#{msg.titleUruguay}",
                                String.class);
                info = FacesContext.getCurrentInstance()
                        .getApplication()
                        .evaluateExpressionGet(
                                FacesContext.getCurrentInstance(),
                                "#{msg.informationUruguay}",
                                String.class);
                cooperationDate = FacesContext.getCurrentInstance()
                        .getApplication()
                        .evaluateExpressionGet(
                                FacesContext.getCurrentInstance(),
                                "#{msg.cooperationUruguay}",
                                String.class);
                break;

            case "paraguay":
                name = FacesContext.getCurrentInstance()
                        .getApplication()
                        .evaluateExpressionGet(
                                FacesContext.getCurrentInstance(),
                                "#{msg.diplomaticMissionParaguay}",
                                String.class);
                image = "paraguay-place.jpg";
                location = "Luanda, Angola";
                title = FacesContext.getCurrentInstance()
                        .getApplication()
                        .evaluateExpressionGet(
                                FacesContext.getCurrentInstance(),
                                "#{msg.titleParaguay}",
                                String.class);
                info = FacesContext.getCurrentInstance()
                        .getApplication()
                        .evaluateExpressionGet(
                                FacesContext.getCurrentInstance(),
                                "#{msg.informationParaguay}",
                                String.class);
                cooperationDate = FacesContext.getCurrentInstance()
                        .getApplication()
                        .evaluateExpressionGet(
                                FacesContext.getCurrentInstance(),
                                "#{msg.cooperationParaguay}",
                                String.class);
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

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return this.info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getCooperationDate() {
        return this.cooperationDate;
    }

    public void setCooperationDate(String cooperationDate) {
        this.cooperationDate = cooperationDate;
    }

    // getters
}