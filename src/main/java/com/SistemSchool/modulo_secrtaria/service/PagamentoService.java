package com.SistemSchool.modulo_secrtaria.service;

import com.SistemSchool.modulo_secrtaria.dto.PagamentoDTO;
import com.SistemSchool.modulo_secrtaria.interfaces.PagamentoTableProjection;
import com.SistemSchool.modulo_secrtaria.io.EstadoPagamento;
import com.SistemSchool.modulo_secrtaria.io.FormaPagamento;
import com.SistemSchool.modulo_secrtaria.model.Enrolment;
import com.SistemSchool.modulo_secrtaria.model.Pagamento;
import com.SistemSchool.modulo_secrtaria.repository.EnrolmentRepository;
import com.SistemSchool.modulo_secrtaria.repository.PagamentoRepository;

import com.SistemSchool.modulo_Financeiro.io.CashBoxStatus;
import com.SistemSchool.modulo_Financeiro.io.MovementStatus;
import com.SistemSchool.modulo_Financeiro.io.MovementType;
import com.SistemSchool.modulo_Financeiro.model.CashBox;
import com.SistemSchool.modulo_Financeiro.model.Fee;
import com.SistemSchool.modulo_Financeiro.model.FinancialMovement;
import com.SistemSchool.modulo_Financeiro.repository.CashBoxRepository;
import com.SistemSchool.modulo_Financeiro.repository.FeeRepository;
import com.SistemSchool.modulo_Financeiro.service.FinancialMovementService;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@Transactional
public class PagamentoService {

    private static final Logger LOGGER = Logger.getLogger(PagamentoService.class.getName());

    private final PagamentoRepository repository;
    private final EnrolmentRepository enrolmentRepository;
    private final FeeRepository feeRepository;
    private final CashBoxRepository cashBoxRepository;
    private final FinancialMovementService financialMovementService;
    private final PdfGeneratorService pdfGeneratorService;

    public PagamentoService(PagamentoRepository repository,
            EnrolmentRepository enrolmentRepository,
            FeeRepository feeRepository,
            CashBoxRepository cashBoxRepository,
            FinancialMovementService financialMovementService,
            PdfGeneratorService pdfGeneratorService) {
        this.repository = repository;
        this.enrolmentRepository = enrolmentRepository;
        this.feeRepository = feeRepository;
        this.cashBoxRepository = cashBoxRepository;
        this.financialMovementService = financialMovementService;
        this.pdfGeneratorService = pdfGeneratorService;
    }


    // ─────────────────────────────────────────────────────────────
    // CRUD PRINCIPAL
    // ─────────────────────────────────────────────────────────────

    public Pagamento save(Pagamento pagamento) {
        if (pagamento.getEnrolment() == null || pagamento.getEnrolment().getPhEnrolment() == null) {
            throw new RuntimeException("É necessário indicar a matrícula do aluno.");
        }
        if (pagamento.getFee() == null || pagamento.getFee().getPhFee() == null) {
            throw new RuntimeException("É necessário indicar a propina.");
        }
        if (pagamento.getCashBox() == null || pagamento.getCashBox().getPhCashBox() == null) {
            throw new RuntimeException("É necessário indicar o caixa.");
        }

        Enrolment enrolment = enrolmentRepository.findById(pagamento.getEnrolment().getPhEnrolment())
                .orElseThrow(() -> new RuntimeException("Matrícula não encontrada."));
        pagamento.setEnrolment(enrolment);

        Fee fee = feeRepository.findById(pagamento.getFee().getPhFee())
                .orElseThrow(() -> new RuntimeException("Propina não encontrada."));
        pagamento.setFee(fee);

        CashBox cashBox = cashBoxRepository.findById(pagamento.getCashBox().getPhCashBox())
                .orElseThrow(() -> new RuntimeException("Caixa não encontrado."));
        pagamento.setCashBox(cashBox);

        if (pagamento.getNumeroDocumento() == null || pagamento.getNumeroDocumento().isBlank()) {
            pagamento.setNumeroDocumento(generateNumeroDocumento());
        } else if (repository.existsByNumeroDocumento(pagamento.getNumeroDocumento())) {
            throw new RuntimeException("Número de documento já existe: " + pagamento.getNumeroDocumento());
        }

        return repository.save(pagamento);
    }

    public void update(PagamentoDTO dto) {
        Pagamento pagamento = repository.findById(dto.getPkPagamento())
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado com id: " + dto.getPkPagamento()));

        if (dto.getEnrolmentPk() != null
                && !dto.getEnrolmentPk().equals(pagamento.getEnrolment().getPhEnrolment())) {
            Enrolment enrolment = enrolmentRepository.findById(dto.getEnrolmentPk())
                    .orElseThrow(() -> new RuntimeException("Matrícula não encontrada."));
            pagamento.setEnrolment(enrolment);
        }

        if (dto.getFeePk() != null
                && !dto.getFeePk().equals(pagamento.getFee().getPhFee())) {
            Fee fee = feeRepository.findById(dto.getFeePk())
                    .orElseThrow(() -> new RuntimeException("Propina não encontrada."));
            pagamento.setFee(fee);
        }

        if (dto.getCashBoxPk() != null
                && !dto.getCashBoxPk().equals(pagamento.getCashBox().getPhCashBox())) {
            CashBox cashBox = cashBoxRepository.findById(dto.getCashBoxPk())
                    .orElseThrow(() -> new RuntimeException("Caixa não encontrado."));
            pagamento.setCashBox(cashBox);
        }

        pagamento.setValor(dto.getValor());
        pagamento.setDesconto(dto.getDesconto());
        pagamento.setMulta(dto.getMulta());
        pagamento.setTotal(dto.getTotal());
        pagamento.setDataVencimento(dto.getDataVencimento());
        pagamento.setDataPagamento(dto.getDataPagamento());
        pagamento.setFormaPagamento(dto.getFormaPagamento());
        pagamento.setEstado(dto.getEstado());
        pagamento.setMesReferencia(dto.getMesReferencia());
        pagamento.setReferencia(dto.getReferencia());
        pagamento.setObservacao(dto.getObservacao());

        repository.save(pagamento);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Pagamento não encontrado com id: " + id);
        }
        repository.deleteById(id);
    }

    // ─────────────────────────────────────────────────────────────
    // CONFIRMAÇÃO DE PAGAMENTO
    // ─────────────────────────────────────────────────────────────

    public Pagamento confirmarPagamento(
            Long enrolmentPk,
            Long feePk,
            BigDecimal valor,
            FormaPagamento formaPagamento,
            String referencia,
            String observacao,
            String operador) {

        if (enrolmentPk == null) {
            throw new RuntimeException("É necessário indicar a matrícula.");
        }
        if (feePk == null) {
            throw new RuntimeException("É necessário indicar a propina.");
        }
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("O valor pago deve ser maior que zero.");
        }
        if (formaPagamento == null) {
            throw new RuntimeException("É necessário indicar a forma de pagamento.");
        }

        Enrolment enrolment = enrolmentRepository.findById(enrolmentPk)
                .orElseThrow(() -> new RuntimeException("Matrícula não encontrada com id: " + enrolmentPk));

        Fee fee = feeRepository.findById(feePk)
                .orElseThrow(() -> new RuntimeException("Propina não encontrada com id: " + feePk));

        CashBox cashBox = cashBoxRepository.findFirstByStatusOrderByOpeningDateDesc(CashBoxStatus.OPEN);
        if (cashBox == null) {
            throw new RuntimeException(
                    "Não existe nenhum caixa aberto. Abra um caixa antes de registar o pagamento.");
        }

        Pagamento pagamento = new Pagamento();
        pagamento.setEnrolment(enrolment);
        pagamento.setFee(fee);
        pagamento.setCashBox(cashBox);
        pagamento.setValor(valor);
        pagamento.setTotal(valor);
        pagamento.setFormaPagamento(formaPagamento);
        pagamento.setReferencia(referencia);
        pagamento.setObservacao(observacao);
        pagamento.setDataPagamento(LocalDateTime.now());
        pagamento.setEstado(EstadoPagamento.PAGO);
        pagamento.setNumeroDocumento(generateNumeroDocumento());

        pagamento = repository.save(pagamento);
        repository.flush();

        FinancialMovement movement = new FinancialMovement();
        movement.setMovementNumber(generateMovementNumber());
        movement.setCashBox(cashBox);
        movement.setDescription("Pagamento da propina " + fee.getPhFee() + " - matrícula " + enrolment.getEnrolmentNumer());
        movement.setAmount(valor);
        movement.setType(MovementType.INCOME);
        movement.setStatus(MovementStatus.ACTIVE);
        movement.setCategory("PROPINA");
        movement.setResponsible(operador);
        movement.setMovementDate(LocalDateTime.now());

        financialMovementService.save(movement);

        BigDecimal currentIncome = cashBox.getTotalIncome() != null
                ? cashBox.getTotalIncome()
                : BigDecimal.ZERO;
        cashBox.setTotalIncome(currentIncome.add(valor));
        cashBoxRepository.save(cashBox);

        return pagamento;
    }

    // ─────────────────────────────────────────────────────────────
    // BUSCAR TODOS
    // ─────────────────────────────────────────────────────────────

    public List<PagamentoDTO> getAllPagamentos() {
        return repository.findAllPagamentosDTO();
    }

    // ─────────────────────────────────────────────────────────────
    // LAZY LOADING PARA TABELA
    // ─────────────────────────────────────────────────────────────

    public Page<PagamentoDTO> findLazy(int page, int size, Sort sort, Map<String, Object> filters) {
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<PagamentoTableProjection> projections = repository.findAllForTable(pageable);

        return projections.map(p -> new PagamentoDTO(
                p.getPkPagamento(),
                p.getNumeroDocumento(),
                p.getEnrolmentPk(),
                p.getEnrolmentNumero(),
                p.getStudentName(),
                p.getFeePk(),
                null,
                p.getCashBoxPk(),
                p.getCashBoxNumber(),
                p.getValor(),
                p.getDesconto(),
                p.getMulta(),
                p.getTotal(),
                p.getDataEmissao(),
                p.getDataVencimento(),
                p.getDataPagamento(),
                p.getFormaPagamento() != null ? FormaPagamento.valueOf(p.getFormaPagamento()) : null,
                p.getEstado() != null ? EstadoPagamento.valueOf(p.getEstado()) : null,
                null,
                p.getReferencia(),
                p.getObservacao(),
                p.getCreatedAt(),
                p.getUpdatedAt()));
    }

    // ─────────────────────────────────────────────────────────────
    // RELATÓRIO / FILTROS DE PAGAMENTOS
    // ─────────────────────────────────────────────────────────────

    public List<PagamentoDTO> buscarComFiltros(String numeroDocumento, String studentName,
            FormaPagamento formaPagamento, EstadoPagamento estado,
            LocalDateTime dataInicio, LocalDateTime dataFim) {
        try {
            return repository.findComFiltros(
                            blankToNull(numeroDocumento),
                            blankToNull(studentName),
                            formaPagamento,
                            estado,
                            dataInicio,
                            dataFim)
                    .stream()
                    .map(PagamentoDTO::fromEntity)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao buscar pagamentos com filtros", e);
            throw new RuntimeException("Erro ao buscar pagamentos: " + e.getMessage(), e);
        }
    }

    private String blankToNull(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }

    public byte[] gerarListaPagamentosPdf(List<PagamentoDTO> pagamentos, String titulo) {
        try {
            return pdfGeneratorService.generatePagamentosListPdf(pagamentos, titulo);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao gerar PDF da lista de pagamentos", e);
            throw new RuntimeException("Erro ao gerar PDF da lista: " + e.getMessage(), e);
        }
    }

    // ─────────────────────────────────────────────────────────────
    // QUERIES UTILITÁRIAS
    // ─────────────────────────────────────────────────────────────

    public List<Pagamento> getByEnrolment(Long enrolmentPk) {
        return repository.findByEnrolment_PhEnrolment(enrolmentPk);
    }

    public List<Pagamento> getByStudent(Long studentPk) {
        return repository.findByEnrolment_Student_PkStudent(studentPk);
    }

    public List<Pagamento> getByFee(Long feePk) {
        return repository.findByFee_PhFee(feePk);
    }

    public List<Pagamento> getByCashBox(Long cashBoxPk) {
        return repository.findByCashBox_PhCashBox(cashBoxPk);
    }

    public List<Pagamento> getByEstado(EstadoPagamento estado) {
        return repository.findByEstado(estado);
    }

    public List<Pagamento> getByFormaPagamento(FormaPagamento formaPagamento) {
        return repository.findByFormaPagamento(formaPagamento);
    }

    public List<Pagamento> getByDataPagamentoBetween(LocalDateTime start, LocalDateTime end) {
        return repository.findByDataPagamentoBetween(start, end);
    }

    public boolean existsByNumeroDocumento(String numeroDocumento) {
        return repository.existsByNumeroDocumento(numeroDocumento);
    }

    public BigDecimal getTotalConfirmado() {
        BigDecimal total = repository.getTotalConfirmado();
        return total != null ? total : BigDecimal.ZERO;
    }

    public Pagamento getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado com id: " + id));
    }

    public Pagamento findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado com id: " + id));
    }

    // ─────────────────────────────────────────────────────────────
    // GERAÇÃO DE NÚMEROS SEQUENCIAIS
    // ─────────────────────────────────────────────────────────────

    private String generateNumeroDocumento() {
        int year = Year.now().getValue();
        long count = repository.count() + 1;
        return String.format("PAG-%d-%05d", year, count);
    }

    private String generateMovementNumber() {
        int year = Year.now().getValue();
        long count = financialMovementService.count() + 1;
        return String.format("MOV-%d-%05d", year, count);
    }

    // ─────────────────────────────────────────────────────────────
    // GERAÇÃO DE PDF
    // ─────────────────────────────────────────────────────────────

    public byte[] gerarComprovativoPdf(Long id) {
        Pagamento pagamento = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado com id: " + id));

        pagamento.getEnrolment().getStudent().getFullName();
        if (pagamento.getFee() != null) {
            pagamento.getFee().getPhFee();
        }
        if (pagamento.getCashBox() != null) {
            pagamento.getCashBox().getCashBoxNumber();
        }

        return pdfGeneratorService.generatePagamentoPdf(pagamento);
    }
}