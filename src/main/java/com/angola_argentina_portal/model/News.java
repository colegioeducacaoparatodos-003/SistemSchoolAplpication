package com.angola_argentina_portal.model;

import java.time.LocalDateTime;

import com.angola_argentina_portal.dto.NewsStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.util.Objects;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.primefaces.model.file.UploadedFile;

@Entity
@Table(name = "news")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String subtitle;

    @Column(length = 500)
    private String summary;

    @Lob
    private String content;

    private String imageUrl;

    @Transient
    private UploadedFile imageUrlUtil;
    private String thumbnailUrl;

    private String author;
    private String category;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime publishedAt;

    @Enumerated(EnumType.STRING)
    private NewsStatus status;

    private Long views;

    public News() {
        super();
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

    public String getSubtitle() {
        return this.subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getSummary() {
        return this.summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public News(Long id, String title, String subtitle, String summary, String content, String imageUrl,
            String thumbnailUrl, String author, String category, LocalDateTime createdAt, LocalDateTime updatedAt,
            LocalDateTime publishedAt, NewsStatus status, Long views) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.summary = summary;
        this.content = content;
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

    public News id(Long id) {
        setId(id);
        return this;
    }

    public News title(String title) {
        setTitle(title);
        return this;
    }

    public News subtitle(String subtitle) {
        setSubtitle(subtitle);
        return this;
    }

    public News summary(String summary) {
        setSummary(summary);
        return this;
    }

    public News content(String content) {
        setContent(content);
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

    public UploadedFile getImageUrlUtil() {
        return imageUrlUtil;
    }

    public void setImageUrlUtil(UploadedFile imageUrlUtil) {
        this.imageUrlUtil = imageUrlUtil;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, subtitle, summary, content, imageUrl, thumbnailUrl, author, category, createdAt,
                updatedAt, publishedAt, status, views);
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", title='" + getTitle() + "'" +
                ", subtitle='" + getSubtitle() + "'" +
                ", summary='" + getSummary() + "'" +
                ", content='" + getContent() + "'" +
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
