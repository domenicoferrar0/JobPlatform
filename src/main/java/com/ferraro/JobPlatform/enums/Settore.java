package com.ferraro.JobPlatform.enums;

public enum Settore {

    TECNOLOGIE_INFORMATICHE("Tecnologie Informatiche"),
    INGEGNERIA("Ingegneria"),
    SANITÀ("Sanità"),
    FINANZA("Finanza"),
    EDUCAZIONE("Educazione"),
    MARKETING("Marketing"),
    RISORSE_UMANE("Risorse Umane"),
    CONSULENZA("Consulenza"),
    OSPITALITÀ("Ospitalità"),
    MEDIA("Media"),
    LEGALE("Legale"),
    VENDITE("Vendite"),
    TRASPORTO("Trasporto"),
    NON_PROFIT("Non Profit"),
    ARTE_E_DIVERTIMENTO("Arte e Divertimento"),
    SCIENZA("Scienza"),
    PUBBLICITÀ("Pubblicità"),
    IMMOBILIARE("Immobiliare"),
    TELECOMUNICAZIONI("Telecomunicazioni"),
    MANIFATTURA("Manifattura"),
    AGRICOLTURA("Agricoltura"),
    ALTRO("Altro");

    private final String settore;

    Settore(String settore) {
        this.settore = settore;
    }

    public String getSettore() {
        return settore;
    }
}
