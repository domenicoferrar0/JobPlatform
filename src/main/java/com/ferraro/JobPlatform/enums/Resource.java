package com.ferraro.JobPlatform.enums;

public enum Resource {
    ANNUNCIO("Annuncio"),
    USER("Utente"),
    EMPLOYER("Azienda"),
    APPLIANCE("Richiesta"),
    TOKEN("Token");

    private String nome;
    Resource(String nome){
        this.nome = nome;
    }
    public String getNome(){
        return this.nome;
    }
}
