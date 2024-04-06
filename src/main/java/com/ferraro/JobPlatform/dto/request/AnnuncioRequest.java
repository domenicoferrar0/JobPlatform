package com.ferraro.JobPlatform.dto.request;

import com.ferraro.JobPlatform.enums.Country;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;

public class AnnuncioRequest {

    @NotNull
    @Size(min = 10, max = 30, message = "title must be between 10 and 30 characters")
    private String title;

    @NotNull
    @Size(max = 500, message = "the description allows a maximum of 500 characters")
    private String description;

    @PastOrPresent(message = "publicaton date cannot be in the future")
    private String publicationDate;

    @NotNull
    private Country country;
}
