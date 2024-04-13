package com.ferraro.JobPlatform.mappers;

import com.ferraro.JobPlatform.dto.JobApplianceDTO;
import com.ferraro.JobPlatform.dto.JobApplianceDTOSimple;
import com.ferraro.JobPlatform.dto.request.JobApplianceRequest;
import com.ferraro.JobPlatform.model.document.JobAppliance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "Spring")
public interface JobApplianceMapper {
    JobApplianceMapper INSTANCE = Mappers.getMapper(JobApplianceMapper.class);

    JobAppliance requestToAppliance(JobApplianceRequest request);

    JobApplianceDTO applianceToDto(JobAppliance appliance);


    JobApplianceDTOSimple applianceToDtoSimple(JobAppliance appliance);




}
