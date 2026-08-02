package com.SistemSchool.io;

public enum Gender {

    MALE("Masculino"),

    FEMALE("Feminino"),

    OTHER("Outro"),

    PREFER_NOT_TO_SAY("Prefiro não informar");

    private final String descricao;

    Gender(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

}