package com.angola_argentina_portal.dto;

import java.time.LocalDateTime;
import java.util.Objects;

import org.apache.commons.lang3.builder.EqualsBuilder;

public class NewsDTO {

    private Long id;
    private String title;
    private String subtitle;
    private String summary;
    private String content;
    private String imageUrl;
    private String thumbnailUrl;
    private String author;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime publishedAt;
    private NewsStatus status;
    private Long views;


    public NewsDTO() {
    }

    public NewsDTO(Long id, String title, String subtitle, String summary, String content, String imageUrl, String thumbnailUrl, String author, String category, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime publishedAt, NewsStatus status, Long views) {
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

    public NewsDTO id(Long id) {
        setId(id);
        return this;
    }

    public NewsDTO title(String title) {
        setTitle(title);
        return this;
    }

    public NewsDTO subtitle(String subtitle) {
        setSubtitle(subtitle);
        return this;
    }

    public NewsDTO summary(String summary) {
        setSummary(summary);
        return this;
    }

    public NewsDTO content(String content) {
        setContent(content);
        return this;
    }

    public NewsDTO imageUrl(String imageUrl) {
        setImageUrl(imageUrl);
        return this;
    }

    public NewsDTO thumbnailUrl(String thumbnailUrl) {
        setThumbnailUrl(thumbnailUrl);
        return this;
    }

    public NewsDTO author(String author) {
        setAuthor(author);
        return this;
    }

    public NewsDTO category(String category) {
        setCategory(category);
        return this;
    }

    public NewsDTO createdAt(LocalDateTime createdAt) {
        setCreatedAt(createdAt);
        return this;
    }

    public NewsDTO updatedAt(LocalDateTime updatedAt) {
        setUpdatedAt(updatedAt);
        return this;
    }

    public NewsDTO publishedAt(LocalDateTime publishedAt) {
        setPublishedAt(publishedAt);
        return this;
    }

    public NewsDTO status(NewsStatus status) {
        setStatus(status);
        return this;
    }

    public NewsDTO views(Long views) {
        setViews(views);
        return this;
    }    
}
