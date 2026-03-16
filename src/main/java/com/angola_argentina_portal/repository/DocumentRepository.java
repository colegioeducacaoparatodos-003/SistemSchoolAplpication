package com.angola_argentina_portal.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.angola_argentina_portal.interfaces.DocumentTableProjection;
import com.angola_argentina_portal.model.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {

  @Query("SELECT d.pkDocument AS pkDocument, d.documentType AS documentType, d.fileName AS fileName, " +
      "d.fileSize AS fileSize, d.uploadDate AS uploadDate, d.fkUser AS uploadedBy " +
      "FROM Document d WHERE d.referenceType = :referenceType AND d.referenceId = :referenceId")
  Page<DocumentTableProjection> findAllForReference(@Param("referenceType") String referenceType,
      @Param("referenceId") int referenceId,
      Pageable pageable);

  // -------------------- BUSCAR POR ID --------------------
  @Query("SELECT d FROM Document d WHERE d.pkDocument = :id")
  Document findDocumentById(@Param("id") int id);

  // -------------------- LISTAR TODOS COM PAGINAÇÃO --------------------
  @Query("SELECT d FROM Document d ORDER BY d.uploadDate DESC")
  Page<Document> findAllDocuments(Pageable pageable);

  // -------------------- LISTAR POR TIPO DE REFERÊNCIA --------------------
  @Query("SELECT d FROM Document d WHERE d.referenceType = :referenceType AND d.referenceId = :referenceId ORDER BY d.uploadDate DESC")
  Page<Document> findAll(@Param("referenceType") String referenceType,
      @Param("referenceId") int referenceId,
      Pageable pageable);

  // -------------------- BUSCAR POR TIPO DE DOCUMENTO --------------------
  @Query("SELECT d FROM Document d WHERE d.documentType = :documentType ORDER BY d.uploadDate DESC")
  Page<Document> findByDocumentType(@Param("documentType") String documentType, Pageable pageable);

  // -------------------- BUSCAR POR USUÁRIO --------------------
  @Query("SELECT d FROM Document d WHERE d.fkUser = :userId ORDER BY d.uploadDate DESC")
  Page<Document> findByUser(@Param("userId") int userId, Pageable pageable);

  // -------------------- DELETE CUSTOM --------------------
  @Query("DELETE FROM Document d WHERE d.pkDocument = :id")
  void deleteDocumentById(@Param("id") int id);
}
