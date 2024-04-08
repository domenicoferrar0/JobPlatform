package com.ferraro.JobPlatform.model.document;

import com.ferraro.JobPlatform.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDate;
import java.util.UUID;

@Getter
public class ConfirmationToken {
    @NotBlank
    private String id;

    @NotBlank
    private String token;

    @NotBlank
    @Indexed
    private String accountId;

    @PastOrPresent
    private LocalDate creationDate;

    @NotNull
    private Role accountRole;

    public ConfirmationToken(String accountId, Role accountRole) {
        this.token = UUID.randomUUID().toString();
        this.accountId = accountId;
        this.creationDate = LocalDate.now();
        this.accountRole = accountRole;
    }


}
