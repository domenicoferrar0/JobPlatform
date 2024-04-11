package com.ferraro.JobPlatform.model.document;

import com.ferraro.JobPlatform.enums.Country;
import com.ferraro.JobPlatform.enums.Disponibilita;
import com.ferraro.JobPlatform.enums.Modalita;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "annunci")
@Getter
@Setter
@NoArgsConstructor
public class Annuncio {

    @Id
    private String id;

    @NotBlank
    @Indexed
    private String employerId;

    @NotBlank
    private String employerName;

    @NotNull
    @Size(min = 10, max = 30, message = "il titolo deve essere tra i 10 e 30 caratteri")
    private String title;

    @NotNull
    @Size(max = 500, message = "la descrizione può avere un massimo di 500 caratteri")
    private String description;

    @PastOrPresent(message = "la data di pubblicazione non può essere nel futuro")
    private LocalDate publicationDate;

    private LocalDate updateDate;

    @Size(max = 300, message = "l'indirizzo può avere un massimo di 300 caratteri")
    private String localita;

    private Modalita modalita;

    private Disponibilita disponibilita;

    @NotNull
    private Country country;



}
