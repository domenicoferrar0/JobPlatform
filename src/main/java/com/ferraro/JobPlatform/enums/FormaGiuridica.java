package com.ferraro.JobPlatform.enums;

public enum FormaGiuridica {

    SRL("Srl"),
    SPA("Spa"),
    SAS("Sas"),
    SA("SA"),
    LTD("Ltd"),
    LLC("LLC"),
    INC("Inc"),
    GmbH("GmbH"),
    BV("BV"),
    AB("AB"),
    AS("AS"),
    AG("AG"),
    LLC_LLP("LLC/LLP"),
    LLP("LLP"),
    PLC("PLC"),
    PTY_LTD("Pty Ltd"),
    PLC_LTD("PLC Ltd"),
    LP("LP"),
    LLP_LTD("LLP Ltd"),
    LLP_PLC("LLP Plc"),
    OTHER("Other");

    private final String nome;

    private FormaGiuridica(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return this.nome;
    }
}
