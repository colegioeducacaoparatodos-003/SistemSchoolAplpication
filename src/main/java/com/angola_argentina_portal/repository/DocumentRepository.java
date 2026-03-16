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

    @Query("""
            SELECT 
                d.pkDocument as pkDocument,
                d.documentType as documentType,
                d.fileName as fileName,
                d.fileSize as fileSize,
                d.uploadDate as uploadDate,
                u.fullName as uploadedBy
            FROM Document d
            LEFT JOIN User u ON u.id = d.fkUser
            WHERE d.referenceType = :referenceType
            AND d.referenceId = :referenceId
            """)
    Page<DocumentTableProjection> findAllForReference(
            @Param("referenceType") String referenceType,
            @Param("referenceId") int referenceId,
            Pageable pageable);

}
