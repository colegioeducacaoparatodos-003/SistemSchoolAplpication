package com.SistemSchool.modulo_Financeiro.io;

public enum CashBoxStatus {

    OPEN("Aberto"),

    CLOSED("Fechado");

    private final String description;

    CashBoxStatus(String description) {

        this.description = description;

    }

    public String getDescription() {

        return description;

    }

}