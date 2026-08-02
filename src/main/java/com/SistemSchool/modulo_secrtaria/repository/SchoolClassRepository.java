package com.SistemSchool.modulo_secrtaria.repository;

import com.SistemSchool.modulo_secrtaria.dto.SchoolClassDTO;
import com.SistemSchool.modulo_secrtaria.interfaces.SchoolClassTableProjection;
import com.SistemSchool.modulo_secrtaria.io.Classe;
import com.SistemSchool.modulo_secrtaria.io.SchoolClaassStatus;
import com.SistemSchool.modulo_secrtaria.io.ShiftType;
import com.SistemSchool.modulo_secrtaria.model.SchoolClass;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolClassRepository extends JpaRepository<SchoolClass, Long> {

        // -------------------------------
        // Lazy Loading para tabela (nativeQuery)
        // -------------------------------

        @Query(value = """
                        SELECT sc.pk_school_class AS pkSchoolClass,
                               sc.class_code      AS classCode,
                               sc.class_name      AS className,
                               sc.classe          AS classe,
                               sc.turno           AS turno,
                               sc.ano_lectivo     AS anoLectivo,
                               sc.capacidade      AS capacidade,
                               sc.room            AS room,
                               sc.status          AS status,
                               sc.obs             AS obs,
                               sc.created_at      AS createdAt,
                               sc.updated_at      AS updatedAt
                        FROM school_class sc
                        """, countQuery = "SELECT COUNT(*) FROM school_class", nativeQuery = true)
        Page<SchoolClassTableProjection> findAllForTable(Pageable pageable);

        // -------------------------------
        // Lista completa com DTO (JPQL)
        // -------------------------------

        @Query("""
                        SELECT new com.SistemSchool.modulo_secrtaria.dto.SchoolClassDTO(
                               sc.pkSchoolClass,
                               sc.classCode,
                               sc.className,
                               sc.classe,
                               sc.turno,
                               sc.anoLectivo,
                               sc.capacidade,
                               sc.room,
                               sc.status,
                               sc.obs,
                               sc.createdAt,
                               sc.updatedAt
                        )
                        FROM SchoolClass sc
                        """)
        List<SchoolClassDTO> findAllSchoolClassesDTO();

        // -------------------------------
        // Queries utilitárias
        // -------------------------------

        boolean existsByClassCode(String classCode);

        List<SchoolClass> findByClasse(Classe classe);

        List<SchoolClass> findByTurno(ShiftType turno);

        List<SchoolClass> findByStatus(SchoolClaassStatus status);

        List<SchoolClass> findByAnoLectivo(String anoLectivo);

        long countByStatus(SchoolClaassStatus status);
}