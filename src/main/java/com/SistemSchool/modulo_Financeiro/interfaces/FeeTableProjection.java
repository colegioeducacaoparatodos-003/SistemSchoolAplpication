package com.SistemSchool.modulo_Financeiro.interfaces;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface FeeTableProjection {

    Long getPhFee();
    String getFeeCode();
    String getDescription();
    /**
     * Dados do aluno (via enrolment)
     */
    Long getStudentPk();
    String getStudentName();
    /**
     * Dados da matrícula
     */
    Long getEnrolmentPk();
    String getEnrolmentNumber();
    /**
     * Turma (relação direta em Fee)
     */
    Long getSchoolClassPk();
    String getSchoolClassName();
    Integer getSchoolYear();
    BigDecimal getAmount();
    LocalDateTime getStartDate();
    LocalDateTime getEndDate();
    String getStatus();
    String getObs();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();

}