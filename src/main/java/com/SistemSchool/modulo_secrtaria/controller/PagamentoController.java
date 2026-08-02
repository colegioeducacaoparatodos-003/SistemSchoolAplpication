package com.SistemSchool.modulo_secrtaria.controller;

import com.SistemSchool.modulo_secrtaria.dto.PagamentoDTO;
import com.SistemSchool.modulo_secrtaria.io.EstadoPagamento;
import com.SistemSchool.modulo_secrtaria.io.FormaPagamento;
import com.SistemSchool.modulo_secrtaria.io.MesReferencia;
import com.SistemSchool.modulo_secrtaria.lazy.PagamentoLazyModel;
import com.SistemSchool.modulo_secrtaria.model.Enrolment;
import com.SistemSchool.modulo_secrtaria.model.Pagamento;
import com.SistemSchool.modulo_secrtaria.repository.EnrolmentRepository;
import com.SistemSchool.modulo_secrtaria.service.PagamentoService;
import com.SistemSchool.report.PdfReportService;
import com.SistemSchool.modulo_Financeiro.model.CashBox;
import com.SistemSchool.modulo_Financeiro.model.Fee;
import com.SistemSchool.modulo_Financeiro.repository.CashBoxRepository;
import com.SistemSchool.modulo_Financeiro.repository.FeeRepository;

// (novo import necessário)
import com.itextpdf.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.IOException;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@ViewScoped
public class PagamentoController implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(PagamentoController.class.getName());

    // ─────────────────────────────────────────────────────────────
    // MODELOS
    // ─────────────────────────────────────────────────────────────

    private Pagamento pagamento = new Pagamento();

    private PagamentoDTO editDto = new PagamentoDTO();
    private PagamentoDTO selectedPagamento = new PagamentoDTO();
    private Long selectedId;

    private Long selectedEnrolmentId;
    private Long selectedFeeId;
    private Long selectedCashBoxId;

    // ── Campos do diálogo de confirmação (confirmarPagamento) ──────
    private BigDecimal valorConfirmar;
    private FormaPagamento formaPagamentoConfirmar;
    private String referenciaConfirmar;
    private String observacaoConfirmar;

    private List<Enrolment> enrolments = new java.util.ArrayList<>();
    private List<Fee> fees = new java.util.ArrayList<>();
    private List<CashBox> cashBoxes = new java.util.ArrayList<>();

    // ─────────────────────────────────────────────────────────────
    // ESTATÍSTICAS
    // ─────────────────────────────────────────────────────────────

    private long totalPagamentoCount;
    private long confirmadoCount;
    private BigDecimal totalConfirmadoAmount;

    // ─────────────────────────────────────────────────────────────
    // SERVIÇOS
    // ─────────────────────────────────────────────────────────────

    @Inject
    private PagamentoService pagamentoService;

    @Inject
    private EnrolmentRepository enrolmentRepository;

    @Inject
    private FeeRepository feeRepository;

    @Inject
    private CashBoxRepository cashBoxRepository;

    private transient PagamentoLazyModel lazyModel;

    // ─────────────────────────────────────────────────────────────
    // INICIALIZAÇÃO
    // ─────────────────────────────────────────────────────────────

    @PostConstruct
    public void init() {
        lazyModel = new PagamentoLazyModel(pagamentoService);
        loadEnrolments();
        loadFees();
        loadCashBoxes();
        computeStatistics();
    }

    private void loadEnrolments() {
        try {
            enrolments = enrolmentRepository.findAllWithStudent();
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao carregar matrículas", e.getMessage());
            LOGGER.log(Level.SEVERE, "Erro ao carregar matrículas para o formulário de pagamento", e);
        }
    }

    private void loadFees() {
        try {
            fees = feeRepository.findAll();
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao carregar propinas", e.getMessage());
            LOGGER.log(Level.SEVERE, "Erro ao carregar propinas para o formulário de pagamento", e);
        }
    }

    private void loadCashBoxes() {
        try {
            cashBoxes = cashBoxRepository.findAll();
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao carregar caixas", e.getMessage());
            LOGGER.log(Level.SEVERE, "Erro ao carregar caixas para o formulário de pagamento", e);
        }
    }

    private void computeStatistics() {
        try {
            List<PagamentoDTO> all = pagamentoService.getAllPagamentos();

            totalPagamentoCount = all.size();

            confirmadoCount = all.stream()
                    .filter(p -> p.getEstado() == EstadoPagamento.PAGO) // TODO: confirmar valor do enum
                    .count();

            totalConfirmadoAmount = pagamentoService.getTotalConfirmado();

        } catch (Exception e) {
            totalPagamentoCount = 0;
            confirmadoCount = 0;
            totalConfirmadoAmount = BigDecimal.ZERO;
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao calcular estatísticas", e.getMessage());
            LOGGER.log(Level.SEVERE, "Erro ao calcular estatísticas de pagamentos", e);
        }
    }

    private BigDecimal calcularTotal(BigDecimal valor, BigDecimal desconto, BigDecimal multa) {
        BigDecimal v = valor != null ? valor : BigDecimal.ZERO;
        BigDecimal d = desconto != null ? desconto : BigDecimal.ZERO;
        BigDecimal m = multa != null ? multa : BigDecimal.ZERO;
        return v.subtract(d).add(m);
    }

    public String load() {
        try {
            init();
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao carregar pagamentos", e.getMessage());
            LOGGER.log(Level.SEVERE, "Erro ao carregar a listagem de pagamentos", e);
        }
        return "/management/financeiro/pagamentos.xhtml?faces-redirect=true";
    }

    public PagamentoLazyModel getLazyModel() {
        return lazyModel;
    }

    // ─────────────────────────────────────────────────────────────
    // CRUD (registo manual, sem orquestração automática)
    // ─────────────────────────────────────────────────────────────

    public String savePagamento() {
        try {
            if (selectedEnrolmentId == null) {
                addMessage(FacesMessage.SEVERITY_ERROR, "Pagamento", "Selecione uma matrícula antes de gravar.");
                return null;
            }
            if (selectedFeeId == null) {
                addMessage(FacesMessage.SEVERITY_ERROR, "Pagamento", "Selecione uma propina antes de gravar.");
                return null;
            }
            if (selectedCashBoxId == null) {
                addMessage(FacesMessage.SEVERITY_ERROR, "Pagamento", "Selecione um caixa antes de gravar.");
                return null;
            }

            Enrolment enrolment = enrolments.stream()
                    .filter(e -> selectedEnrolmentId.equals(e.getPhEnrolment()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Matrícula não encontrada."));
            pagamento.setEnrolment(enrolment);

            Fee fee = fees.stream()
                    .filter(f -> selectedFeeId.equals(f.getPhFee()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Propina não encontrada."));
            pagamento.setFee(fee);

            CashBox cashBox = cashBoxes.stream()
                    .filter(c -> selectedCashBoxId.equals(c.getPhCashBox()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Caixa não encontrado."));
            pagamento.setCashBox(cashBox);

            // em savePagamento()
            pagamento.setTotal(calcularTotal(pagamento.getValor(), pagamento.getDesconto(), pagamento.getMulta()));

            pagamentoService.save(pagamento);

            pagamento = new Pagamento();
            selectedEnrolmentId = null;
            selectedFeeId = null;
            selectedCashBoxId = null;
            init();

            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getFlash()
                    .setKeepMessages(true);

            addMessage(FacesMessage.SEVERITY_INFO, "Pagamento", "Pagamento registado com sucesso");

            return "/management/financeiro/pagamentos.xhtml?faces-redirect=true";

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao gravar pagamento", e);
            addMessage(FacesMessage.SEVERITY_ERROR, "Pagamento", e.getMessage());
            return null;
        }
    }

    // ─────────────────────────────────────────────────────────────
    // CONFIRMAÇÃO DE PAGAMENTO (fluxo completo: Pagamento +
    // FinancialMovement + saldo do CashBox)
    // ─────────────────────────────────────────────────────────────

    /**
     * Abre o diálogo de confirmação já com a matrícula e a propina
     * pré-selecionadas.
     */
    public void openConfirmarPagamentoDialog(Long enrolmentPk, Long feePk) {
        if (enrolmentPk == null || feePk == null) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Selecione a matrícula e a propina!", "");
            return;
        }
        this.selectedEnrolmentId = enrolmentPk;
        this.selectedFeeId = feePk;
        this.valorConfirmar = null;
        this.formaPagamentoConfirmar = null;
        this.referenciaConfirmar = null;
        this.observacaoConfirmar = null;
    }

    /**
     * Confirma o pagamento usando o fluxo completo do PagamentoService:
     * cria o Pagamento, lança o FinancialMovement e atualiza o saldo do
     * CashBox aberto. O caixa é resolvido automaticamente pelo serviço.
     */
    public String confirmarPagamento() {
        try {
            if (selectedEnrolmentId == null || selectedFeeId == null) {
                addMessage(FacesMessage.SEVERITY_ERROR, "Pagamento",
                        "Selecione a matrícula e a propina antes de confirmar o pagamento.");
                return null;
            }

            pagamentoService.confirmarPagamento(
                    selectedEnrolmentId,
                    selectedFeeId,
                    valorConfirmar,
                    formaPagamentoConfirmar,
                    referenciaConfirmar,
                    observacaoConfirmar,
                    resolveOperatorName());

            selectedEnrolmentId = null;
            selectedFeeId = null;
            selectedCashBoxId = null;
            valorConfirmar = null;
            formaPagamentoConfirmar = null;
            referenciaConfirmar = null;
            observacaoConfirmar = null;
            init();

            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getFlash()
                    .setKeepMessages(true);

            addMessage(FacesMessage.SEVERITY_INFO, "Pagamento", "Pagamento confirmado com sucesso");

            return "/management/financeiro/pagamentos.xhtml?faces-redirect=true";

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao confirmar pagamento", e);
            addMessage(FacesMessage.SEVERITY_ERROR, "Pagamento", e.getMessage());
            return null;
        }
    }

    /**
     * TODO: substituir por integração real com o utilizador autenticado
     * assim que estiver disponível neste controller.
     */
    private String resolveOperatorName() {
        return "Sistema";
    }

    // ─────────────────────────────────────────────────────────────
    // EDIT / UPDATE / DELETE
    // ─────────────────────────────────────────────────────────────

    public void openEditDialog(Long id) {
        if (id == null) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Nenhum pagamento selecionado!", "");
            return;
        }

        this.selectedId = id;

        PagamentoDTO dto = pagamentoService.getAllPagamentos()
                .stream()
                .filter(p -> id.equals(p.getPkPagamento()))
                .findFirst()
                .orElse(null);

        if (dto != null) {
            mapDtoFields(dto, editDto = new PagamentoDTO());
            mapDtoFields(dto, selectedPagamento);
            selectedEnrolmentId = dto.getEnrolmentPk();
            selectedFeeId = dto.getFeePk();
            selectedCashBoxId = dto.getCashBoxPk();
        } else {
            addMessage(FacesMessage.SEVERITY_WARN, "Pagamento não encontrado", "");
        }
    }

    public void loadSelectedPagamento() {
        if (selectedId == null) {
            return;
        }

        PagamentoDTO dto = pagamentoService.getAllPagamentos()
                .stream()
                .filter(p -> selectedId.equals(p.getPkPagamento()))
                .findFirst()
                .orElse(null);

        if (dto != null) {
            mapDtoFields(dto, selectedPagamento);
        } else {
            addMessage(FacesMessage.SEVERITY_WARN, "Pagamento não encontrado", "");
        }
    }

    private void mapDtoFields(PagamentoDTO source, PagamentoDTO target) {
        target.setPkPagamento(source.getPkPagamento());
        target.setNumeroDocumento(source.getNumeroDocumento());
        target.setEnrolmentPk(source.getEnrolmentPk());
        target.setEnrolmentNumero(source.getEnrolmentNumero());
        target.setStudentFullName(source.getStudentFullName());
        target.setFeePk(source.getFeePk());
        target.setFeeDescricao(source.getFeeDescricao());
        target.setCashBoxPk(source.getCashBoxPk());
        target.setCashBoxNumber(source.getCashBoxNumber());
        target.setValor(source.getValor());
        target.setDesconto(source.getDesconto());
        target.setMulta(source.getMulta());
        target.setTotal(source.getTotal());
        target.setDataEmissao(source.getDataEmissao());
        target.setDataVencimento(source.getDataVencimento());
        target.setDataPagamento(source.getDataPagamento());
        target.setFormaPagamento(source.getFormaPagamento());
        target.setEstado(source.getEstado());
        target.setMesReferencia(source.getMesReferencia());
        target.setReferencia(source.getReferencia());
        target.setObservacao(source.getObservacao());
        target.setCreatedAt(source.getCreatedAt());
        target.setUpdatedAt(source.getUpdatedAt());
    }

    public void saveUpdate() {
        try {
            if (selectedEnrolmentId != null) {
                editDto.setEnrolmentPk(selectedEnrolmentId);
            }
            if (selectedFeeId != null) {
                editDto.setFeePk(selectedFeeId);
            }
            if (selectedCashBoxId != null) {
                editDto.setCashBoxPk(selectedCashBoxId);
            }

            // em saveUpdate()
            editDto.setTotal(calcularTotal(editDto.getValor(), editDto.getDesconto(), editDto.getMulta()));

            pagamentoService.update(editDto);
            init();
            editDto = new PagamentoDTO();
            selectedId = null;
            selectedEnrolmentId = null;
            selectedFeeId = null;
            selectedCashBoxId = null;
            addMessage(FacesMessage.SEVERITY_INFO, "Pagamento", "Pagamento atualizado com sucesso");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao atualizar pagamento", e);
            addMessage(FacesMessage.SEVERITY_ERROR, "Pagamento", e.getMessage());
        }
    }

    public void delete(Long id) {
        if (id == null) {
            addMessage(FacesMessage.SEVERITY_WARN, "Nenhum pagamento selecionado!", "");
            return;
        }
        try {
            pagamentoService.delete(id);
            selectedId = null;
            init();
            addMessage(FacesMessage.SEVERITY_INFO, "Pagamento", "Pagamento eliminado com sucesso");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao eliminar pagamento", e);
            addMessage(FacesMessage.SEVERITY_ERROR, "Pagamento", e.getMessage());
        }
    }

    public void exportPagamentoListPdf() {
        try {
            List<PagamentoDTO> pagamentos = pagamentoService.getAllPagamentos();

            if (pagamentos == null || pagamentos.isEmpty()) {
                addMessage(FacesMessage.SEVERITY_WARN, "Nenhum pagamento para exportar", "");
                return;
            }

            byte[] pdf = PdfReportService.generatePagamentoListReport(pagamentos);
            String fileName = "lista_pagamentos_" + java.time.LocalDate.now() + ".pdf";

            PdfReportService.streamToResponse(pdf, fileName, true);

        } catch (DocumentException | IOException e) {
            LOGGER.log(Level.SEVERE, "Erro ao exportar lista de pagamentos", e);
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao exportar lista", e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────────
    // GERAÇÃO / DOWNLOAD / IMPRESSÃO DE PDF
    // ─────────────────────────────────────────────────────────────

    public void baixarPdf(Long id) {
        streamPdf(id, "attachment");
    }

    public void imprimirPdf(Long id) {
        streamPdf(id, "inline");
    }

    private void streamPdf(Long id, String disposition) {
        if (id == null) {
            addMessage(FacesMessage.SEVERITY_WARN, "Nenhum pagamento selecionado!", "");
            return;
        }
        try {
            byte[] pdfBytes = pagamentoService.gerarComprovativoPdf(id);
            Pagamento p = pagamentoService.getById(id);

            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();

            externalContext.responseReset();
            externalContext.setResponseContentType("application/pdf");
            externalContext.setResponseHeader("Content-Disposition",
                    disposition + "; filename=\"pagamento-" + p.getNumeroDocumento() + ".pdf\"");
            externalContext.setResponseContentLength(pdfBytes.length);

            OutputStream responseOutputStream = externalContext.getResponseOutputStream();
            responseOutputStream.write(pdfBytes);
            responseOutputStream.flush();

            facesContext.responseComplete();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao gerar PDF do pagamento", e);
            addMessage(FacesMessage.SEVERITY_ERROR, "Pagamento", "Não foi possível gerar o PDF: " + e.getMessage());
        }
    }

    // ── Campos de filtro (lista/relatório) ─────────────────────────
    private String filtroNumeroDocumento;
    private String filtroStudentName;
    private FormaPagamento filtroFormaPagamento;
    private EstadoPagamento filtroEstado;
    private LocalDateTime filtroDataInicio;
    private LocalDateTime filtroDataFim;

    public List<PagamentoDTO> getPagamentosFiltrados() {
        return pagamentoService.buscarComFiltros(
                filtroNumeroDocumento, filtroStudentName,
                filtroFormaPagamento, filtroEstado,
                filtroDataInicio, filtroDataFim);
    }

    public void limparFiltros() {
        filtroNumeroDocumento = null;
        filtroStudentName = null;
        filtroFormaPagamento = null;
        filtroEstado = null;
        filtroDataInicio = null;
        filtroDataFim = null;
    }

    public void baixarListaPdf() {
        streamListaPdf("attachment");
    }

    public void imprimirListaPdf() {
        streamListaPdf("inline");
    }

    private void streamListaPdf(String disposition) {
        try {
            List<PagamentoDTO> lista = getPagamentosFiltrados();
            byte[] pdfBytes = pagamentoService.gerarListaPagamentosPdf(lista, "Relatório de Pagamentos");

            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();

            externalContext.responseReset();
            externalContext.setResponseContentType("application/pdf");
            externalContext.setResponseHeader("Content-Disposition",
                    disposition + "; filename=\"lista-pagamentos-" +
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")) + ".pdf\"");
            externalContext.setResponseContentLength(pdfBytes.length);

            OutputStream out = externalContext.getResponseOutputStream();
            out.write(pdfBytes);
            out.flush();
            facesContext.responseComplete();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao gerar PDF da lista de pagamentos", e);
            addMessage(FacesMessage.SEVERITY_ERROR, "Pagamentos", "Não foi possível gerar o PDF: " + e.getMessage());
        }
    }

    // ── Getters e Setters dos filtros ───────────────────────────────

    public String getFiltroNumeroDocumento() {
        return filtroNumeroDocumento;
    }

    public void setFiltroNumeroDocumento(String filtroNumeroDocumento) {
        this.filtroNumeroDocumento = filtroNumeroDocumento;
    }

    public String getFiltroStudentName() {
        return filtroStudentName;
    }

    public void setFiltroStudentName(String filtroStudentName) {
        this.filtroStudentName = filtroStudentName;
    }

    public FormaPagamento getFiltroFormaPagamento() {
        return filtroFormaPagamento;
    }

    public void setFiltroFormaPagamento(FormaPagamento filtroFormaPagamento) {
        this.filtroFormaPagamento = filtroFormaPagamento;
    }

    public EstadoPagamento getFiltroEstado() {
        return filtroEstado;
    }

    public void setFiltroEstado(EstadoPagamento filtroEstado) {
        this.filtroEstado = filtroEstado;
    }

    public LocalDateTime getFiltroDataInicio() {
        return filtroDataInicio;
    }

    public void setFiltroDataInicio(LocalDateTime filtroDataInicio) {
        this.filtroDataInicio = filtroDataInicio;
    }

    public LocalDateTime getFiltroDataFim() {
        return filtroDataFim;
    }

    public void setFiltroDataFim(LocalDateTime filtroDataFim) {
        this.filtroDataFim = filtroDataFim;
    }
    // ─────────────────────────────────────────────────────────────
    // UTIL
    // ─────────────────────────────────────────────────────────────

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

    // ─────────────────────────────────────────────────────────────
    // GETTERS E SETTERS
    // ─────────────────────────────────────────────────────────────

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }

    public PagamentoDTO getEditDto() {
        return editDto;
    }

    public void setEditDto(PagamentoDTO editDto) {
        this.editDto = editDto;
    }

    public PagamentoDTO getSelectedPagamento() {
        return selectedPagamento;
    }

    public void setSelectedPagamento(PagamentoDTO selectedPagamento) {
        this.selectedPagamento = selectedPagamento;
    }

    public Long getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(Long selectedId) {
        this.selectedId = selectedId;
    }

    public Long getSelectedEnrolmentId() {
        return selectedEnrolmentId;
    }

    public void setSelectedEnrolmentId(Long selectedEnrolmentId) {
        this.selectedEnrolmentId = selectedEnrolmentId;
    }

    public Long getSelectedFeeId() {
        return selectedFeeId;
    }

    public void setSelectedFeeId(Long selectedFeeId) {
        this.selectedFeeId = selectedFeeId;
    }

    public Long getSelectedCashBoxId() {
        return selectedCashBoxId;
    }

    public void setSelectedCashBoxId(Long selectedCashBoxId) {
        this.selectedCashBoxId = selectedCashBoxId;
    }

    public BigDecimal getValorConfirmar() {
        return valorConfirmar;
    }

    public void setValorConfirmar(BigDecimal valorConfirmar) {
        this.valorConfirmar = valorConfirmar;
    }

    public FormaPagamento getFormaPagamentoConfirmar() {
        return formaPagamentoConfirmar;
    }

    public void setFormaPagamentoConfirmar(FormaPagamento formaPagamentoConfirmar) {
        this.formaPagamentoConfirmar = formaPagamentoConfirmar;
    }

    public String getReferenciaConfirmar() {
        return referenciaConfirmar;
    }

    public void setReferenciaConfirmar(String referenciaConfirmar) {
        this.referenciaConfirmar = referenciaConfirmar;
    }

    public String getObservacaoConfirmar() {
        return observacaoConfirmar;
    }

    public void setObservacaoConfirmar(String observacaoConfirmar) {
        this.observacaoConfirmar = observacaoConfirmar;
    }

    public void setLazyModel(PagamentoLazyModel lazyModel) {
        this.lazyModel = lazyModel;
    }

    // ─────────────────────────────────────────────────────────────
    // ESTATÍSTICAS — GETTERS
    // ─────────────────────────────────────────────────────────────

    public long getTotalPagamentoCount() {
        return totalPagamentoCount;
    }

    public long getConfirmadoCount() {
        return confirmadoCount;
    }

    public BigDecimal getTotalConfirmadoAmount() {
        return totalConfirmadoAmount;
    }

    // ─────────────────────────────────────────────────────────────
    // ENUMS E LISTAS
    // ─────────────────────────────────────────────────────────────

    public FormaPagamento[] getFormasPagamento() {
        return FormaPagamento.values();
    }

    public EstadoPagamento[] getEstados() {
        return EstadoPagamento.values();
    }

    public MesReferencia[] getMesesReferencia() {
        return MesReferencia.values();
    }

    public List<Enrolment> getEnrolments() {
        return enrolments;
    }

    public List<Fee> getFees() {
        return fees;
    }

    public List<CashBox> getCashBoxes() {
        return cashBoxes;
    }

    public void refreshEnrolments() {
        loadEnrolments();
    }

    public void refreshFees() {
        loadFees();
    }

    public void refreshCashBoxes() {
        loadCashBoxes();
    }

    public List<PagamentoDTO> getPagamentos() {
        return pagamentoService.getAllPagamentos();
    }
}