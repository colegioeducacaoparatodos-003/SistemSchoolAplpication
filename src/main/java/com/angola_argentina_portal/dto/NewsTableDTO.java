package com.angola_argentina_portal.dto;

import java.time.LocalDateTime;

public class NewsTableDTO {

    private Long id;
    private String title;
    private String subtitle;
    private String summary;
    private String author;
    private String category;
    private LocalDateTime publishedAt;
    private String status;
    private Long views;

    public NewsTableDTO(Long id,
            String title,
            String subtitle,
            String author,
            String category,
              Enum status,
            LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.author = author;
        this.category = category;
         this.status = status.toString();
        this.publishedAt = createdAt; // ou crie um campo createdAt se preferir
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

    public LocalDateTime getPublishedAt() {
        return this.publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getViews() {
        return this.views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

}
