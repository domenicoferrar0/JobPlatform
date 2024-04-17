package com.ferraro.JobPlatform.dto.request;

import com.ferraro.JobPlatform.enums.FormaGiuridica;
import com.ferraro.JobPlatform.enums.Settore;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmployerSignUpRequest {

    @NotNull
    @Email
    private String email;

    @NotNull
    @Pattern(regexp = "^(?=.*[!@#$%^&*(),.?\":{}|<>])(?=.*[A-Z]).{8,}$", message = "Formato password non valido, "
            + "deve contenere almeno 8 caratteri, una maiuscola ed un simbolo speciale")
    private String password;

    @NotBlank
    @Size(min = 2, max = 100)
    private String nomeAzienda;

    @NotBlank
    @Size(min = 2, max = 100)
    private String sedeLegale;

    @Pattern(regexp = "^[0-9]{11}$")
    private String codiceFiscale;

    @NotBlank(message ="PIVA BLANk")
    @Pattern(regexp = "^\\d{11}$", message = "Formato partita IVA non valido")
    private String partitaIva;

    @Positive
    private Integer numeroDipendenti;

    @NotNull
    private Settore settore;

    private FormaGiuridica formaGiuridica;
}
