package com.SistemSchool.modulo_Financeiro.service;

import com.SistemSchool.modulo_Financeiro.dto.FeeDTO;
import com.SistemSchool.modulo_Financeiro.interfaces.FeeTableProjection;
import com.SistemSchool.modulo_Financeiro.io.FeeStatus;
import com.SistemSchool.modulo_Financeiro.model.Fee;
import com.SistemSchool.modulo_Financeiro.repository.FeeRepository;
import com.SistemSchool.modulo_secrtaria.model.Enrolment;
import com.SistemSchool.modulo_secrtaria.model.SchoolClass;
import com.SistemSchool.modulo_secrtaria.repository.EnrolmentRepository;
import com.SistemSchool.modulo_secrtaria.repository.SchoolClassRepository;

import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class FeeService {

    private final FeeRepository repository;
    private final EnrolmentRepository enrolmentRepository;
    private final SchoolClassRepository schoolClassRepository;

    public FeeService(FeeRepository repository,
            EnrolmentRepository enrolmentRepository,
            SchoolClassRepository schoolClassRepository) {
        this.repository = repository;
        this.enrolmentRepository = enrolmentRepository;
        this.schoolClassRepository = schoolClassRepository;
    }

    // ─────────────────────────────────────────────────────────────
    // CRUD PRINCIPAL
    // ─────────────────────────────────────────────────────────────
    public Fee save(Fee fee) {
        if (repository.existsByFeeCode(fee.getFeeCode())) {
            throw new RuntimeException("Código de propina já existe: " + fee.getFeeCode());
        }
        if (fee.getSchoolClass() == null || fee.getSchoolClass().getPkSchoolClass() == null) {
            throw new RuntimeException("É necessário indicar a turma para a propina.");
        }
        if (fee.getEnrolment() == null || fee.getEnrolment().getPhEnrolment() == null) {
            throw new RuntimeException("É necessário indicar a matrícula para a propina.");
        }

        Long schoolClassPk = fee.getSchoolClass().getPkSchoolClass();
        SchoolClass schoolClass = schoolClassRepository.findById(schoolClassPk)
                .orElseThrow(() -> new RuntimeException("Turma não encontrada com id: " + schoolClassPk));
        fee.setSchoolClass(schoolClass);

        Long enrolmentPk = fee.getEnrolment().getPhEnrolment();
        Enrolment enrolment = enrolmentRepository.findById(enrolmentPk)
                .orElseThrow(() -> new RuntimeException("Matrícula não encontrada com id: " + enrolmentPk));
        fee.setEnrolment(enrolment);

        return repository.save(fee);
    }

    public void update(FeeDTO dto) {
        Fee fee = repository.findById(dto.getPhFee())
                .orElseThrow(() -> new RuntimeException("Propina não encontrada com id: " + dto.getPhFee()));

        if (dto.getSchoolClassPk() != null
                && !dto.getSchoolClassPk().equals(fee.getSchoolClass().getPkSchoolClass())) {
            SchoolClass schoolClass = schoolClassRepository.findById(dto.getSchoolClassPk())
                    .orElseThrow(() -> new RuntimeException("Turma não encontrada com id: " + dto.getSchoolClassPk()));
            fee.setSchoolClass(schoolClass);
        }

        fee.setFeeCode(dto.getFeeCode());
        fee.setDescription(dto.getDescription());
        fee.setSchoolYear(dto.getSchoolYear());
        fee.setAmount(dto.getAmount());
        fee.setStartDate(dto.getStartDate());
        fee.setEndDate(dto.getEndDate());
        fee.setStatus(dto.getStatus());
        fee.setObs(dto.getObs());
        fee.setUpdatedAt(LocalDateTime.now());

        repository.save(fee);
    }

    /**
     * Elimina uma propina.
     *
     * IMPORTANTE: Fee possui um relacionamento de FK a partir de Invoice
     * (fk_invoice_fee). Se existirem faturas emitidas para esta propina, o
     * banco de dados recusa o DELETE (constraint violation). Aqui capturamos
     * essa violação e convertemos numa mensagem amigável para o utilizador,
     * em vez de deixar a DataIntegrityViolationException/stacktrace SQL
     * crua subir até a view.
     */
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Propina não encontrada com id: " + id);
        }
        try {
            repository.deleteById(id);
            repository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException(
                    "Não é possível eliminar esta propina porque já existem faturas associadas a ela. "
                    + "Elimine ou reatribua essas faturas antes de tentar novamente.");
        }
    }

    // ─────────────────────────────────────────────────────────────
    // BUSCAR TODOS (lista completa com DTO)
    // ─────────────────────────────────────────────────────────────

    public List<FeeDTO> getAllFees() {
        return repository.findAllFeesDTO();
    }

    // ─────────────────────────────────────────────────────────────
    // LAZY LOADING PARA TABELA
    // ─────────────────────────────────────────────────────────────
    public Page<FeeDTO> findLazy(int page, int size, Sort sort, Map<String, Object> filters) {
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<FeeTableProjection> projections;
        if (filters == null || filters.isEmpty()) {
            projections = repository.findAllForTable(pageable);
        } else {
            // TODO: usar Specification ou query JPQL com os filtros (ex.: feeCode, status)
            projections = repository.findAllForTable(pageable);
        }

        return projections.map(p -> new FeeDTO(
                p.getPhFee(),
                p.getFeeCode(),
                p.getDescription(),
                p.getSchoolClassPk(),
                p.getSchoolClassName(),
                p.getSchoolYear(),
                p.getAmount(),
                p.getStartDate(),
                p.getEndDate(),
                p.getStatus() != null ? FeeStatus.valueOf(p.getStatus()) : null,
                p.getObs(),
                p.getCreatedAt(),
                p.getUpdatedAt()));
    }

    // ─────────────────────────────────────────────────────────────
    // QUERIES UTILITÁRIAS
    // ─────────────────────────────────────────────────────────────

    public List<Fee> getByStatus(FeeStatus status) {
        return repository.findByStatus(status);
    }

    public List<Fee> getBySchoolYear(Integer schoolYear) {
        return repository.findBySchoolYear(schoolYear);
    }

    public List<Fee> getByEndDate(LocalDateTime endDate) {
        return repository.findByEndDate(endDate);
    }

    public List<Fee> getByEnrolment(Long enrolmentPk) {
        return repository.findByEnrolment_PhEnrolment(enrolmentPk);
    }

    public List<Fee> getByStudent(Long studentPk) {
        return repository.findByEnrolment_Student_PkStudent(studentPk);
    }

    public List<Fee> getBySchoolClass(Long schoolClassPk) {
        return repository.findBySchoolClass_PkSchoolClass(schoolClassPk);
    }

    public Fee getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Propina não encontrada com id: " + id));
    }

    public Fee findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Propina não encontrada com id: " + id));
    }
}