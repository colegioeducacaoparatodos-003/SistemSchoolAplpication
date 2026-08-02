package com.SistemSchool.modulo_Financeiro.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import com.SistemSchool.modulo_Financeiro.io.FeeStatus;
import com.SistemSchool.modulo_secrtaria.model.Enrolment;
import com.SistemSchool.modulo_secrtaria.model.SchoolClass;

import jakarta.persistence.*;

@Entity
@Table(name = "fee")
public class Fee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long phFee;

    /**
     * Código da propina
     * Ex: PROP-2026-001
     */
    private String feeCode;

    /**
     * Descrição
     */
    private String description;

    /**
     * Classe/Turma a que pertence a propina
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "school_class_pk", nullable = false, foreignKey = @ForeignKey(name = "fk_fee_school_class"))
    private SchoolClass schoolClass;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "enrolment_pk", nullable = false, foreignKey = @ForeignKey(name = "fk_fee_enrolment"))
    private Enrolment enrolment;
    /**
     * Ano letivo
     */
    private Integer schoolYear;

    /**
     * Valor da propina
     */
    private BigDecimal amount;

    /**
     * Vigência
     */
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    private FeeStatus status;

    /**
     * Auditoria
     */
    private String obs;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {

        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        if (this.status == null) {
            this.status = FeeStatus.ACTIVE;
        }

    }

    @PreUpdate
    protected void onUpdate() {

        this.updatedAt = LocalDateTime.now();

    }

    public Fee() {

    }

    public LocalDateTime getStartDate() {
        return this.startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return this.endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getPhFee() {
        return phFee;
    }

    public void setPhFee(Long phFee) {
        this.phFee = phFee;
    }

    public String getFeeCode() {
        return feeCode;
    }

    public void setFeeCode(String feeCode) {
        this.feeCode = feeCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }

    public Integer getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(Integer schoolYear) {
        this.schoolYear = schoolYear;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public FeeStatus getStatus() {
        return status;
    }

    public void setStatus(FeeStatus status) {
        this.status = status;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;

        if (!(o instanceof Fee))
            return false;

        Fee fee = (Fee) o;

        return Objects.equals(phFee, fee.phFee);

    }

    @Override
    public int hashCode() {

        return Objects.hash(phFee);

    }

    @Override
    public String toString() {

        return description;

    }

    public Enrolment getEnrolment() {
        return enrolment;
    }

    public void setEnrolment(Enrolment enrolment) {
        this.enrolment = enrolment;
    }

}