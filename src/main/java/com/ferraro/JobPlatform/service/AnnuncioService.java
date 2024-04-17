package com.ferraro.JobPlatform.service;

import com.ferraro.JobPlatform.dto.AnnuncioDTO;
import com.ferraro.JobPlatform.dto.request.AnnuncioRequest;
import com.ferraro.JobPlatform.enums.Resource;
import com.ferraro.JobPlatform.exceptions.ResourceNotFoundException;
import com.ferraro.JobPlatform.exceptions.UsersDontMatchException;
import com.ferraro.JobPlatform.mappers.AnnuncioMapper;
import com.ferraro.JobPlatform.model.document.Annuncio;
import com.ferraro.JobPlatform.model.document.Employer;
import com.ferraro.JobPlatform.model.document.JobAppliance;
import com.ferraro.JobPlatform.repository.AnnuncioRepository;
import com.ferraro.JobPlatform.repository.JobApplianceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class AnnuncioService {
    @Autowired
    private AnnuncioRepository annuncioRepository;

    @Autowired
    private JobApplianceRepository applianceRepository;

    @Autowired
    private AnnuncioMapper annuncioMapper;

    @Autowired
    private EmployerService employerService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private FileService fileService;



    public AnnuncioDTO saveAnnuncio(String authorization, AnnuncioRequest requestBody) {
        Employer employer = employerService.extractEmployer(authorization);
        Annuncio annuncio = annuncioMapper.requestToAnnuncio(requestBody);
        annuncio.setPublicationDate(LocalDate.now());
        annuncio.setEmployerId(employer.getId());
        annuncio.setEmployerName(employer.getNomeAzienda());
        return annuncioMapper.annuncioToDTO(annuncioRepository.save(annuncio));
    }

    @Transactional
    public boolean deleteAnnuncio(String idAnnuncio, String authorization) {
        Annuncio annuncio = annuncioRepository.findById(idAnnuncio)
                .orElseThrow(() -> new ResourceNotFoundException(Resource.ANNUNCIO, idAnnuncio));
        Employer employer = employerService.extractEmployer(authorization);
        if (!annuncio.getEmployerId().equals(employer.getId())) {
            throw new UsersDontMatchException();
        }
        List<JobAppliance> appliances = applianceRepository.findAllByAnnuncioId(idAnnuncio);

        if (mongoTemplate.remove(annuncio).getDeletedCount() > 0) {
            applianceRepository.deleteAll(appliances);
            return fileService.fileCleanUp(appliances);

        }
        return false;
    }


    public AnnuncioDTO updateAnnuncio(String idAnnuncio, String authorization, AnnuncioRequest requestBody) {
        Annuncio annuncio = annuncioRepository.findById(idAnnuncio)
                .orElseThrow(() -> new ResourceNotFoundException(Resource.ANNUNCIO, idAnnuncio));
        Employer employer = employerService.extractEmployer(authorization);
        if (!annuncio.getEmployerId().equals(employer.getId())) {
            throw new UsersDontMatchException();
        }
        Annuncio annuncioAggiornato = annuncioMapper.updateAnnuncio(annuncio, requestBody);
        return annuncioMapper.annuncioToDTO(annuncioRepository.save(annuncioAggiornato));
    }

    public Page<AnnuncioDTO> findAnnunciByEmployer(String authorization, int pageNumber, int pageSize) {
        Employer employer = employerService.extractEmployer(authorization);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("publicationDate"));
        Page<Annuncio> page = annuncioRepository.findAllByEmployerId(employer.getId(), pageable);
        return page.map(annuncioMapper::annuncioToDTO);
    }

    public Page<AnnuncioDTO> findAllAnnunci(int pageNumber, int pageSize, String searchTerm) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("publicationDate"));
        Page<Annuncio> page;
        if (StringUtils.hasText(searchTerm)) {
            page = annuncioRepository.findAllByText(searchTerm, pageable);
        } else {
            page = annuncioRepository.findAll(pageable);
        }
        return page.map(annuncioMapper::annuncioToDTO);
    }

    public AnnuncioDTO findAnnuncioById(String id) {
        return annuncioRepository.findById(id)
                .map(annuncioMapper::annuncioToDTO)
                .orElseThrow(() -> new ResourceNotFoundException(Resource.ANNUNCIO, id));
    }
}
