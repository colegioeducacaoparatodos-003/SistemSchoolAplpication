package com.SistemSchool.modulo_Financeiro.controller;

import com.SistemSchool.modulo_Financeiro.dto.CashBoxDTO;
import com.SistemSchool.modulo_Financeiro.io.CashBoxStatus;
import com.SistemSchool.modulo_Financeiro.lazy.CashBoxLazyModel;
import com.SistemSchool.modulo_Financeiro.model.CashBox;
import com.SistemSchool.modulo_Financeiro.service.CashBoxService;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@ViewScoped
public class CashBoxController implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(CashBoxController.class.getName());

    // ─────────────────────────────────────────────────────────────
    // MODELOS
    // ─────────────────────────────────────────────────────────────

    private CashBox cashBox = new CashBox();

    private CashBoxDTO editDto = new CashBoxDTO();
    private CashBoxDTO selectedCashBox = new CashBoxDTO();
    private Long selectedId;

    // Usados no diálogo de fecho de caixa
    private BigDecimal closingBalanceInput;
    private String closingObservationInput;

    // ─────────────────────────────────────────────────────────────
    // ESTATÍSTICAS
    // ─────────────────────────────────────────────────────────────

    private long totalCashBoxCount;
    private long openCashBoxCount;
    private long newCashBoxCountThisMonth;
    private BigDecimal totalOpeningBalance;

    // ─────────────────────────────────────────────────────────────
    // SERVIÇOS
    // ─────────────────────────────────────────────────────────────

    @Inject
    private CashBoxService cashBoxService;

    private transient CashBoxLazyModel lazyModel;

    // ─────────────────────────────────────────────────────────────
    // INICIALIZAÇÃO
    // ─────────────────────────────────────────────────────────────

    @PostConstruct
    public void init() {
        lazyModel = new CashBoxLazyModel(cashBoxService);
        computeStatistics();
    }

    private void computeStatistics() {
        try {
            List<CashBoxDTO> all = cashBoxService.getAllCashBoxes();

            totalCashBoxCount = all.size();

            openCashBoxCount = all.stream()
                    .filter(c -> c.getStatus() == CashBoxStatus.OPEN)
                    .count();

            YearMonth currentMonth = YearMonth.from(LocalDate.now());
            newCashBoxCountThisMonth = all.stream()
                    .filter(c -> c.getOpeningDate() != null)
                    .filter(c -> YearMonth.from(c.getOpeningDate()).equals(currentMonth))
                    .count();

            totalOpeningBalance = cashBoxService.getTotalOpeningBalance();

        } catch (Exception e) {
            totalCashBoxCount = 0;
            openCashBoxCount = 0;
            newCashBoxCountThisMonth = 0;
            totalOpeningBalance = BigDecimal.ZERO;
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao calcular estatísticas", e.getMessage());
            LOGGER.log(Level.SEVERE, "Erro ao calcular estatísticas de caixa", e);
        }
    }

    public String load() {
        try {
            init();
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao carregar caixas", e.getMessage());
            LOGGER.log(Level.SEVERE, "Erro ao carregar a listagem de caixas", e);
        }
        return "/management/financeiro/cashboxes.xhtml?faces-redirect=true";
    }

    public CashBoxLazyModel getLazyModel() {
        return lazyModel;
    }

    // ─────────────────────────────────────────────────────────────
    // CRUD
    // ─────────────────────────────────────────────────────────────

    public String saveCashBox() {
        try {
            if (cashBox.getOpeningDate() == null) {
                cashBox.setOpeningDate(LocalDate.now());
            }
            if (cashBox.getStatus() == null) {
                cashBox.setStatus(CashBoxStatus.OPEN);
            }

            cashBoxService.save(cashBox);

            cashBox = new CashBox();
            init();

            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getFlash()
                    .setKeepMessages(true);

            addMessage(FacesMessage.SEVERITY_INFO, "Caixa", "Caixa aberto com sucesso");

            return "/management/financeiro/cashboxes.xhtml?faces-redirect=true";

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao gravar caixa", e);
            addMessage(FacesMessage.SEVERITY_ERROR, "Caixa", e.getMessage());
            return null;
        }
    }

    // ─────────────────────────────────────────────────────────────
    // EDIT / UPDATE / DELETE
    // ─────────────────────────────────────────────────────────────

    /**
     * Recebe o id diretamente como parâmetro do método EL (chamado a partir do
     * botão de editar: actionListener="#{cashBoxController.openEditDialog(cashBox.phCashBox)}").
     * Isso evita o problema clássico do JSF em que o actionListener do atributo
     * dispara antes do f:setPropertyActionListener aninhado, o que deixava o
     * selectedId desatualizado e o diálogo de edição vazio.
     */
    public void openEditDialog(Long id) {
        if (id == null) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Nenhum caixa selecionado!", "");
            return;
        }

        this.selectedId = id;

        CashBoxDTO dto = cashBoxService.getAllCashBoxes()
                .stream()
                .filter(c -> id.equals(c.getPhCashBox()))
                .findFirst()
                .orElse(null);

        if (dto != null) {
            editDto = new CashBoxDTO();
            mapDtoFields(dto, editDto);
            mapDtoFields(dto, selectedCashBox);
        } else {
            addMessage(FacesMessage.SEVERITY_WARN, "Caixa não encontrado", "");
        }
    }

    public void loadSelectedCashBox() {
        if (selectedId == null) {
            return;
        }

        CashBoxDTO dto = cashBoxService.getAllCashBoxes()
                .stream()
                .filter(c -> selectedId.equals(c.getPhCashBox()))
                .findFirst()
                .orElse(null);

        if (dto != null) {
            mapDtoFields(dto, selectedCashBox);
        } else {
            addMessage(FacesMessage.SEVERITY_WARN, "Caixa não encontrado", "");
        }
    }

    private void mapDtoFields(CashBoxDTO source, CashBoxDTO target) {
        target.setPhCashBox(source.getPhCashBox());
        target.setCashBoxNumber(source.getCashBoxNumber());
        target.setOperator(source.getOperator());
        target.setOpeningBalance(source.getOpeningBalance());
        target.setTotalIncome(source.getTotalIncome());
        target.setTotalExpense(source.getTotalExpense());
        target.setCurrentBalance(source.getCurrentBalance());
        target.setStatus(source.getStatus());
        target.setOpeningDate(source.getOpeningDate());
        target.setClosingDate(source.getClosingDate());
        target.setObservation(source.getObservation());
        target.setCreatedAt(source.getCreatedAt());
        target.setUpdatedAt(source.getUpdatedAt());
    }

    public void saveUpdate() {
        try {
            cashBoxService.update(editDto);
            init();
            editDto = new CashBoxDTO();
            selectedId = null;
            addMessage(FacesMessage.SEVERITY_INFO, "Caixa", "Caixa atualizado com sucesso");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao atualizar caixa", e);
            addMessage(FacesMessage.SEVERITY_ERROR, "Caixa", e.getMessage());
        }
    }

    /**
     * Prepara o diálogo de fecho de caixa, recebendo o id diretamente como
     * parâmetro do método EL (mesmo padrão usado em openEditDialog), garantindo
     * que selectedId esteja correto antes do diálogo ser exibido.
     */
    public void prepareCloseCashBox(Long id) {
        if (id == null) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Nenhum caixa selecionado!", "");
            return;
        }
        this.selectedId = id;
        this.closingBalanceInput = null;
        this.closingObservationInput = null;
    }

    public void closeCashBox() {
        if (selectedId == null) {
            addMessage(FacesMessage.SEVERITY_WARN, "Nenhum caixa selecionado!", "");
            return;
        }
        try {
            cashBoxService.closeCashBox(selectedId, closingBalanceInput, closingObservationInput);
            init();
            selectedId = null;
            closingBalanceInput = null;
            closingObservationInput = null;
            addMessage(FacesMessage.SEVERITY_INFO, "Caixa", "Caixa fechado com sucesso");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao fechar caixa", e);
            addMessage(FacesMessage.SEVERITY_ERROR, "Caixa", e.getMessage());
        }
    }

    /**
     * Recebe o id diretamente como parâmetro do método EL (chamado a partir do
     * botão de excluir: actionListener="#{cashBoxController.delete(cashBox.phCashBox)}").
     * Antes, o id era definido via f:setPropertyActionListener separado, que
     * executava DEPOIS do actionListener do atributo — fazendo o delete rodar
     * com selectedId desatualizado (ou nulo).
     */
    public void delete(Long id) {
        if (id == null) {
            addMessage(FacesMessage.SEVERITY_WARN, "Nenhum caixa selecionado!", "");
            return;
        }
        try {
            cashBoxService.delete(id);
            selectedId = null;
            init();
            addMessage(FacesMessage.SEVERITY_INFO, "Caixa", "Caixa eliminado com sucesso");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao eliminar caixa", e);
            addMessage(FacesMessage.SEVERITY_ERROR, "Caixa", e.getMessage());
        }
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

    public CashBox getCashBox() {
        return cashBox;
    }

    public void setCashBox(CashBox cashBox) {
        this.cashBox = cashBox;
    }

    public CashBoxDTO getEditDto() {
        return editDto;
    }

    public void setEditDto(CashBoxDTO editDto) {
        this.editDto = editDto;
    }

    public CashBoxDTO getSelectedCashBox() {
        return selectedCashBox;
    }

    public void setSelectedCashBox(CashBoxDTO selectedCashBox) {
        this.selectedCashBox = selectedCashBox;
    }

    public Long getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(Long selectedId) {
        this.selectedId = selectedId;
    }

    public BigDecimal getClosingBalanceInput() {
        return closingBalanceInput;
    }

    public void setClosingBalanceInput(BigDecimal closingBalanceInput) {
        this.closingBalanceInput = closingBalanceInput;
    }

    public String getClosingObservationInput() {
        return closingObservationInput;
    }

    public void setClosingObservationInput(String closingObservationInput) {
        this.closingObservationInput = closingObservationInput;
    }

    public void setLazyModel(CashBoxLazyModel lazyModel) {
        this.lazyModel = lazyModel;
    }

    // ─────────────────────────────────────────────────────────────
    // ESTATÍSTICAS — GETTERS
    // ─────────────────────────────────────────────────────────────

    public long getTotalCashBoxCount() {
        return totalCashBoxCount;
    }

    public long getOpenCashBoxCount() {
        return openCashBoxCount;
    }

    public long getNewCashBoxCountThisMonth() {
        return newCashBoxCountThisMonth;
    }

    public BigDecimal getTotalOpeningBalance() {
        return totalOpeningBalance;
    }

    // ─────────────────────────────────────────────────────────────
    // ENUMS E LISTAS
    // ─────────────────────────────────────────────────────────────

    public CashBoxStatus[] getStatuses() {
        return CashBoxStatus.values();
    }

    public List<CashBoxDTO> getCashBoxes() {
        return cashBoxService.getAllCashBoxes();
    }
}