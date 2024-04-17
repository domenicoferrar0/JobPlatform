package com.ferraro.JobPlatform.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserSignUpRequest {

    @Size(min = 2, max = 35, message = "Formato non valido, minimo 2 caratteri, massimo 35")
    @Pattern(regexp = "^[A-Z][a-z]*(\\s+[A-Z][a-z]*)*$", message = "Formato nome non valido, rimuovi gli spazi in eccesso e i caratteri non autorizzati")
    @NotBlank(message = "Inserisci un nome")
    private String nome;

    @Size(min = 2, max = 35, message = "Formato non valido, minimo 2 caratteri, massimo 35")
    @Pattern(regexp = "^[A-Z][a-z]*(\\s+[A-Z][a-z]*)*$", message = "Formato nome non valido, rimuovi gli spazi in eccesso e i caratteri non autorizzati")
    @NotBlank(message = "Inserisci un cognome")
    private String cognome;

    @NotBlank(message = "Inserisci un'email")
    @Email
    private String email;

    @NotBlank(message = "Inserisci una password")
    @Pattern(regexp = "^(?=.*[!@#$%^&*(),.?\":{}|<>])(?=.*[A-Z]).{8,}$", message = "Formato password non valido, "
            + "deve contenere almeno 8 caratteri, una maiuscola ed un simbolo speciale")
    private String password;
}
