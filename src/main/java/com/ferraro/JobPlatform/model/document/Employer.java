package com.ferraro.JobPlatform.model.document;

import com.ferraro.JobPlatform.enums.FormaGiuridica;
import com.ferraro.JobPlatform.enums.Settore;
import com.ferraro.JobPlatform.model.Account;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "employers")
@Getter
@Setter
@NoArgsConstructor
public class Employer extends Account {


    @NotBlank
    @Size(min = 2, max = 100)
    private String nomeAzienda;

    @NotBlank
    @Size(min = 2, max = 100)
    private String sedeLegale;

    @Pattern(regexp = "^[0-9]{11}$")
    private String codiceFiscale;

    @NotBlank
    @Pattern(regexp = "^\\d{11}$", message = "Formato partita IVA non valido")
    private String partitaIva;

    @Positive
    private Integer numeroDipendenti;

    private Settore settore;

    private FormaGiuridica formaGiuridica;

    @Pattern(regexp = "^(http|https)://[a-zA-Z0-9-.]+\\.[a-zA-Z]{2,}(?:/\\S*)?$", message = "Formato sito web non valido")
    private String sitoWeb;
}
