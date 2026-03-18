package com.angola_argentina_portal.dto;

import java.time.LocalDate;

public class BookingTableDTO {

    private int pkBooking;
    private String serviceName;
    private String fullName;
    private String email;
    private String phone;
    private LocalDate bookingDate;
    private String bookingTime;
    private String status;

    public BookingTableDTO(
            int pkBooking,
            String serviceName,
            String fullName,
            String email,
            String phone,
            LocalDate bookingDate,
            String bookingTime,
            String status) {

        this.pkBooking = pkBooking;
        this.serviceName = serviceName;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
        this.status = status;
    }

    public int getPkBooking() {
        return pkBooking;
    }

    public void setPkBooking(int pkBooking) {
        this.pkBooking = pkBooking;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
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

    // getters
}