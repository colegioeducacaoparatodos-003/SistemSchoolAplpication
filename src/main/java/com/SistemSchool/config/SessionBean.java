package com.SistemSchool.config;

import java.io.Serializable;

import com.SistemSchool.dto.UserDTO;
import com.SistemSchool.io.Perfil;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@Named
@SessionScoped
public class SessionBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private UserDTO.UserResponseDTO loggedUser;

    public UserDTO.UserResponseDTO getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(UserDTO.UserResponseDTO loggedUser) {
        this.loggedUser = loggedUser;
    }

    public void clear() {
        this.loggedUser = null;
    }

    public boolean isLoggedIn() {
        return loggedUser != null;
    }

    public boolean isAdmin() {
        return hasPerfil(Perfil.ADMIN);
    }

    public boolean isSecretary() {
        return hasPerfil(Perfil.SECRETARY);
    }

    public boolean isFinancial() {
        return hasPerfil(Perfil.FINANCIAL);
    }

    public boolean isPedagogical() {
        return hasPerfil(Perfil.PEDAGOGICAL);
    }

    private boolean hasPerfil(Perfil perfil) {
        return loggedUser != null && loggedUser.getPerfil() == perfil;
    }
}