package com.ferraro.JobPlatform.enums;

public enum TitoloStudio {


    LICENZA_MEDIA("Licenza Media"),

    DIPLOMA_DI_MATURITA("Diploma di Maturità"),

    LAUREA_TRIENNALE("Laurea Triennale"),

    LAUREA_MAGISTRALE("Laurea Magistrale"),

    DOTTORATO_DI_RICERCA("Dottorato di Ricerca"),

    LAUREA_SPECIALISTICA("Laurea Specialistica"),

    LAUREA_MAGISTRALE_A_CICLO_UNICO("Laurea Magistrale a Ciclo Unico"),

    CORSO_DI_LAUREA("Corso di Laurea"),

    CORSO_DI_LAUREA_MAGISTRALE("Corso di Laurea Magistrale"),

    DIPLOMA_ACCADEMICO_DI_PRIMO_LIVELLO("Diploma Accademico di Primo Livello"),

    DIPLOMA_ACCADEMICO_DI_SECONDO_LIVELLO("Diploma Accademico di Secondo Livello");

    private final String title;

    TitoloStudio(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }


}




