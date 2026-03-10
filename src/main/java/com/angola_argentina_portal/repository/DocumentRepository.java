package com.angola_argentina_portal.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.angola_argentina_portal.interfaces.DocumentTableProjection;
import com.angola_argentina_portal.model.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {

    @Query(value = """
            SELECT
                d.pk_document AS pkDocument,
                d.document_type AS documentType,
                d.file_name AS fileName,
                d.file_size AS fileSize,
                d.upload_date AS uploadDate,
                CONCAT(p.first_name, ' ', p.last_name) AS uploadedBy
            FROM document d
            JOIN user u ON d.fk_user = u.pk_user
            JOIN person p ON u.fk_person = p.pk_person
            WHERE d.reference_type = ?1
              AND d.reference_id = ?2
            """, countQuery = """
            SELECT COUNT(*)
            FROM document
            WHERE reference_type = ?1
              AND reference_id = ?2
            """, nativeQuery = true)
    Page<DocumentTableProjection> findAllForReference(
            String referenceType,
            int referenceId,
            Pageable pageable);


}
