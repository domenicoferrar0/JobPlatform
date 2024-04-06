package com.ferraro.JobPlatform.model;

import com.ferraro.JobPlatform.enums.Settore;
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
public class WorkExperience {

    @NotNull
    private Settore settore;

    @Size(min = 1, max = 100)
    private String posizione;

    @PastOrPresent
    private LocalDate annoInizio;

    private LocalDate annoFine;
}
