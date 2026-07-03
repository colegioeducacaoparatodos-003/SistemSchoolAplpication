package com.SistemSchool.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "user")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pkUser;

    private int fkPerson;

    private int fkUserType;

    private int fkCustomer;

    private String password;

    private String email;

    private boolean active;

    private String salt;
    private String deviceToken;
    private LocalDateTime userCreationDate;
    private LocalDateTime userModificationDate;

    public User() {
        super();
    }

    // Getters and Setters

    public int getPkUser() {
        return pkUser;
    }

    public void setPkUser(int pkUser) {
        this.pkUser = pkUser;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSalt() {
        return salt;
    }

    public int getFkCustomer() { return fkCustomer; }
    public void setFkCustomer(int fkCustomer) { this.fkCustomer = fkCustomer; }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    // Getters and Setters for user_creation_date and user_modification_date
    @PrePersist
    protected void onCreate() {
        this.userCreationDate = LocalDateTime.now();
        this.userModificationDate = LocalDateTime.now();
    }

    // Update modification date on update
    @PreUpdate
    protected void onUpdate() {
        this.userModificationDate = LocalDateTime.now();
    }

    public LocalDateTime getUserCreationDate() {
        return userCreationDate;
    }

    public void setUserCreationDate(LocalDateTime userCreationDate) {
        this.userCreationDate = userCreationDate;
    }

    public LocalDateTime getUserModificationDate() {
        return userModificationDate;
    }

    public void setUserModificationDate(LocalDateTime userModificationDate) {
        this.userModificationDate = userModificationDate;
    }

    public int getFkPerson() {
        return fkPerson;
    }

    public void setFkPerson(int fkPerson) {
        this.fkPerson = fkPerson;
    }

    public int getFkUserType() {
        return fkUserType;
    }

    public void setFkUserType(int fkUserType) {
        this.fkUserType = fkUserType;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    @Override
    public String toString() {
        return "User [pkUser=" + pkUser + ", fkPerson=" + fkPerson + ", fkUserType=" + fkUserType + ", password="
                + password + ", email=" + email + ", active=" + active + ", salt=" + salt + ", deviceToken="
                + deviceToken + ", userCreationDate=" + userCreationDate + ", userModificationDate="
                + userModificationDate + "]";
    }

}
