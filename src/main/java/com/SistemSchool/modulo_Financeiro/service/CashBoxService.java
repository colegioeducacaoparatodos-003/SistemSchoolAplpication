package com.SistemSchool.modulo_Financeiro.service;

import com.SistemSchool.modulo_Financeiro.dto.CashBoxDTO;
import com.SistemSchool.modulo_Financeiro.interfaces.CashBoxTableProjection;
import com.SistemSchool.modulo_Financeiro.io.CashBoxStatus;
import com.SistemSchool.modulo_Financeiro.model.CashBox;
import com.SistemSchool.modulo_Financeiro.repository.CashBoxRepository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CashBoxService {

    private final CashBoxRepository repository;

    public CashBoxService(CashBoxRepository repository) {
        this.repository = repository;
    }

    // ─────────────────────────────────────────────────────────────
    // CRUD PRINCIPAL
    // ─────────────────────────────────────────────────────────────

    public CashBox save(CashBox cashBox) {

        if (repository.existsByCashBoxNumber(cashBox.getCashBoxNumber())) {
            throw new RuntimeException("Número de caixa já existe: " + cashBox.getCashBoxNumber());
        }

        if (repository.existsByStatus(CashBoxStatus.OPEN)) {
            throw new RuntimeException("Já existe um caixa aberto. Feche-o antes de abrir um novo.");
        }

        return repository.save(cashBox);
    }

    public void update(CashBoxDTO dto) {

        CashBox cashBox = repository.findById(dto.getPhCashBox())
                .orElseThrow(() -> new RuntimeException("Caixa não encontrado com id: " + dto.getPhCashBox()));

        cashBox.setCashBoxNumber(dto.getCashBoxNumber());
        cashBox.setOpeningDate(dto.getOpeningDate());
        cashBox.setClosingDate(dto.getClosingDate());
        cashBox.setOpeningBalance(dto.getOpeningBalance());
        cashBox.setClosingBalance(dto.getClosingBalance());
        cashBox.setOperator(dto.getOperator());
        cashBox.setStatus(dto.getStatus());
        cashBox.setObservation(dto.getObservation());

        repository.save(cashBox);
    }

    public void delete(Long id) {

        if (!repository.existsById(id)) {
            throw new RuntimeException("Caixa não encontrado com id: " + id);
        }

        repository.deleteById(id);
    }

    // ─────────────────────────────────────────────────────────────
    // FECHAMENTO DE CAIXA
    // ─────────────────────────────────────────────────────────────

    public void closeCashBox(Long id, BigDecimal closingBalance, String observation) {

        CashBox cashBox = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Caixa não encontrado com id: " + id));

        if (cashBox.getStatus() == CashBoxStatus.CLOSED) {
            throw new RuntimeException("Este caixa já está fechado.");
        }

        cashBox.setStatus(CashBoxStatus.CLOSED);
        cashBox.setClosingDate(LocalDate.now());
        cashBox.setClosingBalance(closingBalance);

        if (observation != null && !observation.isBlank()) {
            cashBox.setObservation(observation);
        }

        repository.save(cashBox);
    }

    // ─────────────────────────────────────────────────────────────
    // BUSCAR TODOS (lista completa com DTO)
    // ─────────────────────────────────────────────────────────────

    public List<CashBoxDTO> getAllCashBoxes() {
        return repository.findAllCashBoxesDTO();
    }

    // ─────────────────────────────────────────────────────────────
    // LAZY LOADING PARA TABELA
    // ─────────────────────────────────────────────────────────────

    public Page<CashBoxDTO> findLazy(int page, int size, Sort sort, Map<String, Object> filters) {

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<CashBoxTableProjection> projections = repository.findAllForTable(pageable);

        return projections.map(p -> {

            CashBoxDTO dto = new CashBoxDTO();

            dto.setPhCashBox(p.getPhCashBox());
            dto.setCashBoxNumber(p.getCashBoxNumber());
            dto.setOperator(p.getOperator());
            dto.setOpeningBalance(p.getOpeningBalance());
            dto.setTotalIncome(p.getTotalIncome());
            dto.setTotalExpense(p.getTotalExpense());
            dto.setCurrentBalance(p.getCurrentBalance());
            dto.setStatus(p.getStatus() != null ? CashBoxStatus.valueOf(p.getStatus()) : null);
            dto.setOpeningDate(p.getOpeningDate());
            dto.setClosingDate(p.getClosingDate());
            dto.setCreatedAt(p.getCreatedAt());
            dto.setUpdatedAt(p.getUpdatedAt());

            return dto;
        });
    }

    // ─────────────────────────────────────────────────────────────
    // QUERIES UTILITÁRIAS
    // ─────────────────────────────────────────────────────────────

    public List<CashBox> getByStatus(CashBoxStatus status) {
        return repository.findByStatus(status);
    }

    public List<CashBox> getByOperator(String operator) {
        return repository.findByOperator(operator);
    }

    public List<CashBox> getByOpeningDate(LocalDate openingDate) {
        return repository.findByOpeningDate(openingDate);
    }

    public List<CashBox> getByOpeningDateBetween(LocalDate startDate, LocalDate endDate) {
        return repository.findByOpeningDateBetween(startDate, endDate);
    }

    public CashBox getOpenCashBox() {
        return repository.findFirstByStatusOrderByOpeningDateDesc(CashBoxStatus.OPEN);
    }

    public boolean hasOpenCashBox() {
        return repository.existsByStatus(CashBoxStatus.OPEN);
    }

    public BigDecimal getTotalOpeningBalance() {
        BigDecimal total = repository.getTotalOpeningBalance();
        return total != null ? total : BigDecimal.ZERO;
    }

    public CashBox getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Caixa não encontrado com id: " + id));
    }

    public CashBox findById(Long id) {
        return getById(id);
    }
}