package com.SistemSchool.report;

import com.SistemSchool.modulo_secrtaria.dto.EnrolmentDTO;
import com.SistemSchool.modulo_secrtaria.dto.PagamentoDTO;
import com.SistemSchool.modulo_secrtaria.dto.StudentDTO;
import com.SistemSchool.modulo_secrtaria.model.SchoolClass;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Geração centralizada de PDFs (fichas de aluno e de matrícula).
 * Todas as cores usam BaseColor (iText 5) — NUNCA java.awt.Color.
 */
public final class PdfReportService {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private static final BaseColor COLOR_RED = new BaseColor(200, 16, 46);
    private static final BaseColor COLOR_GRAY_BG = new BaseColor(247, 247, 248);
    private static final BaseColor COLOR_BORDER = new BaseColor(230, 230, 232);

    private static final Font FONT_SECTION = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, COLOR_RED);
    private static final Font FONT_LABEL = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, BaseColor.DARK_GRAY);
    private static final Font FONT_VALUE = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.BLACK);

    private PdfReportService() {
    }

    // ============================================================
    // 1) Matrícula + dados completos do aluno
    // ============================================================
    public static byte[] generateEnrolmentReport(EnrolmentDTO enrolment, StudentDTO student) throws DocumentException {
        Document document = new Document(PageSize.A4, 40, 40, 90, 50);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PdfWriter writer = PdfWriter.getInstance(document, baos);
        writer.setPageEvent(new ReportPageEvent("Ficha de Matrícula"));

        document.open();

        addSectionTitle(document, "Dados da Matrícula");
        document.add(buildEnrolmentTable(enrolment));
        document.add(spacer());

        addSectionTitle(document, "Dados do Aluno");
        document.add(buildStudentTable(student));

        document.close();
        return baos.toByteArray();
    }

    // ============================================================
    // 2) Ficha completa do aluno
    // ============================================================
    public static byte[] generateStudentReport(StudentDTO student) throws DocumentException {
        Document document = new Document(PageSize.A4, 40, 40, 90, 50);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PdfWriter writer = PdfWriter.getInstance(document, baos);
        writer.setPageEvent(new ReportPageEvent("Ficha do Aluno"));

        document.open();

        addSectionTitle(document, "Dados do Aluno");
        document.add(buildStudentTable(student));

        document.close();
        return baos.toByteArray();
    }

    // ============================================================
    // Streaming da resposta HTTP (download do PDF)
    // ============================================================
    public static void streamToResponse(byte[] pdfBytes, String fileName) throws IOException {
        streamToResponse(pdfBytes, fileName, false); // comportamento antigo: sempre download
    }

    public static void streamToResponse(byte[] pdfBytes, String fileName, boolean inline) throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();

        externalContext.responseReset();
        externalContext.setResponseContentType("application/pdf");
        externalContext.setResponseContentLength(pdfBytes.length);

        String disposition = inline ? "inline" : "attachment";
        externalContext.setResponseHeader("Content-Disposition", disposition + "; filename=\"" + fileName + "\"");

        try (OutputStream out = externalContext.getResponseOutputStream()) {
            out.write(pdfBytes);
            out.flush();
        }

        facesContext.responseComplete();
    }

    // ============================================================
    // Construção das tabelas
    // ============================================================

    private static PdfPTable buildEnrolmentTable(EnrolmentDTO e) {
        PdfPTable table = newFormTable();
        addRow(table, "Nº de Matrícula", e.getEnrolmentNumer());
        addRow(table, "Turma", e.getSchoolclasscode());
        addRow(table, "Nome da Turma", e.getSchoolclassnome());
        addRow(table, "Turno", e.getShift());
        addRow(table, "Tipo de Matrícula", e.getEnrolmentType());
        addRow(table, "Aluno", e.getStudentFullName());
        addRow(table, "Nº do Aluno", e.getStudentNumber());
        addRow(table, "Data da Matrícula", e.getEnrolmentData());
        addRow(table, "Observações", e.getObs());
        addRow(table, "Criado em", e.getCreatedAt());
        addRow(table, "Actualizado em", e.getUpdatedAt());
        return table;
    }

    private static PdfPTable buildStudentTable(StudentDTO s) {
        PdfPTable table = newFormTable();
        addRow(table, "Nº de Estudante", s.getSudentNumber());
        addRow(table, "Nome Completo", s.getFullName());
        addRow(table, "Primeiro Nome", s.getFristName());
        addRow(table, "Último Nome", s.getLastName());
        addRow(table, "Género", s.getGender());
        addRow(table, "Nº do BI", s.getBiNumber());
        addRow(table, "Data de Nascimento", s.getNascDate());
        addRow(table, "Validade do BI", s.getBiExpiryData());
        addRow(table, "Endereço", s.getAddressStreet());
        addRow(table, "Província", s.getAddressProvice());
        addRow(table, "Nome do Pai", s.getNameFather());
        addRow(table, "Nome da Mãe", s.getNameMather());
        addRow(table, "Email", s.getEmail());
        addRow(table, "Telefone 1", s.getPhone_1());
        addRow(table, "Telefone 2", s.getPhone_2());
        addRow(table, "Estado", s.getStatus());
        addRow(table, "Observações", s.getObs());
        addRow(table, "Criado em", s.getCreatedAt());
        addRow(table, "Actualizado em", s.getUpdatedAt());
        return table;
    }

    private static PdfPTable newFormTable() {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        try {
            table.setWidths(new float[] { 30f, 70f });
        } catch (DocumentException ex) {
            // larguras fixas e válidas; nunca deve ocorrer
        }
        table.setSpacingAfter(4f);
        return table;
    }

    private static void addRow(PdfPTable table, String label, Object value) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, FONT_LABEL));
        labelCell.setBackgroundColor(COLOR_GRAY_BG);
        labelCell.setPadding(6f);
        labelCell.setBorderColor(COLOR_BORDER);
        labelCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell valueCell = new PdfPCell(new Phrase(fmt(value), FONT_VALUE));
        valueCell.setPadding(6f);
        valueCell.setBorderColor(COLOR_BORDER);
        valueCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private static void addSectionTitle(Document document, String text) throws DocumentException {
        Paragraph title = new Paragraph(text, FONT_SECTION);
        title.setSpacingBefore(4f);
        title.setSpacingAfter(4f);
        document.add(title);

        LineSeparator separator = new LineSeparator(1f, 100f, COLOR_BORDER, Element.ALIGN_LEFT, -2);
        document.add(new Chunk(separator));
        document.add(Chunk.NEWLINE);
    }

    private static Paragraph spacer() {
        Paragraph p = new Paragraph(" ");
        p.setSpacingAfter(10f);
        return p;
    }

    private static String fmt(Object value) {
        if (value == null) {
            return "-";
        }
        if (value instanceof LocalDate) {
            return ((LocalDate) value).format(DATE_FMT);
        }
        if (value instanceof LocalDateTime) {
            return ((LocalDateTime) value).format(DATETIME_FMT);
        }
        if (value instanceof Enum) {
            return ((Enum<?>) value).name();
        }
        String s = value.toString().trim();
        return s.isEmpty() ? "-" : s;
    }

    // ============================================================
    // 3) Lista de Alunos (tabela) — para exportar/imprimir
    // ============================================================
    public static byte[] generateStudentListReport(java.util.List<StudentDTO> students) throws DocumentException {
        Document document = new Document(PageSize.A4.rotate(), 30, 30, 90, 50);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PdfWriter writer = PdfWriter.getInstance(document, baos);
        writer.setPageEvent(new ReportPageEvent("Lista de Alunos"));

        document.open();

        addSectionTitle(document, "Lista de Alunos (" + students.size() + ")");
        document.add(buildStudentListTable(students));

        document.close();
        return baos.toByteArray();
    }

    private static PdfPTable buildStudentListTable(java.util.List<StudentDTO> students) {
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        try {
            table.setWidths(new float[] { 14f, 26f, 10f, 16f, 12f, 14f, 10f });
        } catch (DocumentException ex) {
            // larguras fixas e válidas; nunca deve ocorrer
        }

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8.5f, BaseColor.WHITE);
        BaseColor headerBg = new BaseColor(20, 20, 20);

        String[] headers = { "Nº Aluno", "Nome Completo", "", "Nº BI", "Data Nasc.", "Telefone", "Estado" };
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
            cell.setBackgroundColor(headerBg);
            cell.setPadding(6f);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        boolean alternate = false;
        for (StudentDTO s : students) {
            BaseColor rowBg = alternate ? COLOR_GRAY_BG : BaseColor.WHITE;

            addListCell(table, fmt(s.getSudentNumber()), rowBg);
            addListCell(table, fmt(s.getFullName()), rowBg);
            addListCell(table, fmt(s.getGender()), rowBg);
            addListCell(table, fmt(s.getBiNumber()), rowBg);
            addListCell(table, fmt(s.getNascDate()), rowBg);
            addListCell(table, fmt(s.getPhone_1()), rowBg);
            addListCell(table, fmt(s.getStatus()), rowBg);

            alternate = !alternate;
        }

        return table;
    }

    private static void addListCell(PdfPTable table, String text, BaseColor bg) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FONT_VALUE));
        cell.setBackgroundColor(bg);
        cell.setPadding(5f);
        cell.setBorderColor(COLOR_BORDER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
    }

    // ============================================================
    // 4) Lista de Pagamentos (tabela) — para exportar/imprimir
    // ============================================================
    public static byte[] generatePagamentoListReport(java.util.List<PagamentoDTO> pagamentos) throws DocumentException {
        Document document = new Document(PageSize.A4.rotate(), 30, 30, 90, 50);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PdfWriter writer = PdfWriter.getInstance(document, baos);
        writer.setPageEvent(new ReportPageEvent("Lista de Pagamentos"));

        document.open();

        addSectionTitle(document, "Lista de Pagamentos (" + pagamentos.size() + ")");
        document.add(buildPagamentoListTable(pagamentos));

        document.close();
        return baos.toByteArray();
    }

    private static PdfPTable buildPagamentoListTable(java.util.List<PagamentoDTO> pagamentos) {
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        try {
            table.setWidths(new float[] { 18f, 34f, 24f, 24f });
        } catch (DocumentException ex) {
            // larguras fixas e válidas; nunca deve ocorrer
        }

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8.5f, BaseColor.WHITE);
        BaseColor headerBg = new BaseColor(20, 20, 20);

        String[] headers = { "Nº Pagamento", "Aluno", "Método de Pagamento", "Data do Pagamento" };
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
            cell.setBackgroundColor(headerBg);
            cell.setPadding(6f);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        boolean alternate = false;
        for (PagamentoDTO p : pagamentos) {
            BaseColor rowBg = alternate ? COLOR_GRAY_BG : BaseColor.WHITE;

            addListCell(table, fmt(p.getNumeroDocumento()), rowBg);
            addListCell(table, fmt(p.getStudentFullName()), rowBg);
            addListCell(table, fmt(p.getFormaPagamento()), rowBg);
            addListCell(table, fmt(p.getDataPagamento()), rowBg);

            alternate = !alternate;
        }

        return table;
    }

    // ============================================================
    // 5) Lista de Matrículas (tabela) — para exportar/imprimir
    // ============================================================
    public static byte[] generateEnrolmentListReport(java.util.List<EnrolmentDTO> enrolments) throws DocumentException {
        Document document = new Document(PageSize.A4.rotate(), 30, 30, 90, 50);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PdfWriter writer = PdfWriter.getInstance(document, baos);
        writer.setPageEvent(new ReportPageEvent("Lista de Matrículas"));

        document.open();

        addSectionTitle(document, "Lista de Matrículas (" + enrolments.size() + ")");
        document.add(buildEnrolmentListTable(enrolments));

        document.close();
        return baos.toByteArray();
    }

    private static PdfPTable buildEnrolmentListTable(java.util.List<EnrolmentDTO> enrolments) {
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        try {
            table.setWidths(new float[] { 13f, 26f, 13f, 12f, 12f, 12f, 12f });
        } catch (DocumentException ex) {
            // larguras fixas e válidas; nunca deve ocorrer
        }

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8.5f, BaseColor.WHITE);
        BaseColor headerBg = new BaseColor(20, 20, 20);

        String[] headers = { "Nº Matrícula", "Aluno", "Nº Aluno", "Turma", "Turno", "Tipo", "Data" };
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
            cell.setBackgroundColor(headerBg);
            cell.setPadding(6f);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        boolean alternate = false;
        for (EnrolmentDTO e : enrolments) {
            BaseColor rowBg = alternate ? COLOR_GRAY_BG : BaseColor.WHITE;

            addListCell(table, fmt(e.getEnrolmentNumer()), rowBg);
            addListCell(table, fmt(e.getStudentFullName()), rowBg);
            addListCell(table, fmt(e.getStudentNumber()), rowBg);
            addListCell(table, fmt(e.getSchoolclasscode()), rowBg);
            addListCell(table, fmt(e.getShift()), rowBg);
            addListCell(table, fmt(e.getEnrolmentType()), rowBg);
            addListCell(table, fmt(e.getEnrolmentData()), rowBg);

            alternate = !alternate;
        }

        return table;
    }

    public static byte[] generateEnrolmentsByClassReport(SchoolClass schoolClass, List<EnrolmentDTO> enrolments)
            throws DocumentException, IOException {

        Document document = new Document(PageSize.A4.rotate(), 24, 24, 40, 30);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        document.open();

        Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Font subFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.WHITE);
        Font cellFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL);

        Paragraph title = new Paragraph("Lista de Alunos por Turma", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Paragraph info = new Paragraph(
                "Turma: " + schoolClass.getClassName() + " (" + schoolClass.getClassCode() + ")"
                        + "   |   Ano Lectivo: " + schoolClass.getAnoLectivo()
                        + "   |   Turno: " + (schoolClass.getTurno() != null ? schoolClass.getTurno() : "-"),
                subFont);
        info.setSpacingAfter(12f);
        document.add(info);

        PdfPTable table = new PdfPTable(new float[] { 0.6f, 1.2f, 2.6f, 1f, 1.4f });
        table.setWidthPercentage(100);

        String[] headers = { "Nº", "Nº Matrícula", "Nome do Aluno", "Turno", "Data de Matrícula" };
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
            cell.setBackgroundColor(new BaseColor(30, 30, 30));
            cell.setPadding(6f);
            table.addCell(cell);
        }

        int i = 1;
        for (EnrolmentDTO e : enrolments) {
            table.addCell(new PdfPCell(new Phrase(String.valueOf(i++), cellFont)));
            table.addCell(new PdfPCell(new Phrase(e.getEnrolmentNumer(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(e.getStudentFullName(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(e.getShift() != null ? e.getShift().toString() : "-", cellFont)));
            table.addCell(new PdfPCell(new Phrase(
                    e.getEnrolmentData() != null ? e.getEnrolmentData().toString() : "-", cellFont)));
        }

        document.add(table);

        Paragraph footer = new Paragraph("Total de alunos: " + enrolments.size(), subFont);
        footer.setSpacingBefore(10f);
        document.add(footer);

        document.close();
        return baos.toByteArray();
    }
}