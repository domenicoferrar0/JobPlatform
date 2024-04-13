package com.ferraro.JobPlatform.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.ferraro.JobPlatform.enums.FormaGiuridica;
import com.ferraro.JobPlatform.enums.Settore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployerDTO {

    private String id;
    private String nomeAzienda;
    private String sedeLegale;
    private String codiceFiscale;
    private String partitaIva;
    private Integer numeroDipendenti;
    private Settore settore;
    private FormaGiuridica formaGiuridica;
    private String sitoWeb;
}
