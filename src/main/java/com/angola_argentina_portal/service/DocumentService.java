package com.angola_argentina_portal.service;

import org.springframework.stereotype.Service;

import com.angola_argentina_portal.model.Document;
import com.angola_argentina_portal.repository.DocumentRepository;

@Service
public class DocumentService {

    private final DocumentRepository repository;

    public DocumentService(DocumentRepository repository) {
        this.repository = repository;
    }

    public void upload(Document document) {
        repository.save(document);
    }

    public Document findById(int id) {
        return repository.findById(id).orElseThrow();
    }

    public void deleteById(int id) {
        repository.deleteById(id);
    }

}
