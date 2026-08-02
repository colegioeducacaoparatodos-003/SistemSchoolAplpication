package com.SistemSchool.modulo_secrtaria.controller;

import com.SistemSchool.modulo_secrtaria.dto.StudentDTO;
import com.SistemSchool.io.Gender;
import com.SistemSchool.modulo_secrtaria.lazy.StudentLazyModel;
import com.SistemSchool.modulo_secrtaria.io.StudentStatus;
import com.SistemSchool.modulo_secrtaria.model.Student;
import com.SistemSchool.modulo_secrtaria.service.StudentService;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;

import org.primefaces.model.file.UploadedFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.SistemSchool.report.PdfReportService;
import com.SistemSchool.service.BIValidationService;
import com.itextpdf.text.DocumentException;
import java.io.IOException;

@Named
@ViewScoped
public class StudentController implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String STUDENT_IMG_FOLDER = "student_img";
    private static final String STUDENT_IMG_WEB = "/" + STUDENT_IMG_FOLDER + "/";

    // ─────────────────────────────────────────────────────────────
    // MODELOS
    // ─────────────────────────────────────────────────────────────

    private Student student = new Student();

    private StudentDTO editDto = new StudentDTO();
    private StudentDTO selectedStudent = new StudentDTO();
    private Long selectedId;

    private UploadedFile uploadedPhoto;

    private long totalStudentCount;
    private long activeStudentCount;
    private long inactiveStudentCount;
    private long newStudentCount;

    

    // ─────────────────────────────────────────────────────────────
    // SERVIÇOS
    // ─────────────────────────────────────────────────────────────

    @Inject
    private StudentService studentService;

    @Inject
    private BIValidationService biValidationService;

    private transient StudentLazyModel lazyModel;

    // ─────────────────────────────────────────────────────────────
    // INICIALIZAÇÃO E NAVEGAÇÃO
    // ─────────────────────────────────────────────────────────────

    @PostConstruct
    public void init() {
        lazyModel = new StudentLazyModel(studentService);
        loadStatistics();
    }

    private void loadStatistics() {
        totalStudentCount = studentService.countAll();
        activeStudentCount = studentService.countByStatus(StudentStatus.ACTIVE);
        inactiveStudentCount = studentService.countByStatus(StudentStatus.INACTIVE);

        List<StudentDTO> allStudents = studentService.getAllStudents();
        if (allStudents == null) {
            newStudentCount = 0;
            return;
        }

        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        newStudentCount = allStudents.stream()
                .filter(s -> s.getCreatedAt() != null && s.getCreatedAt().isAfter(thirtyDaysAgo))
                .count();
    }

    public String loadStudents() {
        try {
            // A inicialização já foi feita no @PostConstruct
            // Se precisar recarregar, pode chamar o init() ou recriar o modelo aqui.
            lazyModel = new StudentLazyModel(studentService); // Mantido para garantir recarga na navegação
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao carregar alunos", e.getMessage());
            e.printStackTrace();
        }
        return "/management/secretaria/students.xhtml?faces-redirect=true";
    }

    public StudentLazyModel getLazyModel() {
        return lazyModel;
    }

    // ─────────────────────────────────────────────────────────────
    // CRUD
    // ─────────────────────────────────────────────────────────────

    public void prepareNewStudent() {
        student = new Student();
        student.setSudentNumber(studentService.generateNextStudentNumber());
        uploadedPhoto = null;
    }

    public String saveStudent() {
        try {
            // 0. Validar BI antes de qualquer persistência
            if (!biValidationService.validar(student.getBiNumber())) {
                addMessage(FacesMessage.SEVERITY_ERROR, "BI inválido",
                        "Informe um número de BI válido (formato: 9 dígitos + 2 letras + 3 dígitos, ex: 123456789LA042).");
                return null;
            }

            // 1. Upload da foto
            processPhotoUpload();

            // 2. Persistir o aluno
            studentService.save(student);

            // 3. Repor estado
            student = new Student();
            uploadedPhoto = null;
            init();

            FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getFlash()
                    .setKeepMessages(true);

            addMessage(FacesMessage.SEVERITY_INFO, "Aluno", "Aluno registado com sucesso");

            return "/management/secretaria/students.xhtml?faces-redirect=true";

        } catch (Exception e) {
            e.printStackTrace();
            addMessage(FacesMessage.SEVERITY_ERROR, "Aluno", e.getMessage());
            return null;
        }
    }

    public void validarBI() {
        if (!biValidationService.validar(student.getBiNumber())) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "BI inválido",
                            "Formato esperado: 9 dígitos + 2 letras + 3 dígitos."));
        }
    }

    /**
     * Lógica de upload centralizada, chamada dentro do saveStudent.
     */
    private void processPhotoUpload() throws IOException {
        if (uploadedPhoto == null || uploadedPhoto.getContent() == null
                || uploadedPhoto.getContent().length == 0) {
            return;
        }

        // Adicionando a validação do tamanho do arquivo aqui. 2MB = 2 * 1024 * 1024
        // bytes.
        if (uploadedPhoto.getSize() > 2097152) {
            throw new IOException("O arquivo excede o tamanho máximo de 2MB.");
        }

        if (uploadedPhoto.getContent().length > 2 * 1024 * 1024) {
            throw new IllegalArgumentException("A foto não pode exceder 2 MB.");
        }

        String originalName = uploadedPhoto.getFileName();
        if (originalName == null || !originalName.matches("(?i).+\\.(jpg|jpeg|png|webp)$")) {
            throw new IllegalArgumentException("Apenas ficheiros JPG, PNG ou WEBP são permitidos.");
        }

        String realPath = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRealPath(STUDENT_IMG_WEB);

        Path uploadDir = Paths.get(realPath);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        String extension = originalName.contains(".")
                ? originalName.substring(originalName.lastIndexOf(".")).toLowerCase()
                : ".jpg";
        String uniqueName = UUID.randomUUID().toString() + extension;

        Path destination = uploadDir.resolve(uniqueName);
        try (InputStream is = uploadedPhoto.getInputStream()) {
            Files.copy(is, destination, StandardCopyOption.REPLACE_EXISTING);
        }

        student.setUploadPhoto(STUDENT_IMG_WEB + uniqueName);
    }

    // ─────────────────────────────────────────────────────────────
    // EDIT / UPDATE / DELETE
    // ─────────────────────────────────────────────────────────────
    public void openEditDialog(Long id) {
        if (id == null) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Nenhum aluno selecionado!", "");
            return;
        }
        this.selectedId = id;

        StudentDTO dto = studentService.getAllStudents()
                .stream()
                .filter(s -> s.getPkStudent().equals(id))
                .findFirst()
                .orElse(null);

        if (dto != null) {
            mapDtoFields(dto, editDto = new StudentDTO());
            mapDtoFields(dto, selectedStudent);
        } else {
            addMessage(FacesMessage.SEVERITY_WARN, "Aluno não encontrado", "");
        }
    }

    public void delete(Long id) {
        if (id == null)
            return;
        try {
            studentService.delete(id);
            selectedId = null;
            init();
            addMessage(FacesMessage.SEVERITY_INFO, "Aluno", "Aluno eliminado com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
            addMessage(FacesMessage.SEVERITY_ERROR, "Aluno", e.getMessage());
        }
    }

    public void printStudentPdf(Long id) {
        if (id == null) {
            addMessage(FacesMessage.SEVERITY_WARN, "Nenhum aluno selecionado!", "");
            return;
        }
        try {
            StudentDTO dto = studentService.getAllStudents().stream()
                    .filter(s -> s.getPkStudent().equals(id))
                    .findFirst()
                    .orElse(null);

            if (dto == null) {
                addMessage(FacesMessage.SEVERITY_WARN, "Aluno não encontrado", "");
                return;
            }

            byte[] pdf = PdfReportService.generateStudentReport(dto);
            String fileName = "aluno_" + dto.getSudentNumber() + ".pdf";
            PdfReportService.streamToResponse(pdf, fileName);

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao gerar PDF", e.getMessage());
        }
    }

    public void loadSelectedStudent() {
        if (selectedId == null || selectedId == 0) {
            return;
        }

        StudentDTO dto = studentService.getAllStudents()
                .stream()
                .filter(s -> s.getPkStudent().equals(selectedId))
                .findFirst()
                .orElse(null);

        if (dto != null) {
            mapDtoFields(dto, selectedStudent);
        } else {
            addMessage(FacesMessage.SEVERITY_WARN, "Aluno não encontrado", "");
        }
    }

    private void mapDtoFields(StudentDTO source, StudentDTO target) {
        target.setPkStudent(source.getPkStudent());
        target.setSudentNumber(source.getSudentNumber());
        target.setFristName(source.getFristName());
        target.setLastName(source.getLastName());
        target.setFullName(source.getFullName());
        target.setGender(source.getGender());
        target.setBiNumber(source.getBiNumber());
        target.setNascDate(source.getNascDate());
        target.setBiExpiryData(source.getBiExpiryData());
        target.setAddressStreet(source.getAddressStreet());
        target.setAddressProvice(source.getAddressProvice());
        target.setNameFather(source.getNameFather());
        target.setNameMather(source.getNameMather());
        target.setEmail(source.getEmail());
        target.setPhone_1(source.getPhone_1());
        target.setPhone_2(source.getPhone_2());
        target.setUploadPhoto(source.getUploadPhoto());
        target.setStatus(source.getStatus());
        target.setObs(source.getObs());
        target.setCreatedAt(source.getCreatedAt());
        target.setUpdatedAt(source.getUpdatedAt());
    }

    public void saveUpdate() {
        try {
            if (!biValidationService.validar(editDto.getBiNumber())) {
                addMessage(FacesMessage.SEVERITY_ERROR, "BI inválido",
                        "Informe um número de BI válido.");
                return;
            }

            studentService.update(editDto);
            init();
            editDto = new StudentDTO();
            selectedId = null;
            addMessage(FacesMessage.SEVERITY_INFO, "Aluno", "Aluno atualizado com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
            addMessage(FacesMessage.SEVERITY_ERROR, "Aluno", e.getMessage());
        }
    }

    public void delete() {
        try {
            studentService.delete(selectedId);
            selectedId = null;
            init(); // Recarrega o lazy model e as estatísticas
            addMessage(FacesMessage.SEVERITY_INFO, "Aluno", "Aluno eliminado com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
            addMessage(FacesMessage.SEVERITY_ERROR, "Aluno", e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────────
    // PDF
    // ─────────────────────────────────────────────────────────────
    public void printStudentPdf() {
        if (selectedId == null) {
            addMessage(FacesMessage.SEVERITY_WARN, "Nenhum aluno selecionado!", "");
            return;
        }
        try {
            StudentDTO dto = studentService.getAllStudents().stream()
                    .filter(s -> s.getPkStudent().equals(selectedId))
                    .findFirst()
                    .orElse(null);

            if (dto == null) {
                addMessage(FacesMessage.SEVERITY_WARN, "Aluno não encontrado", "");
                return;
            }

            byte[] pdf = PdfReportService.generateStudentReport(dto);
            String fileName = "aluno_" + dto.getSudentNumber() + ".pdf";
            PdfReportService.streamToResponse(pdf, fileName);

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao gerar PDF", e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────────
    // EXPORTAR LISTA (imprimir / baixar)
    // ─────────────────────────────────────────────────────────────
    public void exportStudentListPdf() {
        try {
            List<StudentDTO> students = studentService.getAllStudents();

            if (students == null || students.isEmpty()) {
                addMessage(FacesMessage.SEVERITY_WARN, "Nenhum aluno para exportar", "");
                return;
            }

            byte[] pdf = PdfReportService.generateStudentListReport(students);
            String fileName = "lista_alunos_" + java.time.LocalDate.now() + ".pdf";

            // inline = true -> abre no browser, permitindo imprimir e/ou baixar
            PdfReportService.streamToResponse(pdf, fileName, true);

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao exportar lista", e.getMessage());
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

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public UploadedFile getUploadedPhoto() {
        return uploadedPhoto;
    }

    public void setUploadedPhoto(UploadedFile uploadedPhoto) {
        this.uploadedPhoto = uploadedPhoto;
    }

    public StudentDTO getEditDto() {
        return editDto;
    }

    public void setEditDto(StudentDTO editDto) {
        this.editDto = editDto;
    }

    public StudentDTO getSelectedStudent() {
        return selectedStudent;
    }

    public void setSelectedStudent(StudentDTO selectedStudent) {
        this.selectedStudent = selectedStudent;
    }

    public Long getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(Long selectedId) {
        this.selectedId = selectedId;
    }

    public void setLazyModel(StudentLazyModel lazyModel) {
        this.lazyModel = lazyModel;
    }

    public StudentService getStudentService() {
        return studentService;
    }

    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    // ─────────────────────────────────────────────────────────────
    // ENUMS PARA DROPDOWNS
    // ─────────────────────────────────────────────────────────────

    public Gender[] getGenders() {
        return Gender.values();
    }

    public StudentStatus[] getStudentStatuses() {
        return StudentStatus.values();
    }

    public java.util.List<StudentDTO> getStudents() {
        return studentService.getAllStudents();
    }

    // Métodos para estatísticas

    public long getTotalStudentCount() {
        return totalStudentCount;
    }

    public long getActiveStudentCount() {
        return activeStudentCount;
    }

    public long getInactiveStudentCount() {
        return inactiveStudentCount;
    }

    public long getNewStudentCount() {
        return newStudentCount;
    }
}