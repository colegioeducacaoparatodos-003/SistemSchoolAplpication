package com.SistemSchool.modulo_secrtaria.controller;

import com.SistemSchool.modulo_secrtaria.dto.EnrolmentDTO;
import com.SistemSchool.modulo_secrtaria.dto.StudentDTO;
import com.SistemSchool.modulo_secrtaria.lazy.EnrolmentLazyModel;
import com.SistemSchool.modulo_secrtaria.io.EnrolmentType;
import com.SistemSchool.modulo_secrtaria.io.ShiftType;
import com.SistemSchool.modulo_secrtaria.model.Enrolment;
import com.SistemSchool.modulo_secrtaria.model.SchoolClass;
import com.SistemSchool.modulo_secrtaria.model.Student;
import com.SistemSchool.modulo_secrtaria.repository.SchoolClassRepository;
import com.SistemSchool.modulo_secrtaria.service.EnrolmentService;
import com.SistemSchool.modulo_secrtaria.service.SchoolClassService;
import com.SistemSchool.modulo_secrtaria.service.StudentService;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.SistemSchool.report.PdfReportService;
import com.itextpdf.text.DocumentException;
import java.io.IOException;

@Named
@ViewScoped
public class EnrolmentController implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(EnrolmentController.class.getName());

    // ─────────────────────────────────────────────────────────────
    // MODELOS
    // ─────────────────────────────────────────────────────────────

    private Enrolment enrolment = new Enrolment();

    private EnrolmentDTO editDto = new EnrolmentDTO();
    private EnrolmentDTO selectedEnrolment = new EnrolmentDTO();
    private Long selectedId;

    // Id do aluno escolhido no dropdown/autocomplete do formulário
    private Long selectedStudentId;

    // Id da turma escolhida no dropdown do formulário (criação/edição)
    private Long selectedSchoolClassId;

    // Lista de alunos para a view (dropdown/autocomplete), carregada uma vez
    private List<StudentDTO> students = new java.util.ArrayList<>();

    // Lista de turmas para a view (dropdown de criação/edição), carregada uma vez
    private List<SchoolClass> schoolClasses = new java.util.ArrayList<>();

    // ─────────────────────────────────────────────────────────────
    // ESTATÍSTICAS (calculadas uma única vez em init(), não a cada getter)
    // ─────────────────────────────────────────────────────────────

    private long totalEnrolmentCount;
    private long newEnrolmentCount; // matrículas registadas no mês corrente
    private long distinctClassesCount; // nº de turmas com pelo menos uma matrícula
    private long distinctStudentsCount; // nº de alunos distintos matriculados

    // ─────────────────────────────────────────────────────────────
    // SERVIÇOS
    // ─────────────────────────────────────────────────────────────

    @Inject
    private EnrolmentService enrolmentService;

    @Inject
    private StudentService studentService;

    @Inject
    private SchoolClassRepository schoolClassRepository;

    @Inject
    private SchoolClassService schoolClassService;

    private transient EnrolmentLazyModel lazyModel;

    // ─────────────────────────────────────────────────────────────
    // INICIALIZAÇÃO E NAVEGAÇÃO
    // ─────────────────────────────────────────────────────────────

    @PostConstruct
    public void init() {
        lazyModel = new EnrolmentLazyModel(enrolmentService);
        loadStudents();
        loadSchoolClasses();
        computeStatistics();
    }

    /**
     * Carrega a lista de alunos para a view (dropdown/autocomplete de seleção
     * de aluno no formulário de matrícula). Chamado no init() para evitar que
     * o getter dispare uma query à BD a cada chamada do EL.
     */
    private void loadStudents() {
        try {
            students = studentService.getAllStudents();
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao carregar alunos", e.getMessage());
            LOGGER.log(Level.SEVERE, "Erro ao carregar alunos para o formulário de matrícula", e);
        }
    }

    /**
     * Carrega a lista de turmas para a view (dropdown de seleção de turma
     * nos formulários de criação/edição de matrícula). Chamado no init()
     * pelo mesmo motivo de loadStudents().
     */
    private void loadSchoolClasses() {
        try {
            schoolClasses = schoolClassRepository.findAll();
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao carregar turmas", e.getMessage());
            LOGGER.log(Level.SEVERE, "Erro ao carregar turmas para o formulário de matrícula", e);
        }
    }

    /**
     * Calcula os indicadores mostrados nos cards de estatística da view.
     * Executado uma única vez em init()/load() para não recalcular a cada
     * chamada do EL (evita disparar múltiplas queries por render da página).
     */
    private void computeStatistics() {
        try {
            List<EnrolmentDTO> all = enrolmentService.getAllEnrolments();

            totalEnrolmentCount = all.size();

            YearMonth currentMonth = YearMonth.from(LocalDate.now());
            newEnrolmentCount = all.stream()
                    .filter(e -> e.getEnrolmentData() != null)
                    .filter(e -> YearMonth.from(e.getEnrolmentData()).equals(currentMonth))
                    .count();

            Set<Object> classes = new HashSet<>();
            Set<Object> studentIds = new HashSet<>();
            for (EnrolmentDTO e : all) {
                if (e.getSchoolclassPk() != null) {
                    classes.add(e.getSchoolclassPk());
                }
                if (e.getStudentPk() != null) {
                    studentIds.add(e.getStudentPk());
                }
            }
            distinctClassesCount = classes.size();
            distinctStudentsCount = studentIds.size();

        } catch (Exception e) {
            totalEnrolmentCount = 0;
            newEnrolmentCount = 0;
            distinctClassesCount = 0;
            distinctStudentsCount = 0;
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao calcular estatísticas", e.getMessage());
            LOGGER.log(Level.SEVERE, "Erro ao calcular estatísticas de matrículas", e);
        }
    }

    public String load() {
        try {
            init(); // Recarrega o lazy model, a lista de alunos e as estatísticas
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao carregar matrículas", e.getMessage());
            LOGGER.log(Level.SEVERE, "Erro ao carregar a listagem de matrículas", e);
        }
        return "/management/secretaria/enrolments.xhtml?faces-redirect=true";
    }

    public EnrolmentLazyModel getLazyModel() {
        return lazyModel;
    }

    public void clearFilters() {
        if (lazyModel != null) {
            lazyModel.clearFilters();
        }
    }

    // ─────────────────────────────────────────────────────────────
    // CRUD
    // ─────────────────────────────────────────────────────────────

    public void prepareNewEnrolment() {
        enrolment = new Enrolment();
        enrolment.setEnrolmentNumer(enrolmentService.generateNextEnrolmentNumber());
        selectedStudentId = null;
        selectedSchoolClassId = null;
        loadStudents();
        loadSchoolClasses();
    }

    public String saveEnrolment() {
        try {
            if (selectedStudentId == null) {
                addMessage(FacesMessage.SEVERITY_ERROR, "Matrícula", "Selecione um aluno antes de gravar.");
                return null;
            }
            if (selectedSchoolClassId == null) {
                addMessage(FacesMessage.SEVERITY_ERROR, "Matrícula", "Selecione uma turma antes de gravar.");
                return null;
            }

            Student student = studentService.findById(selectedStudentId);
            enrolment.setStudent(student);

            SchoolClass schoolClass = schoolClasses.stream()
                    .filter(sc -> selectedSchoolClassId.equals(sc.getPkSchoolClass()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Turma não encontrada."));
            enrolment.setSchoolClass(schoolClass);

            enrolmentService.save(enrolment);

            enrolment = new Enrolment();
            selectedStudentId = null;
            selectedSchoolClassId = null;
            init(); // Recarrega o lazy model e as estatísticas

            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getFlash()
                    .setKeepMessages(true);

            addMessage(FacesMessage.SEVERITY_INFO, "Matrícula", "Matrícula registada com sucesso");

            return "/management/secretaria/enrolments.xhtml?faces-redirect=true";

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao gravar matrícula", e);
            addMessage(FacesMessage.SEVERITY_ERROR, "Matrícula", e.getMessage());
            return null;
        }
    }

    // ─────────────────────────────────────────────────────────────
    // EDIT / UPDATE / DELETE
    // ─────────────────────────────────────────────────────────────
    public void openEditDialog(Long id) {
        if (id == null) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Nenhuma matrícula selecionada!", "");
            return;
        }
        this.selectedId = id;

        EnrolmentDTO dto = enrolmentService.getAllEnrolments()
                .stream()
                .filter(e -> id.equals(e.getPhEnrolment()))
                .findFirst()
                .orElse(null);

        if (dto != null) {
            mapDtoFields(dto, editDto = new EnrolmentDTO());
            mapDtoFields(dto, selectedEnrolment);
            selectedStudentId = dto.getStudentPk();
            selectedSchoolClassId = dto.getSchoolclassPk();
        } else {
            addMessage(FacesMessage.SEVERITY_WARN, "Matrícula não encontrada", "");
        }
    }

    public void printEnrolmentPdf(Long id) {
        if (id == null) {
            addMessage(FacesMessage.SEVERITY_WARN, "Nenhuma matrícula selecionada!", "");
            return;
        }
        try {
            EnrolmentDTO enrolmentDto = enrolmentService.getAllEnrolments().stream()
                    .filter(en -> id.equals(en.getPhEnrolment()))
                    .findFirst()
                    .orElse(null);

            if (enrolmentDto == null) {
                addMessage(FacesMessage.SEVERITY_WARN, "Matrícula não encontrada", "");
                return;
            }

            StudentDTO studentDto = studentService.getAllStudents().stream()
                    .filter(s -> s.getPkStudent().equals(enrolmentDto.getStudentPk()))
                    .findFirst()
                    .orElse(null);

            if (studentDto == null) {
                addMessage(FacesMessage.SEVERITY_WARN, "Aluno associado não encontrado", "");
                return;
            }

            byte[] pdf = PdfReportService.generateEnrolmentReport(enrolmentDto, studentDto);
            String fileName = "matricula_" + enrolmentDto.getEnrolmentNumer() + ".pdf";
            PdfReportService.streamToResponse(pdf, fileName);

        } catch (DocumentException | IOException e) {
            LOGGER.log(Level.SEVERE, "Erro ao gerar PDF de matrícula", e);
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao gerar PDF", e.getMessage());
        }
    }

    public void loadSelectedEnrolment() {
        if (selectedId == null) {
            return;
        }

        EnrolmentDTO dto = enrolmentService.getAllEnrolments()
                .stream()
                .filter(e -> selectedId.equals(e.getPhEnrolment()))
                .findFirst()
                .orElse(null);

        if (dto != null) {
            mapDtoFields(dto, selectedEnrolment);
        } else {
            addMessage(FacesMessage.SEVERITY_WARN, "Matrícula não encontrada", "");
        }
    }

    private void mapDtoFields(EnrolmentDTO source, EnrolmentDTO target) {
        target.setPhEnrolment(source.getPhEnrolment());
        target.setEnrolmentNumer(source.getEnrolmentNumer());
        target.setShift(source.getShift());
        target.setEnrolmentType(source.getEnrolmentType());
        target.setStudentPk(source.getStudentPk());
        target.setStudentFullName(source.getStudentFullName());
        target.setStudentNumber(source.getStudentNumber());
        target.setSchoolclassPk(source.getSchoolclassPk());
        target.setSchoolclassnome(source.getSchoolclassnome());
        target.setSchoolclasscode(source.getSchoolclasscode());
        target.setEnrolmentData(source.getEnrolmentData());
        target.setObs(source.getObs());
        target.setCreatedAt(source.getCreatedAt());
        target.setUpdatedAt(source.getUpdatedAt());
    }

    public void saveUpdate() {
        try {
            if (selectedStudentId != null) {
                editDto.setStudentPk(selectedStudentId);
            }
            if (selectedSchoolClassId != null) {
                editDto.setSchoolclassPk(selectedSchoolClassId);
            }
            enrolmentService.update(editDto);
            init(); // Recarrega o lazy model e as estatísticas
            editDto = new EnrolmentDTO();
            selectedId = null;
            selectedStudentId = null;
            selectedSchoolClassId = null;
            addMessage(FacesMessage.SEVERITY_INFO, "Matrícula", "Matrícula atualizada com sucesso");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao atualizar matrícula", e);
            addMessage(FacesMessage.SEVERITY_ERROR, "Matrícula", e.getMessage());
        }
    }

    public void delete() {
        if (selectedId == null) {
            addMessage(FacesMessage.SEVERITY_WARN, "Nenhuma matrícula selecionada!", "");
            return;
        }
        try {
            enrolmentService.delete(selectedId);
            selectedId = null;
            init();
            addMessage(FacesMessage.SEVERITY_INFO, "Matrícula", "Matrícula eliminada com sucesso");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao eliminar matrícula", e);
            addMessage(FacesMessage.SEVERITY_ERROR, "Matrícula", e.getMessage());
        }
    }

    public void openDeleteDialog(Long id) {
        if (id == null) {
            addMessage(FacesMessage.SEVERITY_WARN, "Nenhuma matrícula selecionada!", "");
            return;
        }
        this.selectedId = id;
    }

    // ─────────────────────────────────────────────────────────────
    // EXPORTAR LISTA (imprimir / baixar)
    // ─────────────────────────────────────────────────────────────
    public void exportEnrolmentListPdf() {
        try {
            List<EnrolmentDTO> enrolments = enrolmentService.getAllEnrolments();

            if (enrolments == null || enrolments.isEmpty()) {
                addMessage(FacesMessage.SEVERITY_WARN, "Nenhuma matrícula para exportar", "");
                return;
            }

            byte[] pdf = PdfReportService.generateEnrolmentListReport(enrolments);
            String fileName = "lista_matriculas_" + java.time.LocalDate.now() + ".pdf";

            // inline = true -> abre no browser, permitindo imprimir e/ou baixar
            PdfReportService.streamToResponse(pdf, fileName, true);

        } catch (DocumentException | IOException e) {
            LOGGER.log(Level.SEVERE, "Erro ao exportar lista de matrículas", e);
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao exportar lista", e.getMessage());
        }
    }

    public void exportEnrolmentsByClassPdf(Long schoolClassPk) {
        if (schoolClassPk == null) {
            addMessage(FacesMessage.SEVERITY_WARN, "Nenhuma turma selecionada!", "");
            return;
        }
        try {
            SchoolClass schoolClass = schoolClassService.getById(schoolClassPk);
            List<EnrolmentDTO> enrolments = enrolmentService.getEnrolmentsBySchoolClassDTO(schoolClassPk);

            if (enrolments.isEmpty()) {
                addMessage(FacesMessage.SEVERITY_WARN, "Turma sem alunos matriculados", "");
                return;
            }

            byte[] pdf = PdfReportService.generateEnrolmentsByClassReport(schoolClass, enrolments);
            String fileName = "alunos_" + schoolClass.getClassCode() + "_" + java.time.LocalDate.now() + ".pdf";

            // inline = true -> abre no browser (permite imprimir e baixar)
            PdfReportService.streamToResponse(pdf, fileName, true);

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao exportar lista da turma", e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────────
    // UTIL
    // ─────────────────────────────────────────────────────────────
    public void printEnrolmentPdf() {
        if (selectedId == null) {
            addMessage(FacesMessage.SEVERITY_WARN, "Nenhuma matrícula selecionada!", "");
            return;
        }
        try {
            EnrolmentDTO enrolmentDto = enrolmentService.getAllEnrolments().stream()
                    .filter(en -> selectedId.equals(en.getPhEnrolment()))
                    .findFirst()
                    .orElse(null);

            if (enrolmentDto == null) {
                addMessage(FacesMessage.SEVERITY_WARN, "Matrícula não encontrada", "");
                return;
            }

            StudentDTO studentDto = studentService.getAllStudents().stream()
                    .filter(s -> s.getPkStudent().equals(enrolmentDto.getStudentPk()))
                    .findFirst()
                    .orElse(null);

            if (studentDto == null) {
                addMessage(FacesMessage.SEVERITY_WARN, "Aluno associado não encontrado", "");
                return;
            }

            byte[] pdf = PdfReportService.generateEnrolmentReport(enrolmentDto, studentDto);
            String fileName = "matricula_" + enrolmentDto.getEnrolmentNumer() + ".pdf";
            PdfReportService.streamToResponse(pdf, fileName);

        } catch (DocumentException | IOException e) {
            LOGGER.log(Level.SEVERE, "Erro ao gerar PDF de matrícula", e);
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao gerar PDF", e.getMessage());
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

    public Enrolment getEnrolment() {
        return enrolment;
    }

    public void setEnrolment(Enrolment enrolment) {
        this.enrolment = enrolment;
    }

    public EnrolmentDTO getEditDto() {
        return editDto;
    }

    public void setEditDto(EnrolmentDTO editDto) {
        this.editDto = editDto;
    }

    public EnrolmentDTO getSelectedEnrolment() {
        return selectedEnrolment;
    }

    public void setSelectedEnrolment(EnrolmentDTO selectedEnrolment) {
        this.selectedEnrolment = selectedEnrolment;
    }

    public Long getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(Long selectedId) {
        this.selectedId = selectedId;
    }

    public Long getSelectedStudentId() {
        return selectedStudentId;
    }

    public void setSelectedStudentId(Long selectedStudentId) {
        this.selectedStudentId = selectedStudentId;
    }

    public Long getSelectedSchoolClassId() {
        return selectedSchoolClassId;
    }

    public void setSelectedSchoolClassId(Long selectedSchoolClassId) {
        this.selectedSchoolClassId = selectedSchoolClassId;
    }

    public void setLazyModel(EnrolmentLazyModel lazyModel) {
        this.lazyModel = lazyModel;
    }

    public EnrolmentService getEnrolmentService() {
        return enrolmentService;
    }

    public void setEnrolmentService(EnrolmentService enrolmentService) {
        this.enrolmentService = enrolmentService;
    }

    // ─────────────────────────────────────────────────────────────
    // ESTATÍSTICAS — GETTERS (usados pelos cards da view)
    // ─────────────────────────────────────────────────────────────

    public long getTotalEnrolmentCount() {
        return totalEnrolmentCount;
    }

    public long getNewEnrolmentCount() {
        return newEnrolmentCount;
    }

    public long getDistinctClassesCount() {
        return distinctClassesCount;
    }

    public long getDistinctStudentsCount() {
        return distinctStudentsCount;
    }

    // ─────────────────────────────────────────────────────────────
    // ENUMS E LISTAS PARA DROPDOWNS
    // ─────────────────────────────────────────────────────────────

    public ShiftType[] getShifts() {
        return ShiftType.values();
    }

    public EnrolmentType[] getEnrolmentTypes() {
        return EnrolmentType.values();
    }

    public List<StudentDTO> getStudents() {
        return students;
    }

    public List<SchoolClass> getSchoolClasses() {
        return schoolClasses;
    }

    /**
     * Permite à view forçar a recarga da lista de alunos, por exemplo
     * depois de um novo aluno ser registado noutro ecrã/diálogo.
     */
    public void refreshStudents() {
        loadStudents();
    }

    /**
     * Permite à view forçar a recarga da lista de turmas, por exemplo
     * depois de uma nova turma ser registada noutro ecrã/diálogo.
     */
    public void refreshSchoolClasses() {
        loadSchoolClasses();
    }

    public List<EnrolmentDTO> getEnrolments() {
        return enrolmentService.getAllEnrolments();
    }
}