package com.angola_argentina_portal.dto;

public class HotelTableDTO {

    private Long id;
    private String name;
    private String city;
    private int stars;
    private String phone;
    private String imageUrl;
    private String address;
    private String email;
    private String website;
    private String mapLocation;

    public HotelTableDTO(Long id,
            String name,
            String city,
            int stars,
            String phone,
            String imageUrl,
            String address,
            String email,
            String website,
            String mapLocation) {

        this.id = id;
        this.name = name;
        this.city = city;
        this.stars = stars;
        this.phone = phone;
        this.imageUrl = imageUrl;
        this.address = address;
        this.email = email;
        this.website = website;
        this.mapLocation = mapLocation;
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

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getMapLocation() {
        return mapLocation;
    }

    public void setMapLocation(String mapLocation) {
        this.mapLocation = mapLocation;
    }

    // getters
}