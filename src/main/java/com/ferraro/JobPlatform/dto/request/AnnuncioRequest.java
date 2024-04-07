package com.ferraro.JobPlatform.dto.request;

import com.ferraro.JobPlatform.enums.Country;
import com.ferraro.JobPlatform.enums.Disponibilita;
import com.ferraro.JobPlatform.enums.Modalita;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class AnnuncioRequest {

    @NotNull
    @Size(min = 10, max = 30, message = "title must be between 10 and 30 characters")
    private String title;

    @NotNull
    @Size(max = 500, message = "la descrizione può avere un massimo di 500 caratteri")
    private String description;

    @Size(max = 300, message = "l'indirizzo può avere un massimo di 300 caratteri")
    private String localita;

    private Modalita modalita;

    private Disponibilita disponibilita;

    @NotNull
    private Country country;
}
