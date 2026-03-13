package com.angola_argentina_portal.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "announcement")
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(nullable = false, length = 2000)
    private String description;
    private String imageUrl;

    private LocalDateTime createdAt;

    public Announcement() {
    }

    public Announcement(Long id, String title, String description, String imageUrl, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Announcement id(Long id) {
        setId(id);
        return this;
    }

    public Announcement title(String title) {
        setTitle(title);
        return this;
    }

    public Announcement description(String description) {
        setDescription(description);
        return this;
    }

    public Announcement imageUrl(String imageUrl) {
        setImageUrl(imageUrl);
        return this;
    }

    public Announcement createdAt(LocalDateTime createdAt) {
        setCreatedAt(createdAt);
        return this;
    }

    // @Override
    // public boolean equals(Object o) {
    //     return EqualsBuilder.reflectionEquals(this, o);
    // }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, imageUrl, createdAt);
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", title='" + getTitle() + "'" +
                ", description='" + getDescription() + "'" +
                ", imageUrl='" + getImageUrl() + "'" +
                ", createdAt='" + getCreatedAt() + "'" +
                "}";
    }

}
