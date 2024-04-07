package com.ferraro.JobPlatform.enums;

public enum Modalita {

    IBRIDA("Ibrida"),
    IN_PRESENZA("In presenza"),

    DA_REMOTO("Da Remoto");

    private final String tipo;

    Modalita(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return this.tipo;
    }
}
