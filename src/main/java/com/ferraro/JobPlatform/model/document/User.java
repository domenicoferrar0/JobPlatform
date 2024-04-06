package com.ferraro.JobPlatform.model.document;


import com.ferraro.JobPlatform.enums.Role;
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
@ToString
public class User {

    @Id
    private String id;

    @Size(min = 2, max = 35, message = "Formato non valido, minimo 2 caratteri, massimo 35")
    @Pattern(regexp = "\\S(\\s*[a-zA-Z]+)*\\s*", message = "Formato nome non valido, rimuovi gli spazi in eccesso e i caratteri non autorizzati")
    @NotNull
    private String nome;

    @Size(min = 2, max = 35, message = "Formato non valido, minimo 2 caratteri, massimo 35")
    @Pattern(regexp = "\\S(\\s*[a-zA-Z]+)*\\s*", message = "Formato nome non valido, rimuovi gli spazi in eccesso e i caratteri non autorizzati")
    @NotNull
    private String cognome;

    @NotNull
    @Pattern(regexp = "^(?:[A-Z][AEIOU][AEIOUX]|[AEIOU]X{2}|"
            + "[B-DF-HJ-NP-TV-Z]{2}[A-Z]){2}(?:[\\dLMNP-V]{2}(?:[A-EHLMPR-T](?:[04LQ][1-9MNP-V]|[15MR][\\dLMNP-V]|[26NS][0-8LMNP-U])|[DHPS][37PT][0L]|[ACELMRT][37PT][01LM]|[AC-EHLMPR-T][26NS][9V])|(?:[02468LNQSU][048LQU]|[13579MPRTV][26NS])B[26NS][9V])(?:[A-MZ][1-9MNP-V][\\dLMNP-V]{2}|[A-M][0L](?:[1-9MNP-V][\\dLMNP-V]|[0L][1-9MNP-V]))[A-Z]$", message = "Formato CF non valido")
    @Indexed
    private String cf;

    @NotNull
    @Email
    @Indexed
    private String email;

    @NotNull
    @Pattern(regexp = "^(?=.*[!@#$%^&*(),.?\":{}|<>])(?=.*[A-Z]).{8,}$", message = "Formato password non valido, "
            + "deve contenere almeno 8 caratteri, una maiuscola ed un simbolo speciale")
    private String password;

    @NotNull
    private boolean isEnabled = false;

    @NotNull
    private Role role;
}
