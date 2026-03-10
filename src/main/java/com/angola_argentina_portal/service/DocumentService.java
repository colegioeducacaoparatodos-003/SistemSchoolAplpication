package com.angola_argentina_portal.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.angola_argentina_portal.dto.DocumentTableDTO;
import com.angola_argentina_portal.interfaces.DocumentTableProjection;
import com.angola_argentina_portal.model.Document;
import com.angola_argentina_portal.repository.DocumentRepository;

@Service
@Transactional
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

    public Page<DocumentTableDTO> findLazy(
            String referenceType,
            int referenceId,
            int page,
            int size,
            Sort sort) {

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<DocumentTableProjection> projections = repository.findAllForReference(referenceType, referenceId,
                pageable);

        return projections.map(p -> new DocumentTableDTO(
                p.getPkDocument(),
                p.getDocumentType(),
                p.getFileName(),
                p.getFileSize(),
                p.getUploadDate(),
                p.getUploadedBy()));
    }

}
