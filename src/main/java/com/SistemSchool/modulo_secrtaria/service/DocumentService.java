package com.SistemSchool.modulo_secrtaria.service;

import com.SistemSchool.modulo_secrtaria.dto.DocumentDTO;
import com.SistemSchool.modulo_secrtaria.interfaces.DocumentTableProjection;
import com.SistemSchool.modulo_secrtaria.io.DocumentType;
import com.SistemSchool.modulo_secrtaria.model.Document;
import com.SistemSchool.modulo_secrtaria.model.Student;
import com.SistemSchool.modulo_secrtaria.repository.DocumentRepository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class DocumentService {

    private final DocumentRepository repository;
    private final StudentService studentService;

    public DocumentService(DocumentRepository repository, StudentService studentService) {
        this.repository = repository;
        this.studentService = studentService;
    }

    // ─────────────────────────────────────────────────────────────
    // CRUD PRINCIPAL
    // ─────────────────────────────────────────────────────────────

    public Document save(Document document) {
        if (repository.existsByDocumentNumber(document.getDocumentNumber())) {
            throw new RuntimeException("Número de documento já existe: " + document.getDocumentNumber());
        }
        return repository.save(document);
    }

    /**
     * Atualiza os dados do documento (sem tocar no ficheiro físico).
     * Para trocar o ficheiro, usa replaceDocumentFile().
     */
    public void update(DocumentDTO dto) {
        Document document = repository.findById(dto.getPhDocument())
                .orElseThrow(() -> new RuntimeException("Documento não encontrado com id: " + dto.getPhDocument()));

        document.setDocumentNumber(dto.getDocumentNumber());
        document.setDocumentType(dto.getDocumentType());
        document.setIssueDate(dto.getIssueDate());
        document.setExpiryDate(dto.getExpiryDate());
        document.setObs(dto.getObs());

        if (dto.getStudentId() != null
                && (document.getStudent() == null || !dto.getStudentId().equals(document.getStudent().getPkStudent()))) {
            Student student = studentService.findById(dto.getStudentId());
            document.setStudent(student);
        }

        // onUpdate() é disparado automaticamente pelo @PreUpdate no flush
        repository.save(document);
    }

    public void delete(Long id, String uploadBaseDir) {
        Document document = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Documento não encontrado com id: " + id));

        deletePhysicalFile(document, uploadBaseDir);
        repository.deleteById(id);
    }

    // ─────────────────────────────────────────────────────────────
    // BUSCAR TODOS (lista completa com DTO)
    // ─────────────────────────────────────────────────────────────

    public List<DocumentDTO> getAllDocuments() {
        return repository.findAllDocumentsDTO();
    }

    public List<DocumentDTO> getByStudentId(Long studentId) {
        return repository.findAllByStudentId(studentId);
    }

    // ─────────────────────────────────────────────────────────────
    // LAZY LOADING PARA TABELA
    // ─────────────────────────────────────────────────────────────

    public Page<DocumentDTO> findLazy(int page, int size, Sort sort, Map<String, Object> filters) {
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<DocumentTableProjection> projections = repository.findAllForTable(pageable);

        // getIssueDate()/getExpiryDate() da projeção já devolvem LocalDate
        // diretamente (colunas DATE no banco) — não converter de novo aqui.
        return projections.map(p -> new DocumentDTO(
                p.getPkDocument(),
                p.getDocumentNumber(),
                p.getFileName(),
                p.getFilePath(),
                p.getStudentId(),
                p.getStudentName(),
                p.getDocumentType() != null ? DocumentType.valueOf(p.getDocumentType()) : null,
                p.getIssueDate(),
                p.getExpiryDate(),
                p.getObs(),
                p.getCreatedAt(),
                p.getUpdatedAt()));
    }

    // ─────────────────────────────────────────────────────────────
    // QUERIES UTILITÁRIAS
    // ─────────────────────────────────────────────────────────────

    public List<Document> getByDocumentType(DocumentType documentType) {
        return repository.findByDocumentType(documentType);
    }

    public Document getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Documento não encontrado com id: " + id));
    }

    // ─────────────────────────────────────────────────────────────
    // ESTATÍSTICAS (stat cards)
    // ─────────────────────────────────────────────────────────────

    public long getTotalDocumentCount() {
        return repository.count();
    }

    public long getExpiringSoonCount() {
        LocalDate today = LocalDate.now();
        return repository.countExpiringBetween(today, today.plusDays(30));
    }

    public long getExpiredCount() {
        return repository.countExpiredBefore(LocalDate.now());
    }

    public long getDistinctStudentsCount() {
        return repository.countDistinctStudents();
    }

    // ─────────────────────────────────────────────────────────────
    // UPLOAD DE DOCUMENTO
    // ─────────────────────────────────────────────────────────────

    public Document uploadDocument(Document document, InputStream fileContent, String originalFileName,
            String uploadBaseDir) throws IOException {

        Path uploadDirPath = Paths.get(uploadBaseDir);
        if (!Files.exists(uploadDirPath)) {
            Files.createDirectories(uploadDirPath);
        }

        String extension = "";
        int dotIndex = originalFileName.lastIndexOf('.');
        if (dotIndex >= 0) {
            extension = originalFileName.substring(dotIndex);
        }

        String storedFileName = UUID.randomUUID() + extension;
        Path targetPath = uploadDirPath.resolve(storedFileName);

        try (InputStream in = fileContent) {
            Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
        }

        document.setFileName(originalFileName);
        document.setFilePath(storedFileName);

        return save(document);
    }

    /**
     * Substitui o ficheiro físico de um documento já existente, mantendo o
     * mesmo registo. Não persiste os outros campos — chama update() à parte
     * se quiseres também guardar as alterações de texto do formulário.
     */
    public void replaceDocumentFile(Long documentId, InputStream fileContent, String originalFileName,
            String uploadBaseDir) throws IOException {

        Document document = getById(documentId);
        deletePhysicalFile(document, uploadBaseDir);

        Path uploadDirPath = Paths.get(uploadBaseDir);
        if (!Files.exists(uploadDirPath)) {
            Files.createDirectories(uploadDirPath);
        }

        String extension = "";
        int dotIndex = originalFileName.lastIndexOf('.');
        if (dotIndex >= 0) {
            extension = originalFileName.substring(dotIndex);
        }

        String storedFileName = UUID.randomUUID() + extension;
        Path targetPath = uploadDirPath.resolve(storedFileName);

        try (InputStream in = fileContent) {
            Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
        }

        document.setFileName(originalFileName);
        document.setFilePath(storedFileName);

        repository.save(document);
    }

    // ─────────────────────────────────────────────────────────────
    // DOWNLOAD DE DOCUMENTO
    // ─────────────────────────────────────────────────────────────

    public byte[] downloadDocumentFile(Long documentId, String uploadBaseDir) throws IOException {
        Document document = getById(documentId);

        if (document.getFilePath() == null || document.getFilePath().isBlank()) {
            throw new RuntimeException("Este documento não tem ficheiro anexado.");
        }

        Path filePath = Paths.get(uploadBaseDir, document.getFilePath());

        if (!Files.exists(filePath)) {
            throw new RuntimeException("Ficheiro não encontrado em disco: " + filePath);
        }

        return Files.readAllBytes(filePath);
    }

    // ─────────────────────────────────────────────────────────────
    // PRIVADOS
    // ─────────────────────────────────────────────────────────────

    private void deletePhysicalFile(Document document, String uploadBaseDir) {
        if (document.getFilePath() == null || document.getFilePath().isBlank()) {
            return;
        }
        try {
            Path filePath = Paths.get(uploadBaseDir, document.getFilePath());
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            // não interrompe a operação principal por causa de erro ao apagar o ficheiro físico
        }
    }
}