package com.ferraro.JobPlatform.model.document;

import com.ferraro.JobPlatform.enums.Country;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "annunci")
@Getter
@Setter
@NoArgsConstructor
public class Annuncio {

    @Id
    private String id;

    @NotNull
    @Size(min = 10, max = 30, message = "title must be between 10 and 30 characters")
    private String title;

    @NotNull
    @Size(max = 500, message = "the description allows a maximum of 500 characters")
    private String description;

    @PastOrPresent(message = "publicaton date cannot be in the future")
    private String publicationDate;

    @NotNull
    private Country country;



}
