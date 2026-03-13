package com.angola_argentina_portal.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.angola_argentina_portal.dto.FileTableDTO;
import com.angola_argentina_portal.model.FileDocument;

public interface FileRepository extends JpaRepository<FileDocument, Long> {

    @Query("SELECT new com.angola_argentina_portal.dto.FileTableDTO(" +
            "f.id, f.fileName, f.contentType, f.size, f.author, f.createdAt) " +
            "FROM FileDocument f " +
            "WHERE (:name IS NULL OR LOWER(f.fileName) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:author IS NULL OR LOWER(f.author) LIKE LOWER(CONCAT('%', :author, '%')))")
    Page<FileTableDTO> searchFiles(@Param("name") String name,
            @Param("author") String author,
            Pageable pageable);

    // ✅ Query de count apenas
    @Query("""
                SELECT COUNT(f)
                FROM FileDocument f
                WHERE
                    (:name IS NULL OR LOWER(f.fileName) LIKE LOWER(CONCAT('%', :name, '%')))
                AND
                    (:author IS NULL OR LOWER(f.author) LIKE LOWER(CONCAT('%', :author, '%')))
            """)
    int countFiles(
            @Param("name") String name,
            @Param("author") String author);

    @Query("""
                SELECT f
                FROM FileDocument f
                WHERE f.id = :id
            """)
    Optional<FileDocument> findFileForDownload(@Param("id") Long id);
}