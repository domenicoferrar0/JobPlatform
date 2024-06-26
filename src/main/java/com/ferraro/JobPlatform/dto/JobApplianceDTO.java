package com.ferraro.JobPlatform.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ferraro.JobPlatform.enums.Country;
import com.ferraro.JobPlatform.enums.Disponibilita;
import com.ferraro.JobPlatform.enums.Language;
import com.ferraro.JobPlatform.model.Formazione;
import com.ferraro.JobPlatform.model.WorkExperience;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class JobApplianceDTO {

    private String id;
    private String idAnnuncio;
    private String userId;
    private String employerId;
    private String titleAnnuncio;
    private String nome;
    private String cognome;
    private String cf;
    private String email;
    private String tel;
    private LocalDate nascita;
    private Country nazionalita;
    private String indirizzo;
    private Set<Language> languages;
    private String presentazione;
    private Set<Formazione> formazione;
    private Set<WorkExperience> esperienzeLavorative;
    private boolean isCategorieProtette;
    private Disponibilita disponibilita;
    private String cvPath;
}
