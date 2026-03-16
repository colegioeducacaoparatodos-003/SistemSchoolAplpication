package com.angola_argentina_portal.model;

public class Destination {

    private Long id;
    private String name;
    private String city;
    private String country;
    private String description;
    private String imageUrl;
    private String category;
    private String location;
    private String website;

    public Destination() {
    }

    public Destination(Long id, String name, String city, String country, String description, String imageUrl,
            String category, String location, String website) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.country = country;
        this.description = description;
        this.imageUrl = imageUrl;
        this.category = category;
        this.location = location;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    // getters and setters
}