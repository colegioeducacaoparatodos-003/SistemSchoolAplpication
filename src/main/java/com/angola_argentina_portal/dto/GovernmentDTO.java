package com.angola_argentina_portal.dto;

import java.time.LocalDateTime;

public class GovernmentDTO {

    private Long id;
    private String fullName;
    private String type;
    private String title;
    private String subTitle;
    private String description;

    public GovernmentDTO(Long id, String fullName, String type,
            String title, String subTitle, String description) {
        this.id = id;
        this.fullName = fullName;
        this.type = type;
        this.title = title;
        this.subTitle = subTitle;
        this.description = description;
    }

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

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
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

}
