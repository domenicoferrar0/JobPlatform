package com.ferraro.JobPlatform.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class JobApplianceDTOSimple {
    private String nome;
    private String cognome;
    private String id;
}
