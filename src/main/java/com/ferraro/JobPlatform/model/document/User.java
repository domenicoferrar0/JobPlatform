package com.ferraro.JobPlatform.model.document;


import com.ferraro.JobPlatform.model.Account;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document(collection = "users")
@Getter
@Setter
@NoArgsConstructor
public class User extends Account {



    @Size(min = 2, max = 35, message = "Formato non valido, minimo 2 caratteri, massimo 35")
    @Pattern(regexp = "^[A-Z][a-z]*(\\s+[A-Z][a-z]*)*$", message = "Formato nome non valido, rimuovi gli spazi in eccesso e i caratteri non autorizzati")
    @NotNull
    private String nome;

    @Size(min = 2, max = 35, message = "Formato non valido, minimo 2 caratteri, massimo 35")
    @Pattern(regexp = "^[A-Z][a-z]*(\\s+[A-Z][a-z]*)*$", message = "Formato nome non valido, rimuovi gli spazi in eccesso e i caratteri non autorizzati")
    @NotNull
    private String cognome;

    private Set<String> favouriteAnnouncements;



}
