package com.angola_argentina_portal.interfaces;

import java.time.LocalDateTime;

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
