package com.ferraro.JobPlatform.service;

import com.ferraro.JobPlatform.dto.JobApplianceDTO;
import com.ferraro.JobPlatform.dto.JobApplianceDTOSimple;
import com.ferraro.JobPlatform.dto.request.JobApplianceRequest;
import com.ferraro.JobPlatform.enums.Resource;
import com.ferraro.JobPlatform.exceptions.*;
import com.ferraro.JobPlatform.mappers.JobApplianceMapper;
import com.ferraro.JobPlatform.model.document.Annuncio;
import com.ferraro.JobPlatform.model.document.Employer;
import com.ferraro.JobPlatform.model.document.JobAppliance;
import com.ferraro.JobPlatform.model.document.User;
import com.ferraro.JobPlatform.repository.AnnuncioRepository;
import com.ferraro.JobPlatform.repository.JobApplianceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class JobApplianceService {
    @Autowired
    private JobApplianceRepository applianceRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private AnnuncioRepository annuncioRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private EmployerService employerService;

    @Autowired
    private JobApplianceMapper applianceMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;


    public JobApplianceDTO saveAppliance(JobApplianceRequest applianceRequest, String annuncioId, String authorization, MultipartFile file) {
        if (file.isEmpty() || !fileService.isValid(file)) {
            throw new FileHandlingException("Spiacente, inserire un file del formato e delle dimensioni richiesti.");
        }

        Annuncio annuncio = annuncioRepository.findById(annuncioId)
                .orElseThrow(() -> new ResourceNotFoundException(Resource.ANNUNCIO, annuncioId));
        User user = userService.extractUser(authorization);
        if(applianceRepository.existsByIdAnnuncioAndUserId(annuncioId, user.getId())){
            throw new DuplicateApplianceException();
        }
        JobAppliance appliance = applianceMapper.requestToAppliance(applianceRequest);
        appliance.setIdAnnuncio(annuncioId);
        appliance.setTitleAnnuncio(annuncio.getTitle());
        appliance.setUserId(user.getId());
        appliance.setEmployerId(annuncio.getEmployerId());
        String cvPath = fileService.savePdf(file);
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
                .orElseThrow(() -> new ResourceNotFoundException(Resource.APPLIANCE, applianceId));
        User user = userService.extractUser(authorization);
        if (!user.getId().equals(appliance.getUserId())) {
            throw new UsersDontMatchException();
        }
        if(mongoTemplate.remove(appliance).getDeletedCount()<=0){
            return false;
        }

        return fileService.delete(appliance.getCvPath());
    }

    public org.springframework.core.io.Resource findFileByApplianceId(String authorization, String applianceId) {
        JobAppliance appliance = applianceRepository.findById(applianceId)
                .orElseThrow(() -> new ResourceNotFoundException(Resource.APPLIANCE, applianceId));
        Employer employer = employerService.extractEmployer(authorization);
        if(!appliance.getEmployerId().equals(employer.getId())){
            throw new UsersDontMatchException();
        }
        byte[] file = fileService.getFile(appliance.getCvPath());
        return new ByteArrayResource(file);
    }

    public Page<JobApplianceDTOSimple> findAllAppliancesByAnnuncio(String authorization, String idAnnuncio, int page, int pageSize) {
        Annuncio annuncio = annuncioRepository.findById(idAnnuncio)
                .orElseThrow(() -> new ResourceNotFoundException(Resource.ANNUNCIO, idAnnuncio));
        Employer employer = employerService.extractEmployer(authorization);
        if(!annuncio.getEmployerId().equals(employer.getId())){
            throw new UsersDontMatchException();
        }
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<JobAppliance> appliances = applianceRepository.findAllByAnnuncioPaginated(idAnnuncio, pageable);
        return appliances.map(applianceMapper::applianceToDtoSimple);
    }

    public JobApplianceDTO findApplianceById(String authorization, String applianceId) {
        JobApplianceDTO appliance = applianceRepository.findById(applianceId)
                .map(applianceMapper::applianceToDto)
                .orElseThrow(() -> new ResourceNotFoundException(Resource.APPLIANCE, applianceId));
        Employer employer = employerService.extractEmployer(authorization);
        if(!appliance.getEmployerId().equals(employer.getId())){
            throw new UsersDontMatchException();
        }
        return appliance;

    }

    public Page<JobApplianceDTOSimple> findAppliancesByUser(String authorization, int currentPage, int pageSize) {
        Pageable pageable = PageRequest.of(currentPage, pageSize);
        User user = userService.extractUser(authorization);

        return applianceRepository
                .findByUserPaginated(user.getId(),pageable)
                .map(applianceMapper::applianceToDtoSimple);
    }
}
