package com.angola_argentina_portal.repository;

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

        @Query(value = """
                        SELECT d.pk_document AS pkDocument,
                               d.document_type AS documentType,
                               d.file_name AS fileName,
                               d.content_type AS contentType,
                               d.file_size AS fileSize,
                               d.upload_date AS uploadDate
                        FROM document d

                        """, countQuery = "SELECT COUNT(*) FROM document", nativeQuery = true)
        Page<DocumentTableProjection> findAllForTable(Pageable pageable);

    // Nova query: busca por tipo
    @Query(value = """
                    SELECT d.pk_document AS pkDocument,
                           d.document_type AS documentType,
                           d.file_name AS fileName,
                           d.content_type AS contentType,
                           d.file_size AS fileSize,
                           d.upload_date AS uploadDate
                    FROM document d
                    WHERE d.document_type = :type
                    """, countQuery = "SELECT COUNT(*) FROM document WHERE document_type = :type", nativeQuery = true)
    Page<DocumentTableProjection> findAllByType(@Param("type") String type, Pageable pageable);

}
