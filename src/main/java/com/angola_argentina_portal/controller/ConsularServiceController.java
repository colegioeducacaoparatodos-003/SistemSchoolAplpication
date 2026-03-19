package com.angola_argentina_portal.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.springframework.core.io.ClassPathResource;

import com.angola_argentina_portal.dto.ConsularServiceTableDTO;
import com.angola_argentina_portal.lazy.BookingLazyModel;
import com.angola_argentina_portal.lazy.ConsularServiceLazyModel;
import com.angola_argentina_portal.model.Booking;
import com.angola_argentina_portal.model.ConsularService;
import com.angola_argentina_portal.service.BookingService;
import com.angola_argentina_portal.service.ConsularServiceService;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import javax.sql.DataSource;

@Named
@ViewScoped
public class ConsularServiceController implements Serializable {

    private static final long serialVersionUID = 1L;

    private ConsularService consularService = new ConsularService();
    private Booking booking = new Booking();
    private LazyDataModel<ConsularServiceTableDTO> lazyModel;

    private int serviceId;
    private int selectedServiceId;
    @Inject
    private ConsularServiceService service;

    @Inject
    private BookingService bookingService;

    @Inject
    private DataSource dataSource;

    public void add() {
        try {

            service.save(consularService);

            // lazyModel = new ConsularServiceLazyModel(service);
            consularService = new ConsularService();

            addMessage(FacesMessage.SEVERITY_INFO,
                    "Consular Service",
                    "Saved successfully");

        } catch (Exception e) {
            e.printStackTrace();

            consularService = new ConsularService();

            addMessage(FacesMessage.SEVERITY_ERROR,
                    "Consular Service",
                    e.getMessage());
        }
    }

    public String loadConsularService() {
        try {

            lazyModel = new ConsularServiceLazyModel(service);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao processar notícias",
                            e.getMessage()));
            e.printStackTrace();
        }
        return "/management/consular-services.xhtml?faces-redirect=true";
    }

    public String goToBooking(int serviceId) {
        setServiceId(serviceId);
        return "/booking.xhtml?faces-redirect=true";
    }

    /*
     * public void booking() {
     * try {
     * 
     * booking.setFkService(serviceId);
     * bookingService.save(booking);
     * addMessage(FacesMessage.SEVERITY_INFO,
     * "Booking",
     * "Appointment scheduled successfully");
     * } catch (Exception e) {
     * 
     * e.printStackTrace();
     * booking = new Booking();
     * 
     * addMessage(FacesMessage.SEVERITY_ERROR,
     * "Booking",
     * e.getMessage());
     * }
     * }
     */

    public void booking() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext ec = facesContext.getExternalContext();

        try {

            booking.setFkService(serviceId);

            // salva primeiro
            Booking savedBooking = bookingService.save(booking);

            // parâmetros do report
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("id", savedBooking.getPkBooking()); // usa ID do booking

            try (InputStream reportStream = new ClassPathResource("reports/scheduled-service.jrxml").getInputStream();
                    OutputStream out = ec.getResponseOutputStream()) {

                ec.responseReset();

                JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

                JasperPrint jasperPrint = JasperFillManager.fillReport(
                        jasperReport,
                        parameters,
                        dataSource.getConnection());

                ec.setResponseContentType("application/pdf");
                ec.setResponseHeader("Content-Disposition",
                        "attachment; filename=scheduled-service.pdf");

                JasperExportManager.exportReportToPdfStream(jasperPrint, out);

                facesContext.responseComplete();
            }

            // NÃO usar mensagens aqui depois do responseComplete
            booking = new Booking();

        } catch (Exception e) {
            e.printStackTrace();

            booking = new Booking();

            addMessage(FacesMessage.SEVERITY_ERROR,
                    "Booking",
                    e.getMessage());
        }
    }

    public String loadConsularServicePage() {
        try {

            lazyModel = new ConsularServiceLazyModel(service);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao processar notícias",
                            e.getMessage()));
            e.printStackTrace();
        }
        return "/consular-services.xhtml?faces-redirect=true";
    }

    public void downloadConsularServiceReportPDF() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext ec = facesContext.getExternalContext();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", selectedServiceId);

        try (InputStream reportStream = new ClassPathResource("reports/consular-service.jrxml").getInputStream();
                OutputStream out = ec.getResponseOutputStream()) {

            ec.responseReset();

            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport, parameters, dataSource.getConnection());

            ec.setResponseContentType("application/pdf");
            ec.setResponseHeader("Content-Disposition", "attachment; filename=consular-service.pdf");

            JasperExportManager.exportReportToPdfStream(jasperPrint, out);

            facesContext.responseComplete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(severity, summary, detail));
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public ConsularService getConsularService() {
        return consularService;
    }

    public void setConsularService(ConsularService consularService) {
        this.consularService = consularService;
    }

    public LazyDataModel<ConsularServiceTableDTO> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<ConsularServiceTableDTO> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public ConsularServiceService getService() {
        return service;
    }

    public void setService(ConsularServiceService service) {
        this.service = service;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getSelectedServiceId() {
        return selectedServiceId;
    }

    public void setSelectedServiceId(int selectedServiceId) {
        this.selectedServiceId = selectedServiceId;
    }

    // getters and setters
}