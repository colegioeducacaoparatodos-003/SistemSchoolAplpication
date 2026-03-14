package com.angola_argentina_portal.dto;

public class TravelAgencyTableDTO {

    private Long id;
    private String name;
    private String logoUrl;
    private String city;
    private String phone;
    private String website;

    public TravelAgencyTableDTO(Long id,
            String name,
            String logoUrl,
            String city,
            String phone,
            String website) {

        this.id = id;
        this.name = name;
        this.logoUrl = logoUrl;
        this.city = city;
        this.phone = phone;
        this.website = website;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    // getters
}