package com.ferraro.JobPlatform.service;

import com.ferraro.JobPlatform.dto.JobApplianceDTO;
import com.ferraro.JobPlatform.dto.request.JobApplianceRequest;
import com.ferraro.JobPlatform.enums.Resource;
import com.ferraro.JobPlatform.exceptions.ApplianceSavingFailureException;
import com.ferraro.JobPlatform.exceptions.FileHandlingException;
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
import org.springframework.web.multipart.MultipartFile;

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

    //TODO VERIFICARE CHE CON ELIMINA<IONE ANNUNCI VENGA ELIMINATO IL FILE, DA IMPLEMENTARE LOGICA PER EVITARE CHE STESSO UTENTE FACCIA PIU APPLIANCE

    public JobApplianceDTO saveAppliance(JobApplianceRequest applianceRequest, String annuncioId, String authorization, MultipartFile file) {
        if (!fileService.isValid(file)) {
            throw new FileHandlingException("Spiacente, il file non è conforme alla dimensione e al formato richiesti.");
        }
        Annuncio annuncio = annuncioRepository.findById(annuncioId)
                .orElseThrow(() -> new ResourceNotFoundException(Resource.ANNUNCIO, annuncioId));
        User user = userService.extractUser(authorization);
        //TODO CODICE CHE VEDE SE ESISTONO GIA ALTRE APPLIANCE CON STESSO ANNUNCIO ID E USER ID
        JobAppliance appliance = applianceMapper.requestToAppliance(applianceRequest);
        appliance.setIdAnnuncio(annuncioId);
        appliance.setTitleAnnuncio(annuncio.getTitle());
        appliance.setUserId(user.getId());
        String cvPath = fileService.savePdf(file, appliance.getCf());
        appliance.setCvPath(cvPath);
        JobApplianceDTO applianceDTO;
        try {
            applianceDTO = applianceMapper.applianceToDto(applianceRepository.save(appliance));
        } catch (Exception ex) {
            fileService.delete(cvPath);
            throw new ApplianceSavingFailureException();
        }
        return applianceDTO;
    }


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
