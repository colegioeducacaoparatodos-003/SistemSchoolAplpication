package com.angola_argentina_portal.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.angola_argentina_portal.interfaces.DocumentTableProjection;
import com.angola_argentina_portal.interfaces.GovernmentTableProjection;
import com.angola_argentina_portal.model.Government;

@Repository
public interface GovernmentRepository extends JpaRepository<Government, Long> {
    @Query(value = """
            SELECT
                d.id AS id,
                d.full_name AS fullName,
                d.type AS type,
                d.title AS title,
                d.sub_title AS subTitle,
                d.description AS description
            FROM document d
            """, countQuery = "SELECT COUNT(*) FROM document", nativeQuery = true)
    Page<GovernmentTableProjection> findAllForTable(Pageable pageable);
}
