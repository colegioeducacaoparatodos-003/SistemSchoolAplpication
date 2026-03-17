package com.angola_argentina_portal.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.util.Objects;

import org.apache.commons.lang3.builder.EqualsBuilder;

@Entity
@Table(name = "government_entities")
public class Government {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    private String type;

    private String title;

    private String subTitle;


    private String description;


    public Government() {
    }

    public Government(Long id, String fullName, String type, String title, String subTitle, String description) {
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

    public String getTypeEm() {
        return this.type;
    }

    public void setTypeEm(String type) {
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

    public Government id(Long id) {
        setId(id);
        return this;
    }

    public Government fullName(String fullName) {
        setFullName(fullName);
        return this;
    }

    public Government typeEm(String typeEm) {
        setTypeEm(typeEm);
        return this;
    }

    public Government title(String title) {
        setTitle(title);
        return this;
    }

    public Government subTitle(String subTitle) {
        setSubTitle(subTitle);
        return this;
    }

    public Government description(String description) {
        setDescription(description);
        return this;
    }
    
    
}
