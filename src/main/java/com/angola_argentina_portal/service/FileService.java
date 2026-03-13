package com.angola_argentina_portal.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.angola_argentina_portal.dto.FileTableDTO;
import com.angola_argentina_portal.model.FileDocument;
import com.angola_argentina_portal.repository.FileRepository;

@Service
public class FileService {

    private final FileRepository fileRepository;

    public FileService (FileRepository fileRepository){
        this.fileRepository = fileRepository;
    }

    // Busca página com DTO
    public Page<FileTableDTO> searchFiles(int page,
                                         int size,
                                         String name,
                                         String author,
                                         String sortField,
                                         Sort.Direction direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        return fileRepository.searchFiles(name, author, pageable);
    }

    // ✅ Novo método: retorna total de registros filtrados
    public int countFiles(String name, String author) {
        return fileRepository.countFiles(name, author);
    }

    public FileDocument getFileForDownload(Long id) {
        return fileRepository.findFileForDownload(id)
                .orElseThrow(() -> new RuntimeException("Arquivo não encontrado"));
    }
}
