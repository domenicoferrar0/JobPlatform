package com.ferraro.JobPlatform.enums;

public enum Disponibilità {

    FULL_TIME("Full-time"),
    PART_TIME("Part-time"),
    FLESSIBILIE("Flessibile");

    private final String nome;

    Disponibilità(String nome){
        this.nome = nome;
    }

    public String getNome(){
        return this.nome;
    }
}
