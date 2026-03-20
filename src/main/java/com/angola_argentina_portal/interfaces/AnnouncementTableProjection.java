package com.angola_argentina_portal.interfaces;

import java.time.LocalDate;


public interface AnnouncementTableProjection {

    Long getId();

    String getTitle();

    String getContent();

    String getImageUrl();

    String getAuthor();

    LocalDate getPublishDate();

    String getStatus();

}
