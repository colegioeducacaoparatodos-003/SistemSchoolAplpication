package com.angola_argentina_portal.dto;

import java.time.LocalDateTime;

public class CreateNewsDTO {

    
        private Long id;
        private String title;
        private String subtitle;
        private String summary;
        private String content;
        private String imageUrl;
        private String thumbnailUrl;
        private String author;
        private String category;
        private NewsStatus status;
        private LocalDateTime publishedAt;
        private Long views;

        public CreateNewsDTO() {
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

        public NewsStatus getStatus() {
            return this.status;
        }

        public void setStatus(NewsStatus status) {
            this.status = status;
        }

        public LocalDateTime getPublishedAt() {
            return this.publishedAt;
        }

        public void setPublishedAt(LocalDateTime publishedAt) {
            this.publishedAt = publishedAt;
        }
        
        public Long getViews() {
            return this.views;
        }

        public void setViews(Long views) {
            this.views = views;
        }

}
