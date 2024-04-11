package com.ferraro.JobPlatform.service;

import com.ferraro.JobPlatform.dto.JobApplianceDTO;
import com.ferraro.JobPlatform.dto.request.JobApplianceRequest;
import com.ferraro.JobPlatform.enums.Resource;
import com.ferraro.JobPlatform.exceptions.ApplianceSavingFailureException;
import com.ferraro.JobPlatform.exceptions.ResourceNotFoundException;
import com.ferraro.JobPlatform.exceptions.UsersDontMatchException;
import com.ferraro.JobPlatform.mappers.JobApplianceMapper;
import com.ferraro.JobPlatform.model.document.Annuncio;
import com.ferraro.JobPlatform.model.document.JobAppliance;
import com.ferraro.JobPlatform.model.document.User;
import com.ferraro.JobPlatform.repository.AnnuncioRepository;
import com.ferraro.JobPlatform.repository.JobApplianceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobApplianceService {
    @Autowired
    private JobApplianceRepository applianceRepository;

    @Autowired
    private AnnuncioRepository annuncioRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private JobApplianceMapper applianceMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;


    public JobApplianceDTO saveAppliance(JobApplianceRequest applianceRequest, String annuncioId, String authorization) {
        Annuncio annuncio = annuncioRepository.findById(annuncioId)
                .orElseThrow(() -> new ResourceNotFoundException(Resource.ANNUNCIO, annuncioId));
        User user = userService.extractUser(authorization);
        JobAppliance appliance = applianceMapper.requestToAppliance(applianceRequest);
        appliance.setIdAnnuncio(annuncioId);
        appliance.setTitleAnnuncio(annuncio.getTitle());
        appliance.setUserId(user.getId());
        String cvPath = fileService.savePdf(applianceRequest.getFile(), appliance.getCf());
        JobApplianceDTO applianceDTO;
        try {
            applianceDTO = applianceMapper.applianceToDto(applianceRepository.save(appliance));
        } catch (Exception ex) {
            fileService.delete(cvPath);
            throw new ApplianceSavingFailureException();
        }
        return applianceDTO;
    }

    //TODO
    public boolean deleteAppliance(String applianceId, String authorization) {
        JobAppliance appliance = applianceRepository.findById(applianceId)
                .orElseThrow(() -> new ResourceNotFoundException(Resource.APPLIANCE));
        User user = userService.extractUser(authorization);
        if (!user.getId().equals(appliance.getUserId())) {
            throw new UsersDontMatchException();
        }
        return fileService.delete(appliance.getCvPath());
    }

}
