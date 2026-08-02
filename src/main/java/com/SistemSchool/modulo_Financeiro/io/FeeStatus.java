package com.SistemSchool.modulo_Financeiro.io;

public enum FeeStatus {

    ACTIVE("Ativa"),
    INACTIVE("Inativa");

    private final String description;

    FeeStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
