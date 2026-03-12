package com.angola_argentina_portal.interfaces;

import java.time.LocalDateTime;

import com.angola_argentina_portal.dto.NewsStatus;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Lob;

public interface NewsTableProjetion {

     Long getId();

     Long getViews();

     String getTitle();

     String getSubtitle();

     String getSummary();

     String getContent();

     String getImageUrl();

     String getThumbnailUrl();

     String getAuthor();

     String getCategory();

     LocalDateTime getCreatedAt();

     LocalDateTime getUpdatedAt();

     LocalDateTime getPublishedAt();

     String getStatus();

}
