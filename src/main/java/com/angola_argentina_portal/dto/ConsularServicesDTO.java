package com.angola_argentina_portal.dto;

import java.util.Objects;

public class ConsularServicesDTO {
      private String serviceName;
    private String requirements;
    private String fees;
    private double price;
    private String details;
    private String status;
    private String availableDays;
    private boolean onlineBooking;

    public ConsularServicesDTO(String serviceName, String requirements, String fees, double price,
                           String details, String status, String availableDays, boolean onlineBooking) {
        this.serviceName = serviceName;
        this.requirements = requirements;
        this.fees = fees;
        this.price = price;
        this.details = details;
        this.status = status;
        this.availableDays = availableDays;
        this.onlineBooking = onlineBooking;
    }


    public String getServiceName() {
        return this.serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getRequirements() {
        return this.requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getFees() {
        return this.fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDetails() {
        return this.details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAvailableDays() {
        return this.availableDays;
    }

    public void setAvailableDays(String availableDays) {
        this.availableDays = availableDays;
    }

    public boolean isOnlineBooking() {
        return this.onlineBooking;
    }

    public boolean getOnlineBooking() {
        return this.onlineBooking;
    }

    public void setOnlineBooking(boolean onlineBooking) {
        this.onlineBooking = onlineBooking;
    }
    
}
