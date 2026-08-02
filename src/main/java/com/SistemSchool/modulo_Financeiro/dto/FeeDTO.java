package com.SistemSchool.modulo_Financeiro.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.SistemSchool.modulo_Financeiro.io.FeeStatus;
import com.SistemSchool.modulo_Financeiro.model.Fee;

public class FeeDTO {

    private Long phFee;
    private String feeCode;
    private String description;

    // Dados achatados da SchoolClass
    private Long schoolClassPk;
    private String schoolClassName;

    private Integer schoolYear;
    private BigDecimal amount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private FeeStatus status;
    private String obs;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public FeeDTO() {

    }

    public FeeDTO(
            Long phFee, String feeCode, String description, Long schoolClassPk, String schoolClassName,
            Integer schoolYear, BigDecimal amount, LocalDateTime startDate, LocalDateTime endDate,
            FeeStatus status, String obs, LocalDateTime createdAt, LocalDateTime updatedAt) {

        this.phFee = phFee;
        this.feeCode = feeCode;
        this.description = description;
        this.schoolClassPk = schoolClassPk;
        this.schoolClassName = schoolClassName;
        this.schoolYear = schoolYear;
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.obs = obs;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

    }

    // =====================================================
    // CONVERSÃO ENTITY -> DTO
    // =====================================================

    public static FeeDTO fromEntity(Fee fee) {

        return new FeeDTO(

                fee.getPhFee(),
                fee.getFeeCode(),
                fee.getDescription(),
                fee.getSchoolClass() != null ? fee.getSchoolClass().getPkSchoolClass() : null,
                fee.getSchoolClass() != null ? fee.getSchoolClass().getClassCode() : null,
                fee.getSchoolYear(),
                fee.getAmount(),
                fee.getStartDate(),
                fee.getEndDate(),
                fee.getStatus(),
                fee.getObs(),
                fee.getCreatedAt(),
                fee.getUpdatedAt()

        );

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

    public Long getSchoolClassPk() {
        return schoolClassPk;
    }

    public void setSchoolClassPk(Long schoolClassPk) {
        this.schoolClassPk = schoolClassPk;
    }

    public String getSchoolClassName() {
        return schoolClassName;
    }

    public void setSchoolClassName(String schoolClassName) {
        this.schoolClassName = schoolClassName;
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

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
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

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public FeeDTO phFee(Long phFee) {
        setPhFee(phFee);
        return this;

    }

    public FeeDTO feeCode(String feeCode) {
        setFeeCode(feeCode);
        return this;

    }

    public FeeDTO description(String description) {
        setDescription(description);
        return this;

    }

    public FeeDTO schoolClassPk(Long schoolClassPk) {
        setSchoolClassPk(schoolClassPk);
        return this;

    }

    public FeeDTO schoolClassName(String schoolClassName) {
        setSchoolClassName(schoolClassName);
        return this;

    }

    public FeeDTO amount(BigDecimal amount) {
        setAmount(amount);
        return this;

    }

    public FeeDTO status(FeeStatus status) {
        setStatus(status);
        return this;

    }

}