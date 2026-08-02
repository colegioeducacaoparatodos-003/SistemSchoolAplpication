package com.SistemSchool.modulo_Financeiro.controller;

import com.SistemSchool.modulo_Financeiro.dto.FeeDTO;
import com.SistemSchool.modulo_Financeiro.io.FeeStatus;
import com.SistemSchool.modulo_Financeiro.lazy.FeeLazyModel;
import com.SistemSchool.modulo_Financeiro.model.Fee;
import com.SistemSchool.modulo_Financeiro.service.FeeService;
import com.SistemSchool.modulo_secrtaria.model.Enrolment;
import com.SistemSchool.modulo_secrtaria.model.SchoolClass;
import com.SistemSchool.modulo_secrtaria.repository.EnrolmentRepository;
import com.SistemSchool.modulo_secrtaria.repository.SchoolClassRepository;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@ViewScoped
public class FeeController implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(FeeController.class.getName());

    // ─────────────────────────────────────────────────────────────
    // MODELOS
    // ─────────────────────────────────────────────────────────────

    private Fee fee = new Fee();

    private FeeDTO editDto = new FeeDTO();
    private FeeDTO selectedFee = new FeeDTO();
    private Long selectedId;

    private Long selectedSchoolClassId;
    private Long selectedEnrolmentId;

    private List<SchoolClass> schoolClasses = new java.util.ArrayList<>();
    private List<Enrolment> enrolments = new java.util.ArrayList<>();

    // ─────────────────────────────────────────────────────────────
    // ESTATÍSTICAS
    // ─────────────────────────────────────────────────────────────

    private long totalFeeCount;
    private long activeFeeCount;
    private BigDecimal totalFeeAmount;

    // ─────────────────────────────────────────────────────────────
    // SERVIÇOS
    // ─────────────────────────────────────────────────────────────

    @Inject
    private FeeService feeService;

    @Inject
    private SchoolClassRepository schoolClassRepository;

    @Inject
    private EnrolmentRepository enrolmentRepository;

    private transient FeeLazyModel lazyModel;

    // ─────────────────────────────────────────────────────────────
    // INICIALIZAÇÃO
    // ─────────────────────────────────────────────────────────────

    @PostConstruct
    public void init() {
        lazyModel = new FeeLazyModel(feeService);
        loadSchoolClasses();
        loadEnrolments();
        computeStatistics();
    }

    private void loadSchoolClasses() {
        try {
            schoolClasses = schoolClassRepository.findAll();
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao carregar turmas", e.getMessage());
            LOGGER.log(Level.SEVERE, "Erro ao carregar turmas para o formulário de propina", e);
        }
    }

    private void loadEnrolments() {
        try {
            enrolments = enrolmentRepository.findAll();
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao carregar matrículas", e.getMessage());
            LOGGER.log(Level.SEVERE, "Erro ao carregar matrículas para o formulário de propina", e);
        }
    }

    private void computeStatistics() {
        try {
            List<FeeDTO> all = feeService.getAllFees();

            totalFeeCount = all.size();

            activeFeeCount = all.stream()
                    .filter(f -> f.getStatus() == FeeStatus.ACTIVE)
                    .count();

            totalFeeAmount = all.stream()
                    .map(FeeDTO::getAmount)
                    .filter(java.util.Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

        } catch (Exception e) {
            totalFeeCount = 0;
            activeFeeCount = 0;
            totalFeeAmount = BigDecimal.ZERO;
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao calcular estatísticas", e.getMessage());
            LOGGER.log(Level.SEVERE, "Erro ao calcular estatísticas de propinas", e);
        }
    }

    public String load() {
        try {
            init();
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao carregar propinas", e.getMessage());
            LOGGER.log(Level.SEVERE, "Erro ao carregar a listagem de propinas", e);
        }
        return "/management/financeiro/fees.xhtml?faces-redirect=true";
    }

    public FeeLazyModel getLazyModel() {
        return lazyModel;
    }

    // ─────────────────────────────────────────────────────────────
    // CRUD
    // ─────────────────────────────────────────────────────────────

    public String saveFee() {
        try {
            if (selectedSchoolClassId == null) {
                addMessage(FacesMessage.SEVERITY_ERROR, "Propina", "Selecione uma turma antes de gravar.");
                return null;
            }
            if (selectedEnrolmentId == null) {
                addMessage(FacesMessage.SEVERITY_ERROR, "Propina", "Selecione uma matrícula antes de gravar.");
                return null;
            }

            SchoolClass schoolClass = schoolClasses.stream()
                    .filter(sc -> selectedSchoolClassId.equals(sc.getPkSchoolClass()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Turma não encontrada."));
            fee.setSchoolClass(schoolClass);

            Enrolment enrolment = enrolments.stream()
                    .filter(en -> selectedEnrolmentId.equals(en.getPhEnrolment()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Matrícula não encontrada."));
            fee.setEnrolment(enrolment);

            feeService.save(fee);

            fee = new Fee();
            selectedSchoolClassId = null;
            selectedEnrolmentId = null;
            init();

            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getFlash()
                    .setKeepMessages(true);

            addMessage(FacesMessage.SEVERITY_INFO, "Propina", "Propina registada com sucesso");

            return "/management/financeiro/fees.xhtml?faces-redirect=true";

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao gravar propina", e);
            addMessage(FacesMessage.SEVERITY_ERROR, "Propina", e.getMessage());
            return null;
        }
    }

    // ─────────────────────────────────────────────────────────────
    // EDIT / UPDATE / DELETE
    // ─────────────────────────────────────────────────────────────

    /**
     * Abre o diálogo de edição para a propina indicada.
     *
     * IMPORTANTE: recebe o id diretamente como parâmetro (EL 2.2), em vez de
     * depender de um "selectedId" setado por <f:setPropertyActionListener>.
     * Isso evita o bug clássico do JSF em que o listener declarado no
     * atributo "actionListener" é executado ANTES dos listeners das tags
     * filhas (<f:setPropertyActionListener>), fazendo este método rodar
     * com o id da linha clicada anteriormente (ou null na primeira vez) e
     * deixando o formulário de edição vazio ou com dados errados.
     */
    public void openEditDialog(Long id) {
        if (id == null) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Nenhuma propina selecionada!", "");
            return;
        }

        this.selectedId = id;

        FeeDTO dto = feeService.getAllFees()
                .stream()
                .filter(f -> id.equals(f.getPhFee()))
                .findFirst()
                .orElse(null);

        if (dto != null) {
            mapDtoFields(dto, editDto = new FeeDTO());
            mapDtoFields(dto, selectedFee);
            selectedSchoolClassId = dto.getSchoolClassPk();
        } else {
            addMessage(FacesMessage.SEVERITY_WARN, "Propina não encontrada", "");
        }
    }

    public void loadSelectedFee() {
        if (selectedId == null) {
            return;
        }

        FeeDTO dto = feeService.getAllFees()
                .stream()
                .filter(f -> selectedId.equals(f.getPhFee()))
                .findFirst()
                .orElse(null);

        if (dto != null) {
            mapDtoFields(dto, selectedFee);
        } else {
            addMessage(FacesMessage.SEVERITY_WARN, "Propina não encontrada", "");
        }
    }

    private void mapDtoFields(FeeDTO source, FeeDTO target) {
        target.setPhFee(source.getPhFee());
        target.setFeeCode(source.getFeeCode());
        target.setDescription(source.getDescription());
        target.setSchoolClassPk(source.getSchoolClassPk());
        target.setSchoolClassName(source.getSchoolClassName());
        target.setSchoolYear(source.getSchoolYear());
        target.setAmount(source.getAmount());
        target.setStartDate(source.getStartDate());
        target.setEndDate(source.getEndDate());
        target.setStatus(source.getStatus());
        target.setObs(source.getObs());
        target.setCreatedAt(source.getCreatedAt());
        target.setUpdatedAt(source.getUpdatedAt());
    }

    public void saveUpdate() {
        try {
            if (selectedSchoolClassId != null) {
                editDto.setSchoolClassPk(selectedSchoolClassId);
            }
            feeService.update(editDto);
            init();
            editDto = new FeeDTO();
            selectedId = null;
            selectedSchoolClassId = null;
            addMessage(FacesMessage.SEVERITY_INFO, "Propina", "Propina atualizada com sucesso");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao atualizar propina", e);
            addMessage(FacesMessage.SEVERITY_ERROR, "Propina", e.getMessage());
        }
    }

    /**
     * Elimina a propina indicada.
     *
     * IMPORTANTE: também recebe o id diretamente como parâmetro, pelo mesmo
     * motivo explicado em {@link #openEditDialog(Long)} — aqui o risco é
     * ainda mais sério, pois a ordem incorreta poderia eliminar o registo
     * errado.
     */
    public void delete(Long id) {
        if (id == null) {
            addMessage(FacesMessage.SEVERITY_WARN, "Nenhuma propina selecionada!", "");
            return;
        }
        try {
            feeService.delete(id);
            selectedId = null;
            init();
            addMessage(FacesMessage.SEVERITY_INFO, "Propina", "Propina eliminada com sucesso");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao eliminar propina", e);
            addMessage(FacesMessage.SEVERITY_ERROR, "Propina", e.getMessage());
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

    public Fee getFee() {
        return fee;
    }

    public void setFee(Fee fee) {
        this.fee = fee;
    }

    public FeeDTO getEditDto() {
        return editDto;
    }

    public void setEditDto(FeeDTO editDto) {
        this.editDto = editDto;
    }

    public FeeDTO getSelectedFee() {
        return selectedFee;
    }

    public void setSelectedFee(FeeDTO selectedFee) {
        this.selectedFee = selectedFee;
    }

    public Long getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(Long selectedId) {
        this.selectedId = selectedId;
    }

    public Long getSelectedSchoolClassId() {
        return selectedSchoolClassId;
    }

    public void setSelectedSchoolClassId(Long selectedSchoolClassId) {
        this.selectedSchoolClassId = selectedSchoolClassId;
    }

    public Long getSelectedEnrolmentId() {
        return selectedEnrolmentId;
    }

    public void setSelectedEnrolmentId(Long selectedEnrolmentId) {
        this.selectedEnrolmentId = selectedEnrolmentId;
    }

    public void setLazyModel(FeeLazyModel lazyModel) {
        this.lazyModel = lazyModel;
    }

    // ─────────────────────────────────────────────────────────────
    // ESTATÍSTICAS — GETTERS
    // ─────────────────────────────────────────────────────────────

    public long getTotalFeeCount() {
        return totalFeeCount;
    }

    public long getActiveFeeCount() {
        return activeFeeCount;
    }

    public BigDecimal getTotalFeeAmount() {
        return totalFeeAmount;
    }

    // ─────────────────────────────────────────────────────────────
    // ENUMS E LISTAS
    // ─────────────────────────────────────────────────────────────

    public FeeStatus[] getStatuses() {
        return FeeStatus.values();
    }

    public List<SchoolClass> getSchoolClasses() {
        return schoolClasses;
    }

    public List<Enrolment> getEnrolments() {
        return enrolments;
    }

    public void refreshSchoolClasses() {
        loadSchoolClasses();
    }

    public void refreshEnrolments() {
        loadEnrolments();
    }

    public List<FeeDTO> getFees() {
        return feeService.getAllFees();
    }
}