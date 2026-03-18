package com.angola_argentina_portal.controller;

import java.io.Serializable;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import org.primefaces.model.LazyDataModel;

import com.angola_argentina_portal.dto.BookingTableDTO;
import com.angola_argentina_portal.dto.ConsularServiceTableDTO;
import com.angola_argentina_portal.lazy.BookingLazyModel;
import com.angola_argentina_portal.lazy.ConsularServiceLazyModel;
import com.angola_argentina_portal.model.Booking;
import com.angola_argentina_portal.model.ConsularService;
import com.angola_argentina_portal.service.BookingService;
import com.angola_argentina_portal.service.ConsularServiceService;

@Named
@ViewScoped
public class BookingController implements Serializable {

    private static final long serialVersionUID = 1L;

    private Booking booking = new Booking();
    private LazyDataModel<BookingTableDTO> lazyModel;

    @Inject
    private BookingService bookingService;

    /*
     * @PostConstruct
     * public void init() {
     * lazyModel = new BookingLazyModel(bookingService);
     * }
     */

    public void add() {
        try {

            bookingService.save(booking);

            lazyModel = new BookingLazyModel(bookingService);
            booking = new Booking();

            addMessage(FacesMessage.SEVERITY_INFO,
                    "Booking",
                    "Appointment scheduled successfully");

        } catch (Exception e) {

            e.printStackTrace();
            booking = new Booking();

            addMessage(FacesMessage.SEVERITY_ERROR,
                    "Booking",
                    e.getMessage());
        }
    }

    public void confirm(int id) {
        try {
            Booking booking = bookingService.findById(id);
            booking.setStatus("CONFIRMED");
            bookingService.save(booking);

            addMessage(FacesMessage.SEVERITY_INFO,
                    "Booking",
                    "Confirmed successfully");

        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR,
                    "Error",
                    e.getMessage());
        }
    }

    public void delete(int id) {
        try {
            bookingService.delete(id);

            addMessage(FacesMessage.SEVERITY_INFO,
                    "Booking",
                    "Deleted successfully");

        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR,
                    "Error",
                    e.getMessage());
        }
    }

    public String loadBooking() {
        try {

            lazyModel = new BookingLazyModel(bookingService);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao processar",
                            e.getMessage()));
            e.printStackTrace();
        }
        return "/management/bookings.xhtml?faces-redirect=true";

    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(severity, summary, detail));
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public LazyDataModel<BookingTableDTO> getLazyModel() {
        return lazyModel;
    }

    public void setLazyModel(LazyDataModel<BookingTableDTO> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public BookingService getBookingService() {
        return bookingService;
    }

    public void setBookingService(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // getters and setters
}
