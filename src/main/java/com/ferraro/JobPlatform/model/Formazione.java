package com.ferraro.JobPlatform.model;

import com.ferraro.JobPlatform.enums.TitoloStudio;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class Formazione {

    @NotNull
    private TitoloStudio titoloStudio;

    @NotNull
    @Size(min = 3, max = 100)
    private String istituto;

    @PastOrPresent
    private LocalDate annoInizio;

    private LocalDate annoFine;

}
