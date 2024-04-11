package com.ferraro.JobPlatform.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ferraro.JobPlatform.enums.Country;
import com.ferraro.JobPlatform.enums.Disponibilita;
import com.ferraro.JobPlatform.enums.Modalita;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class AnnuncioDTO {

    private String id;
    private String employerName;
    private String employerId;
    private String title;
    private String description;
    private LocalDate publicationDate;
    private LocalDate updateDate;
    private String localita;
    private Modalita modalita;
    private Disponibilita disponibilita;
    private Country country;

}
