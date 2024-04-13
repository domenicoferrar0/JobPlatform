package com.ferraro.JobPlatform.dto.request;

import com.ferraro.JobPlatform.enums.Country;
import com.ferraro.JobPlatform.enums.Disponibilita;
import com.ferraro.JobPlatform.enums.Language;
import com.ferraro.JobPlatform.model.Formazione;
import com.ferraro.JobPlatform.model.WorkExperience;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class JobApplianceRequest {

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
    private String cf;

    @NotNull
    @Email
    private String email;

    @Pattern(regexp = "^\\d{3}-\\d{3}-\\d{4}$\n")
    private String tel;

    @NotNull
    @Past
    private LocalDate nascita;

    @NotNull
    private Country nazionalita;

    @Size(min = 2, max = 300)
    private String indirizzo;

    private Set<Language> languages;

    @Size(min = 1, max = 300)
    private String presentazione;

    @NotEmpty
    private Set<Formazione> formazione;

    private Set<WorkExperience> esperienzeLavorative;

    private boolean isCategorieProtette;

    private Disponibilita disponibilita;
}
