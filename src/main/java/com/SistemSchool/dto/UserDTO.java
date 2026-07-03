package com.SistemSchool.dto;

import java.time.LocalDateTime;
import java.util.Date;

public class UserDTO {

    // Para criação (sem senha)
    public static class CreateUserDTO {
        private int fkPerson;
        private int fkUserType;
        private int fkCustomer;
        private String email;
        private String password;
        private boolean active;
        private String deviceToken;

        // Getters e Setters
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

        public int getFkCustomer() {
            return fkCustomer;
        }

        public void setFkCustomer(int fkCustomer) {
            this.fkCustomer = fkCustomer;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
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

        public String getDeviceToken() {
            return deviceToken;
        }

        public void setDeviceToken(String deviceToken) {
            this.deviceToken = deviceToken;
        }
    }

    // Para atualização
    public static class UpdateUserDTO {
        private int pkUser;
        private String email;
        private boolean active;
        private String deviceToken;
        private Integer fkUserType;
        private String password;
        private String salt;

        // Getters e Setters
        public int getPkUser() {
            return pkUser;
        }

        public void setPkUser(int pkUser) {
            this.pkUser = pkUser;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public String getDeviceToken() {
            return deviceToken;
        }

        public void setDeviceToken(String deviceToken) {
            this.deviceToken = deviceToken;
        }

        public Integer getFkUserType() {
            return fkUserType;
        }

        public void setFkUserType(Integer fkUserType) {
            this.fkUserType = fkUserType;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getSalt() {
            return salt;
        }

        public void setSalt(String salt) {
            this.salt = salt;
        }
    }

    // Para resposta (sem dados sensíveis)
    public static class UserResponseDTO {
        private int pkUser;
        private int fkPerson;
        private int fkCustomer;
        private int fkUserType;
        private String email;
        private boolean active;
        private String deviceToken;
        private LocalDateTime userCreationDate;
        private LocalDateTime userModificationDate;

        // Getters e Setters
        public int getPkUser() {
            return pkUser;
        }

        public void setPkUser(int pkUser) {
            this.pkUser = pkUser;
        }

        public int getFkPerson() {
            return fkPerson;
        }

        public int getFkCustomer() {
            return fkCustomer;
        }

        public void setFkCustomer(int fkCustomer) {
            this.fkCustomer = fkCustomer;
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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public String getDeviceToken() {
            return deviceToken;
        }

        public void setDeviceToken(String deviceToken) {
            this.deviceToken = deviceToken;
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

    }

    // Para autenticação
    public static class LoginDTO {
        private String email;
        private String password;

        // Getters e Setters
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}