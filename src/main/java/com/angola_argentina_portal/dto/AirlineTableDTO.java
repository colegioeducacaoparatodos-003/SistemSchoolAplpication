package com.angola_argentina_portal.dto;

public class AirlineTableDTO {

    private Long id;
    private String name;
    private String logoUrl;
    private String country;
    private String website;
    private boolean directFlights;

    public AirlineTableDTO(Long id, String name, String logoUrl,
            String country, String website, boolean directFlights) {

        this.id = id;
        this.name = name;
        this.logoUrl = logoUrl;
        this.country = country;
        this.website = website;
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

    public boolean isDirectFlights() {
        return directFlights;
    }

    public void setDirectFlights(boolean directFlights) {
        this.directFlights = directFlights;
    }

    // getters
}