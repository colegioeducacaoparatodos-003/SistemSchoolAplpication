package com.SistemSchool.modulo_Financeiro.repository;

import com.SistemSchool.modulo_Financeiro.dto.FeeDTO;
import com.SistemSchool.modulo_Financeiro.interfaces.FeeTableProjection;
import com.SistemSchool.modulo_Financeiro.io.FeeStatus;
import com.SistemSchool.modulo_Financeiro.model.Fee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FeeRepository extends JpaRepository<Fee, Long> {

    // =====================================================
    // Lazy Loading para PrimeFaces DataTable
    // =====================================================

    @Query(value = """

            SELECT

                f.ph_fee AS phFee,

                f.fee_code AS feeCode,

                f.description AS description,


                e.ph_enrolment AS enrolmentPk,

                e.enrolment_numer AS enrolmentNumber,


                s.pk_student AS studentPk,

                CONCAT(
                    s.frist_name,
                    ' ',
                    s.last_name
                ) AS studentName,


                sc.pk_school_class AS schoolClassPk,

                sc.class_code AS schoolClassName,


                f.school_year AS schoolYear,


                f.amount AS amount,


                f.start_date AS startDate,


                f.end_date AS endDate,


                f.status AS status,


                f.obs AS obs,


                f.created_at AS createdAt,


                f.updated_at AS updatedAt



            FROM fee f


            INNER JOIN enrolment e

            ON e.ph_enrolment = f.enrolment_pk



            INNER JOIN student s

            ON s.pk_student = e.student_pk



            INNER JOIN school_class sc

            ON sc.pk_school_class = f.school_class_pk



            """,

            countQuery = """

                    SELECT COUNT(*)

                    FROM fee

                    """,

            nativeQuery = true)

    Page<FeeTableProjection> findAllForTable(Pageable pageable);

    // =====================================================
    // Lista completa usando DTO
    // =====================================================

    @Query("""

            SELECT new com.SistemSchool.modulo_Financeiro.dto.FeeDTO(

                f.phFee,

                f.feeCode,

                f.description,


                f.schoolClass.pkSchoolClass,

                f.schoolClass.classCode,


                f.schoolYear,


                f.amount,


                f.startDate,


                f.endDate,


                f.status,


                f.obs,


                f.createdAt,


                f.updatedAt

            )

            FROM Fee f

            """)

    List<FeeDTO> findAllFeesDTO();

    // =====================================================
    // Consultas utilitárias
    // =====================================================

    List<Fee> findByEnrolment_PhEnrolment(Long enrolmentPk);

    List<Fee> findByStatus(FeeStatus status);

    List<Fee> findBySchoolYear(Integer schoolYear);

    List<Fee> findByEndDate(LocalDateTime endDate);

    boolean existsByFeeCode(String feeCode);

    List<Fee> findByEnrolment_Student_PkStudent(Long studentPk);

    List<Fee> findBySchoolClass_PkSchoolClass(Long schoolClassPk);

}