package com.angola_argentina_portal.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;

import org.apache.commons.lang3.builder.EqualsBuilder;

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
    private String imagePerson;
    private String password;
    private String confPasswor;
    private String email;
    private boolean active = true;

    public Person() {
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfPasswor() {
        return this.confPasswor;
    }

    public void setConfPasswor(String confPasswor) {
        this.confPasswor = confPasswor;
    }

    public Person(int pkPerson, String firstName, String middleName, String lastName, String phone, String imagePerson, String password, String confPasswor, String email, boolean active) {
        this.pkPerson = pkPerson;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.phone = phone;
        this.imagePerson = imagePerson;
        this.password = password;
        this.confPasswor = confPasswor;
        this.email = email;
        this.active = active;
    }

    public Person password(String password) {
        setPassword(password);
        return this;
    }

    public Person confPasswor(String confPasswor) {
        setConfPasswor(confPasswor);
        return this;
    }


    public int getPkPerson() {
        return this.pkPerson;
    }

    public void setPkPerson(int pkPerson) {
        this.pkPerson = pkPerson;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return this.middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImagePerson() {
        return this.imagePerson;
    }

    public void setImagePerson(String imagePerson) {
        this.imagePerson = imagePerson;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return this.active;
    }

    public boolean getActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Person pkPerson(int pkPerson) {
        setPkPerson(pkPerson);
        return this;
    }

    public Person firstName(String firstName) {
        setFirstName(firstName);
        return this;
    }

    public Person middleName(String middleName) {
        setMiddleName(middleName);
        return this;
    }

    public Person lastName(String lastName) {
        setLastName(lastName);
        return this;
    }

    public Person phone(String phone) {
        setPhone(phone);
        return this;
    }

    public Person imagePerson(String imagePerson) {
        setImagePerson(imagePerson);
        return this;
    }

    public Person email(String email) {
        setEmail(email);
        return this;
    }

    public Person active(boolean active) {
        setActive(active);
        return this;
    }

    @Override
    public boolean equals(Object o) {
      return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pkPerson, firstName, middleName, lastName, phone, imagePerson, email, active);
    }

    @Override
    public String toString() {
        return "{" +
            " pkPerson='" + getPkPerson() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", middleName='" + getMiddleName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", phone='" + getPhone() + "'" +
            ", imagePerson='" + getImagePerson() + "'" +
            ", email='" + getEmail() + "'" +
            ", active='" + isActive() + "'" +
            "}";
    }
    


}
