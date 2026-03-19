package com.angola_argentina_portal.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pkBooking;

    private int fkService;

    // Dados do cidadão
    private String fullName;
    private String email;
    private String phone;

    // Agendamento
    private LocalDate bookingDate;
    private String bookingTime;

    private String status; // PENDING | CONFIRMED | CANCELLED

    private LocalDate createdDate;

    @PrePersist
    public void onCreate() {
        this.createdDate = LocalDate.now();
        this.status = "PENDING";
    }

    public Booking() {
        super();
    }

    public int getPkBooking() {
        return pkBooking;
    }

    public void setPkBooking(int pkBooking) {
        this.pkBooking = pkBooking;
    }

    public int getFkService() {
        return fkService;
    }

    public void setFkService(int fkService) {
        this.fkService = fkService;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    // getters and setters
}