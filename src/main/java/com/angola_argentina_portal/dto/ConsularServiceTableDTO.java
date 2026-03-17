package com.angola_argentina_portal.dto;

public class ConsularServiceTableDTO {

    private int pkService;
    private String name;
    private String requirements;
    private String fees;
    private Double price;
    private String details;
    private String status;
    private String availableDays;
    private Boolean onlineBooking;

    public ConsularServiceTableDTO(
            int pkService,
            String name,
            String requirements,
            String fees,
            Double price,
            String details,
            String status,
            String availableDays,
            Boolean onlineBooking) {

        this.pkService = pkService;
        this.name = name;
        this.requirements = requirements;
        this.fees = fees;
        this.price = price;
        this.details = details;
        this.status = status;
        this.availableDays = availableDays;
        this.onlineBooking = onlineBooking;
    }

    public int getPkService() {
        return pkService;
    }

    public void setPkService(int pkService) {
        this.pkService = pkService;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAvailableDays() {
        return availableDays;
    }

    public void setAvailableDays(String availableDays) {
        this.availableDays = availableDays;
    }

    public Boolean getOnlineBooking() {
        return onlineBooking;
    }

    public void setOnlineBooking(Boolean onlineBooking) {
        this.onlineBooking = onlineBooking;
    }

    // getters
}