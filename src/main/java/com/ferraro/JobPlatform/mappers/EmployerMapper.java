package com.ferraro.JobPlatform.mappers;

import com.ferraro.JobPlatform.dto.EmployerDTO;
import com.ferraro.JobPlatform.dto.request.EmployerSignUpRequest;
import com.ferraro.JobPlatform.model.document.Employer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "Spring")
public interface EmployerMapper {
    EmployerMapper INSTANCE = Mappers.getMapper(EmployerMapper.class);

    Employer requestToEmployer(EmployerSignUpRequest request);

    EmployerDTO employerToDto(Employer employer);
}
