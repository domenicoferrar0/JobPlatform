package com.ferraro.JobPlatform.model;

import com.ferraro.JobPlatform.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

@Getter
@Setter
@NoArgsConstructor
public class Account {

    @Id
    private String id;

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
