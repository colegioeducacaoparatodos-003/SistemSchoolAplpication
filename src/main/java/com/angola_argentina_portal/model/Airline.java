package com.angola_argentina_portal.model;

public class Airline {

    private Long id;
    private String name;
    private String logoUrl;
    private String country;
    private String website;
    private String phone;
    private String email;
    private String destinations;
    private boolean directFlights;

    public Airline() {
    }

    public Airline(Long id, String name, String logoUrl, String country, String website, String phone, String email,
            String destinations, boolean directFlights) {
        this.id = id;
        this.name = name;
        this.logoUrl = logoUrl;
        this.country = country;
        this.website = website;
        this.phone = phone;
        this.email = email;
        this.destinations = destinations;
        this.directFlights = directFlights;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
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

    public String getDestinations() {
        return destinations;
    }

    public void setDestinations(String destinations) {
        this.destinations = destinations;
    }

    public boolean isDirectFlights() {
        return directFlights;
    }

    public void setDirectFlights(boolean directFlights) {
        this.directFlights = directFlights;
    }

    // getters and setters
}
