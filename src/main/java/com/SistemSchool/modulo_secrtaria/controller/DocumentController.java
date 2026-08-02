package com.SistemSchool.modulo_secrtaria.controller;

import com.SistemSchool.modulo_secrtaria.dto.DocumentDTO;
import com.SistemSchool.modulo_secrtaria.dto.StudentDTO;
import com.SistemSchool.modulo_secrtaria.io.DocumentType;
import com.SistemSchool.modulo_secrtaria.lazy.DocumentLazyModel;
import com.SistemSchool.modulo_secrtaria.model.Document;
import com.SistemSchool.modulo_secrtaria.model.Student;
import com.SistemSchool.modulo_secrtaria.service.DocumentService;
import com.SistemSchool.modulo_secrtaria.service.StudentService;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@ViewScoped
public class DocumentController implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(DocumentController.class.getName());

    private static final String UPLOAD_RELATIVE_PATH = "/documents_files";

    // ─────────────────────────────────────────────────────────────
    // MODELOS
    // ─────────────────────────────────────────────────────────────

    private Document novoDocumento = new Document();
    private DocumentDTO editDto = new DocumentDTO();
    private Long selectedId;

    // Id do aluno escolhido no dropdown/autocomplete do formulário
    private Long selectedStudentId;

    // Lista de alunos para a view (dropdown), carregada uma vez
    private List<StudentDTO> students = new ArrayList<>();

    // Conteúdo do ficheiro lido para memória IMEDIATAMENTE no handleFileUpload().
    // Não guardamos a referência ao UploadedFile: o ficheiro temporário criado
    // pelo Tomcat para essa requisição de upload é apagado assim que ela
    // termina, então tentar ler o InputStream mais tarde (ex: no clique de
    // "Guardar", que é outra requisição ajax) lança NoSuchFileException.
    private byte[] uploadedFileBytes;
    private String uploadedFileName;

    // ─────────────────────────────────────────────────────────────
    // ESTATÍSTICAS (calculadas uma única vez em init(), não a cada getter)
    // ─────────────────────────────────────────────────────────────

    private long totalDocumentCount;
    private long expiringSoonCount;
    private long expiredCount;
    private long distinctStudentsCount;

    // ─────────────────────────────────────────────────────────────
    // SERVIÇOS
    // ─────────────────────────────────────────────────────────────

    @Inject
    private DocumentService documentService;

    @Inject
    private StudentService studentService;

    private transient DocumentLazyModel lazyModel;

    // ─────────────────────────────────────────────────────────────
    // INICIALIZAÇÃO E NAVEGAÇÃO
    // ─────────────────────────────────────────────────────────────

    @PostConstruct
    public void init() {
        lazyModel = new DocumentLazyModel(documentService);
        loadStudents();
        computeStatistics();
    }

    /**
     * Carrega a lista de alunos para a view (dropdown de seleção de aluno no
     * formulário de documento). Chamado no init() para evitar que o getter
     * dispare uma query à BD a cada chamada do EL.
     */
    private void loadStudents() {
        try {
            students = studentService.getAllStudents();
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao carregar alunos", e.getMessage());
            LOGGER.log(Level.SEVERE, "Erro ao carregar alunos para o formulário de documento", e);
        }
    }

    /**
     * Calcula os indicadores mostrados nos cards de estatística da view.
     * Executado uma única vez em init()/load() para não recalcular a cada
     * chamada do EL.
     */
    private void computeStatistics() {
        try {
            totalDocumentCount = documentService.getTotalDocumentCount();
            expiringSoonCount = documentService.getExpiringSoonCount();
            expiredCount = documentService.getExpiredCount();
            distinctStudentsCount = documentService.getDistinctStudentsCount();
        } catch (Exception e) {
            totalDocumentCount = 0;
            expiringSoonCount = 0;
            expiredCount = 0;
            distinctStudentsCount = 0;
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao calcular estatísticas", e.getMessage());
            LOGGER.log(Level.SEVERE, "Erro ao calcular estatísticas de documentos", e);
        }
    }

    public String load() {
        try {
            init(); // Recarrega o lazy model, a lista de alunos e as estatísticas
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao carregar documentos", e.getMessage());
            LOGGER.log(Level.SEVERE, "Erro ao carregar a listagem de documentos", e);
        }
        return "/management/secretaria/document.xhtml?faces-redirect=true";
    }

    public DocumentLazyModel getLazyModel() {
        return lazyModel;
    }

    // ─────────────────────────────────────────────────────────────
    // UPLOAD
    // ─────────────────────────────────────────────────────────────

    public void handleFileUpload(FileUploadEvent event) {
        try {
            UploadedFile file = event.getFile();
            this.uploadedFileBytes = file.getContent();
            this.uploadedFileName = file.getFileName();
            addMessage(FacesMessage.SEVERITY_INFO, "Ficheiro carregado",
                    "\"" + file.getFileName() + "\" pronto para ser guardado.");
        } catch (Exception e) {
            this.uploadedFileBytes = null;
            this.uploadedFileName = null;
            LOGGER.log(Level.SEVERE, "Erro ao ler ficheiro enviado", e);
            addMessage(FacesMessage.SEVERITY_ERROR, "Ficheiro", "Não foi possível ler o ficheiro enviado.");
        }
    }

    // ─────────────────────────────────────────────────────────────
    // CRUD
    // ─────────────────────────────────────────────────────────────

    /**
     * Prepara o diálogo de criação com um formulário limpo. Chamar isto
     * no actionListener do botão "Novo" (antes de abrir o dialog), senão
     * o formulário reaproveita dados deixados por uma edição anterior —
     * novoDocumento e selectedStudentId são campos do bean ViewScoped e
     * não são limpos automaticamente entre aberturas do diálogo.
     */
    public void openCreateDialog() {
        this.novoDocumento = new Document();
        this.uploadedFileBytes = null;
        this.uploadedFileName = null;
        this.selectedStudentId = null;
    }

    /**
     * IMPORTANTE: este método é chamado por um p:commandButton AJAX
     * (process="@form", update="..."). Por isso NÃO deve devolver um
     * outcome de navegação com faces-redirect: disparar um redirect de
     * página inteira dentro de uma resposta ajax parcial do PrimeFaces
     * impede o oncomplete do botão (fechar o dialog) e os updates
     * (mensagens + tabela) de serem aplicados corretamente. A
     * atualização da tabela e das mensagens já é feita via ajax pelo
     * próprio update do botão no XHTML.
     */
    public void salvarDocumento() {
        try {
            if (uploadedFileBytes == null) {
                addMessage(FacesMessage.SEVERITY_WARN, "Documento", "Selecione um ficheiro antes de guardar.");
                return;
            }
            if (selectedStudentId == null) {
                addMessage(FacesMessage.SEVERITY_WARN, "Documento", "Selecione o aluno associado ao documento.");
                return;
            }

            Student student = studentService.findById(selectedStudentId);
            novoDocumento.setStudent(student);

            String uploadBaseDir = resolveUploadDir();

            documentService.uploadDocument(
                    novoDocumento,
                    new ByteArrayInputStream(uploadedFileBytes),
                    uploadedFileName,
                    uploadBaseDir);

            resetCreateForm();
            init(); // Recarrega o lazy model e as estatísticas

            addMessage(FacesMessage.SEVERITY_INFO, "Documento", "Documento carregado com sucesso");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao guardar documento", e);
            addMessage(FacesMessage.SEVERITY_ERROR, "Documento", e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────────
    // EDIT / UPDATE / DELETE
    // ─────────────────────────────────────────────────────────────

    public void openEditDialog() {
        if (selectedId == null) {
            addMessage(FacesMessage.SEVERITY_WARN, "Nenhum documento selecionado!", "");
            return;
        }

        try {
            Document document = documentService.getById(selectedId);
            this.editDto = DocumentDTO.fromEntity(document);
            this.selectedStudentId = editDto.getStudentId();
            this.uploadedFileBytes = null; // limpa qualquer ficheiro pendente de outra ação
            this.uploadedFileName = null;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao carregar documento para edição", e);
            addMessage(FacesMessage.SEVERITY_WARN, "Documento não encontrado", "");
        }
    }

    public void saveUpdate() {
        try {
            // se o utilizador escolheu um novo ficheiro no diálogo de edição, substitui primeiro
            if (uploadedFileBytes != null) {
                String uploadBaseDir = resolveUploadDir();
                documentService.replaceDocumentFile(
                        editDto.getPhDocument(),
                        new ByteArrayInputStream(uploadedFileBytes),
                        uploadedFileName,
                        uploadBaseDir);
            }

            if (selectedStudentId != null) {
                editDto.setStudentId(selectedStudentId);
            }

            documentService.update(editDto);

            uploadedFileBytes = null;
            uploadedFileName = null;
            editDto = new DocumentDTO();
            selectedId = null;
            selectedStudentId = null;
            init(); // Recarrega o lazy model e as estatísticas

            addMessage(FacesMessage.SEVERITY_INFO, "Documento", "Documento atualizado com sucesso");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao atualizar documento", e);
            addMessage(FacesMessage.SEVERITY_ERROR, "Documento", e.getMessage());
        }
    }

    public void delete() {
        if (selectedId == null) {
            addMessage(FacesMessage.SEVERITY_WARN, "Nenhum documento selecionado!", "");
            return;
        }
        try {
            documentService.delete(selectedId, resolveUploadDir());
            selectedId = null;
            init(); // Recarrega o lazy model e as estatísticas
            addMessage(FacesMessage.SEVERITY_INFO, "Documento", "Documento eliminado com sucesso");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao eliminar documento", e);
            addMessage(FacesMessage.SEVERITY_ERROR, "Documento", e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────────
    // DOWNLOAD (botão com ajax="false")
    // ─────────────────────────────────────────────────────────────

    /**
     * Lê o id via request parameter (f:param "documentId") em vez de receber
     * o id como argumento de EL resolvido a partir de #{document.phDocument}.
     * Num p:dataTable lazy com botão ajax="false" (postback completo), a
     * variável de linha nem sempre está confiavelmente populada no momento
     * do decode/invoke_application — ler o parâmetro HTTP diretamente evita
     * essa dependência.
     */
    public void downloadDocumento() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();

        try {
            String idParam = externalContext.getRequestParameterMap().get("documentId");
            if (idParam == null || idParam.isBlank()) {
                addMessage(FacesMessage.SEVERITY_ERROR, "Documento", "Documento não especificado.");
                return;
            }
            Long documentId = Long.valueOf(idParam);

            Document document = documentService.getById(documentId);
            String uploadBaseDir = resolveUploadDir();

            byte[] fileBytes = documentService.downloadDocumentFile(documentId, uploadBaseDir);

            String encodedFileName = URLEncoder.encode(document.getFileName(), StandardCharsets.UTF_8)
                    .replace("+", "%20");

            externalContext.responseReset();
            externalContext.setResponseContentType(guessContentType(document.getFileName()));
            externalContext.setResponseContentLength(fileBytes.length);
            externalContext.setResponseHeader(
                    "Content-Disposition",
                    "attachment; filename=\"" + document.getFileName() + "\"; filename*=UTF-8''" + encodedFileName);

            try (OutputStream output = externalContext.getResponseOutputStream()) {
                output.write(fileBytes);
                output.flush();
            }

            facesContext.responseComplete();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao descarregar documento", e);
            addMessage(FacesMessage.SEVERITY_ERROR, "Documento", "Erro ao descarregar: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────────
    // UTIL
    // ─────────────────────────────────────────────────────────────

    private String resolveUploadDir() {
        String realPath = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRealPath(UPLOAD_RELATIVE_PATH);

        if (realPath == null) {
            realPath = System.getProperty("java.io.tmpdir") + UPLOAD_RELATIVE_PATH;
        }
        return realPath;
    }

    private String guessContentType(String fileName) {
        String lower = fileName.toLowerCase();
        if (lower.endsWith(".pdf")) return "application/pdf";
        if (lower.endsWith(".png")) return "image/png";
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
        if (lower.endsWith(".doc")) return "application/msword";
        if (lower.endsWith(".docx")) return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        return "application/octet-stream";
    }

    private void resetCreateForm() {
        this.novoDocumento = new Document();
        this.uploadedFileBytes = null;
        this.uploadedFileName = null;
        this.selectedStudentId = null;
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

    // ─────────────────────────────────────────────────────────────
    // GETTERS E SETTERS
    // ─────────────────────────────────────────────────────────────

    public Document getNovoDocumento() {
        return novoDocumento;
    }

    public void setNovoDocumento(Document novoDocumento) {
        this.novoDocumento = novoDocumento;
    }

    public DocumentDTO getEditDto() {
        return editDto;
    }

    public void setEditDto(DocumentDTO editDto) {
        this.editDto = editDto;
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

    public void setLazyModel(DocumentLazyModel lazyModel) {
        this.lazyModel = lazyModel;
    }

    // ─────────────────────────────────────────────────────────────
    // ESTATÍSTICAS — GETTERS (usados pelos cards da view)
    // ─────────────────────────────────────────────────────────────

    public long getTotalDocumentCount() {
        return totalDocumentCount;
    }

    public long getExpiringSoonCount() {
        return expiringSoonCount;
    }

    public long getExpiredCount() {
        return expiredCount;
    }

    public long getDistinctStudentsCount() {
        return distinctStudentsCount;
    }

    public String expiryStatus(LocalDate expiryDate) {
        if (expiryDate == null) {
            return "none";
        }
        LocalDate today = LocalDate.now();
        if (expiryDate.isBefore(today)) {
            return "expired";
        }
        if (!expiryDate.isAfter(today.plusDays(30))) {
            return "soon";
        }
        return "ok";
    }

    // ─────────────────────────────────────────────────────────────
    // ENUMS E LISTAS PARA DROPDOWNS
    // ─────────────────────────────────────────────────────────────

    public DocumentType[] getTiposDocumento() {
        return DocumentType.values();
    }

    public List<StudentDTO> getStudents() {
        return students;
    }

    /**
     * Permite à view forçar a recarga da lista de alunos, por exemplo
     * depois de um novo aluno ser registado noutro ecrã/diálogo.
     */
    public void refreshStudents() {
        loadStudents();
    }

    public List<DocumentDTO> getDocuments() {
        return documentService.getAllDocuments();
    }
}