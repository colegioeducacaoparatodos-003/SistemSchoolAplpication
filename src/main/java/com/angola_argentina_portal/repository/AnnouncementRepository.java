package com.angola_argentina_portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.angola_argentina_portal.model.Announcement;


public interface AnnouncementRepository
        extends JpaRepository<Announcement, Integer> {
}
