package com.ferraro.JobPlatform.model.document;


import com.ferraro.JobPlatform.enums.Role;
import com.ferraro.JobPlatform.model.Account;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Getter
@Setter
@NoArgsConstructor
public class User extends Account {



    @Size(min = 2, max = 35, message = "Formato non valido, minimo 2 caratteri, massimo 35")
    @Pattern(regexp = "\\S(\\s*[a-zA-Z]+)*\\s*", message = "Formato nome non valido, rimuovi gli spazi in eccesso e i caratteri non autorizzati")
    @NotNull
    private String nome;

    @Size(min = 2, max = 35, message = "Formato non valido, minimo 2 caratteri, massimo 35")
    @Pattern(regexp = "\\S(\\s*[a-zA-Z]+)*\\s*", message = "Formato nome non valido, rimuovi gli spazi in eccesso e i caratteri non autorizzati")
    @NotNull
    private String cognome;



}
