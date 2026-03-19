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
            SELECT g.id AS id,
                   g.full_name AS fullName,
                   g.type AS type,
                   g.title AS title,
                   g.sub_title AS subTitle,
                   g.description AS description
            FROM government_entities g
            """, countQuery = "SELECT COUNT(*) FROM government_entities", nativeQuery = true)
    Page<GovernmentTableProjection> findAllForTable(Pageable pageable);
}
