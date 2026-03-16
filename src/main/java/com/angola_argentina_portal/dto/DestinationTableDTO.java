package com.angola_argentina_portal.dto;

public class DestinationTableDTO {

    private Long id;
    private String name;
    private String city;
    private String country;
    private String category;
    private String imageUrl;

    public DestinationTableDTO(Long id,
            String name,
            String city,
            String country,
            String category,
            String imageUrl) {

        this.id = id;
        this.name = name;
        this.city = city;
        this.country = country;
        this.category = category;
        this.imageUrl = imageUrl;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // getters
}