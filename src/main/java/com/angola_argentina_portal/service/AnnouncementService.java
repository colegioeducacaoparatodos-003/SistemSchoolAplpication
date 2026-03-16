package com.angola_argentina_portal.service;

import org.springframework.stereotype.Service;

import com.angola_argentina_portal.model.Announcement;
import com.angola_argentina_portal.repository.AnnouncementRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AnnouncementService {

    private final AnnouncementRepository repository;
    

    public AnnouncementService(AnnouncementRepository repository) {
        this.repository = repository;
        
    }

    public Announcement save(Announcement announcement) {
        return repository.save(announcement);
    }

    public void publish(int announcementId) {
        Announcement a = repository.findById(announcementId).orElseThrow();
        a.setStatus("PUBLISHED");
        repository.save(a);
    }
}
