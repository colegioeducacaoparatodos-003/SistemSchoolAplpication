package com.angola_argentina_portal.dto;

public class CreateNewsDTO {

    private String title;
    private String subtitle;
    private String summary;
    private String content;
    private String author;
    private String category;
    private NewsStatus status;
    private String imageUrl;
    private String thumbnailUrl;

    // Construtor completo
    public CreateNewsDTO(String title, String subtitle, String summary, String content,
            String author, String category, NewsStatus status,
            String imageUrl, String thumbnailUrl) {
        this.title = title;
        this.subtitle = subtitle;
        this.summary = summary;
        this.content = content;
        this.author = author;
        this.category = category;
        this.status = status;
        this.imageUrl = imageUrl;
        this.thumbnailUrl = thumbnailUrl;
    }

    // ---------- GETTERS E SETTERS ----------

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public NewsStatus getStatus() {
        return status;
    }

    public void setStatus(NewsStatus status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}