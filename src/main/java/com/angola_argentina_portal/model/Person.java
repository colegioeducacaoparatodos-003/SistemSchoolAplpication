package com.angola_argentina_portal.model;

import java.time.LocalDateTime;

import org.primefaces.model.file.UploadedFile;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;


@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pkPerson;
    private String firstName;
    private String middleName;
    private String lastName;
    private String phone;
    private String address;
    private String city;
    private Double latitude;
    private Double longitude;
    private int fkUser;
    private String imagePerson;

    @Transient
    private UploadedFile imagePersonUtil;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String email;
    private String documentNumber;
    private String documentType;
    private boolean active = true;

    public Person() {
        super();
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public int getPkPerson() {
        return pkPerson;
    }

    public void setPkPerson(int pkPerson) {
        this.pkPerson = pkPerson;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public int getFkUser() {
        return fkUser;
    }

    public void setFkUser(int fkUser) {
        this.fkUser = fkUser;
    }

    public String getImagePerson() {
        return imagePerson;
    }

    public void setImagePerson(String imagePerson) {
        this.imagePerson = imagePerson;
    }

    public UploadedFile getImagePersonUtil() {
        return imagePersonUtil;
    }

    public void setImagePersonUtil(UploadedFile imagePersonUtil) {
        this.imagePersonUtil = imagePersonUtil;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // Métodos auxiliares
    public String getFullName() {
        if (middleName != null && !middleName.isEmpty()) {
            return firstName + " " + middleName + " " + lastName;
        }
        return firstName + " " + lastName;
    }

    public String getInitials() {
        String initials = "";
        if (firstName != null && !firstName.isEmpty()) {
            initials += firstName.charAt(0);
        }
        if (lastName != null && !lastName.isEmpty()) {
            initials += lastName.charAt(0);
        }
        return initials.toUpperCase();
    }

    @Override
    public String toString() {
        return "Person [pkPerson=" + pkPerson + ", firstName=" + firstName + ", middleName=" + middleName
                + ", lastName=" + lastName + ", phone=" + phone + ", address=" + address + ", city=" + city
                + ", latitude=" + latitude + ", longitude=" + longitude + ", fkUser=" + fkUser + ", imagePerson="
                + imagePerson + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", email=" + email
                + ", documentNumber=" + documentNumber + ", documentType=" + documentType + ", active=" + active + "]";
    }
}
