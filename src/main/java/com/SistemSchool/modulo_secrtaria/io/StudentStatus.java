package com.SistemSchool.modulo_secrtaria.io;

public enum StudentStatus {

    ACTIVE("Ativo"),

    INACTIVE("Inativo"),

    GRADUATED("Graduado"),

    TRANSFERED("Transferido"),

    EXPELLED("Expulso"),

    DECEASED("Falecido");

    private final String descricao;

    StudentStatus(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

}