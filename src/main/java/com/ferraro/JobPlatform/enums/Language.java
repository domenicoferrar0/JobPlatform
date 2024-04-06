package com.ferraro.JobPlatform.enums;

public enum Language {
    ENGLISH("English"),
    SPANISH("Spanish"),
    FRENCH("French"),
    GERMAN("German"),
    CHINESE("Chinese"),
    JAPANESE("Japanese"),
    ITALIAN("Italian"),
    PORTUGUESE("Portuguese"),
    RUSSIAN("Russian"),
    ARABIC("Arabic"),
    HINDI("Hindi"),
    BENGALI("Bengali"),
    URDU("Urdu"),
    DUTCH("Dutch"),
    SWEDISH("Swedish"),
    DANISH("Danish"),
    NORWEGIAN("Norwegian"),
    FINNISH("Finnish"),
    KOREAN("Korean"),
    TURKISH("Turkish"),
    POLISH("Polish"),
    GREEK("Greek"),
    HUNGARIAN("Hungarian"),
    CZECH("Czech"),
    THAI("Thai"),
    INDONESIAN("Indonesian"),
    MALAY("Malay"),
    VIETNAMESE("Vietnamese"),
    FILIPINO("Filipino"),
    SWAHILI("Swahili"),
    OTHER("Other");

    private final String name;

    Language(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


}

