package com.angola_argentina_portal.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "servico_consular")
public class ConsularServices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pkServico;

    private String serviceName;
    private String requirements;
    private String fees;
    private double price;
    private String details;
    private String status;
    private String availableDays;
    private boolean onlineBooking;

    // Getters e Setters
    // Construtores padrão e completo


    public ConsularServices() {
    }

    public ConsularServices(Long pkServico, String serviceName, String requirements, String fees, double price, String details, String status, String availableDays, boolean onlineBooking) {
        this.pkServico = pkServico;
        this.serviceName = serviceName;
        this.requirements = requirements;
        this.fees = fees;
        this.price = price;
        this.details = details;
        this.status = status;
        this.availableDays = availableDays;
        this.onlineBooking = onlineBooking;
    }

    public Long getPkServico() {
        return this.pkServico;
    }

    public void setPkServico(Long pkServico) {
        this.pkServico = pkServico;
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

    public ConsularServices pkServico(Long pkServico) {
        setPkServico(pkServico);
        return this;
    }

    public ConsularServices serviceName(String serviceName) {
        setServiceName(serviceName);
        return this;
    }

    public ConsularServices requirements(String requirements) {
        setRequirements(requirements);
        return this;
    }

    public ConsularServices fees(String fees) {
        setFees(fees);
        return this;
    }

    public ConsularServices price(double price) {
        setPrice(price);
        return this;
    }

    public ConsularServices details(String details) {
        setDetails(details);
        return this;
    }

    public ConsularServices status(String status) {
        setStatus(status);
        return this;
    }

    public ConsularServices availableDays(String availableDays) {
        setAvailableDays(availableDays);
        return this;
    }

    public ConsularServices onlineBooking(boolean onlineBooking) {
        setOnlineBooking(onlineBooking);
        return this;
    }
}
