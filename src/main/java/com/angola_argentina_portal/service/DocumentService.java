package com.angola_argentina_portal.service;

import jakarta.transaction.Transactional;

import java.io.File;
import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.angola_argentina_portal.dto.DocumentTableDTO;
import com.angola_argentina_portal.interfaces.DocumentTableProjection;
import com.angola_argentina_portal.io.Assistant;
import com.angola_argentina_portal.io.FileImage;
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

    public Document save(Document document) throws IOException {

        FileImage acessImage = new FileImage();
        Assistant assistant = new Assistant();
        String newNameFile = "default.png"; // Default image nam

        if (document.getUploadedFile() != null) {
            newNameFile = "0" + assistant.novoNome(document.getUploadedFile().getContentType());
            acessImage.salvarArquivo(document.getUploadedFile(), "documents_files", newNameFile);
        } else {
            document.setUploadedFile(null);
        }

        return repository.save(document);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public Page<DocumentTableDTO> findLazy(int page, int size, Sort sort) {

        Pageable pageable = PageRequest.of(page, size, sort);

<<<<<<< HEAD
        Page<DocumentTableProjection> projections =
                repository.findAllForReference(referenceType, referenceId, pageable);
=======
        Page<DocumentTableProjection> projections = repository.findAllForTable(pageable);
>>>>>>> b55ce8ee43bc8acf20586db32ebb4276593b8c35

        return projections.map(p -> new DocumentTableDTO(
                p.getPkDocument(),
                p.getDocumentType(),
                p.getFileName(),
                p.getContentType(),
                p.getFileSize(),
                p.getUploadDate()));
    }

}
