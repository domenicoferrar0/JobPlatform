package com.ferraro.JobPlatform.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserSignUpRequest {

    @Size(min = 2, max = 35, message = "Formato non valido, minimo 2 caratteri, massimo 35")
    @Pattern(regexp = "\\S(\\s*[a-zA-Z]+)*\\s*", message = "Formato nome non valido, rimuovi gli spazi in eccesso e i caratteri non autorizzati")
    @NotNull
    private String nome;

    @Size(min = 2, max = 35, message = "Formato non valido, minimo 2 caratteri, massimo 35")
    @Pattern(regexp = "\\S(\\s*[a-zA-Z]+)*\\s*", message = "Formato nome non valido, rimuovi gli spazi in eccesso e i caratteri non autorizzati")
    @NotNull
    private String cognome;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Pattern(regexp = "^(?=.*[!@#$%^&*(),.?\":{}|<>])(?=.*[A-Z]).{8,}$", message = "Formato password non valido, "
            + "deve contenere almeno 8 caratteri, una maiuscola ed un simbolo speciale")
    private String password;
}
