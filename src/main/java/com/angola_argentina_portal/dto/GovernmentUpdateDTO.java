package com.angola_argentina_portal.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class GovernmentUpdateDTO {

    private Long id;
    private String fullName;
    private String typeEm;
    private String title;
    private String subTitle;
    private String description;
    private LocalDateTime inicio;
    private LocalDateTime termino;

    public GovernmentUpdateDTO() {
    }

    // getters and setters

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTypeEm() {
        return this.typeEm;
    }

    public void setTypeEm(String typeEm) {
        this.typeEm = typeEm;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return this.subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getInicio() {
        return this.inicio;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public LocalDateTime getTermino() {
        return this.termino;
    }

    public void setTermino(LocalDateTime termino) {
        this.termino = termino;
    }

}
