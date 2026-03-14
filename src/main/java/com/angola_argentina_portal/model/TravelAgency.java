package com.angola_argentina_portal.model;

public class TravelAgency {

    private Long id;
    private String name;
    private String logoUrl;
    private String city;
    private String address;
    private String phone;
    private String email;
    private String website;
    private String services;

    public TravelAgency() {
    }

    public TravelAgency(Long id, String name, String logoUrl, String city, String address, String phone, String email,
            String website, String services) {
        this.id = id;
        this.name = name;
        this.logoUrl = logoUrl;
        this.city = city;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.website = website;
        this.services = services;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    // getters and setters
}