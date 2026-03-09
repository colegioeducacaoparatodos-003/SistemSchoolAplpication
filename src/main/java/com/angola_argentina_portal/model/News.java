package com.angola_argentina_portal.model;

import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.checkerframework.checker.units.qual.C;

import com.angola_argentina_portal.dto.NewsStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "news")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String subTitle;

    @Column(length = 500)
    private String sammary;

    private String imageUrl;
    private String thumbnailUrl;
    private String author;
    private String category;


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime publishedAt;

    @Enumerated(EnumType.STRING)
    private NewsStatus status;

    private Long views;

    // Getters and Setters

    public News() {
    }

    public News(Long id, String title, String subTitle, 
        String sammary, String imageUrl, String thumbnailUrl, 
        String author, String category, LocalDateTime createdAt, 
        LocalDateTime updatedAt, LocalDateTime publishedAt, 
        NewsStatus status, Long views) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.sammary = sammary;
        this.imageUrl = imageUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.author = author;
        this.category = category;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.publishedAt = publishedAt;
        this.status = status;
        this.views = views;
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

    public String getSubTitle() {
        return this.subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getSammary() {
        return this.sammary;
    }

    public void setSammary(String sammary) {
        this.sammary = sammary;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getThumbnailUrl() {
        return this.thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getPublishedAt() {
        return this.publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public NewsStatus getStatus() {
        return this.status;
    }

    public void setStatus(NewsStatus status) {
        this.status = status;
    }

    public Long getViews() {
        return this.views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public News id(Long id) {
        setId(id);
        return this;
    }

    public News title(String title) {
        setTitle(title);
        return this;
    }

    public News subTitle(String subTitle) {
        setSubTitle(subTitle);
        return this;
    }

    public News sammary(String sammary) {
        setSammary(sammary);
        return this;
    }

    public News imageUrl(String imageUrl) {
        setImageUrl(imageUrl);
        return this;
    }

    public News thumbnailUrl(String thumbnailUrl) {
        setThumbnailUrl(thumbnailUrl);
        return this;
    }

    public News author(String author) {
        setAuthor(author);
        return this;
    }

    public News category(String category) {
        setCategory(category);
        return this;
    }

    public News createdAt(LocalDateTime createdAt) {
        setCreatedAt(createdAt);
        return this;
    }

    public News updatedAt(LocalDateTime updatedAt) {
        setUpdatedAt(updatedAt);
        return this;
    }

    public News publishedAt(LocalDateTime publishedAt) {
        setPublishedAt(publishedAt);
        return this;
    }

    public News status(NewsStatus status) {
        setStatus(status);
        return this;
    }

    public News views(Long views) {
        setViews(views);
        return this;
    }

    @Override
    public boolean equals(Object o) {
      return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, subTitle, sammary, imageUrl, thumbnailUrl, author, category, createdAt, updatedAt, publishedAt, status, views);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", title='" + getTitle() + "'" +
            ", subTitle='" + getSubTitle() + "'" +
            ", sammary='" + getSammary() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            ", thumbnailUrl='" + getThumbnailUrl() + "'" +
            ", author='" + getAuthor() + "'" +
            ", category='" + getCategory() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", publishedAt='" + getPublishedAt() + "'" +
            ", status='" + getStatus() + "'" +
            ", views='" + getViews() + "'" +
            "}";
    }

    
}
