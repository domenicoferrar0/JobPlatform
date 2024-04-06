package com.ferraro.JobPlatform.model.document;

import jakarta.validation.constraints.NotBlank;
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
    private String userId;

    @PastOrPresent
    private LocalDate creationDate;

    public ConfirmationToken(String userId) {
        this.token = UUID.randomUUID().toString();
        this.userId = userId;
        this.creationDate = LocalDate.now();
    }
}
