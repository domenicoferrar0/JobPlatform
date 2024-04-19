package com.ferraro.JobPlatform.service;

import com.ferraro.JobPlatform.dto.AnnuncioDTO;
import com.ferraro.JobPlatform.dto.AnnuncioDTOSimple;
import com.ferraro.JobPlatform.dto.request.AnnuncioRequest;
import com.ferraro.JobPlatform.enums.Resource;
import com.ferraro.JobPlatform.exceptions.ResourceNotFoundException;
import com.ferraro.JobPlatform.exceptions.UsersDontMatchException;
import com.ferraro.JobPlatform.mappers.AnnuncioMapper;
import com.ferraro.JobPlatform.model.document.Annuncio;
import com.ferraro.JobPlatform.model.document.Employer;
import com.ferraro.JobPlatform.model.document.JobAppliance;
import com.ferraro.JobPlatform.model.document.User;
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
import org.springframework.data.mongodb.core.query.Update;
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
    private UserService userService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private FileService fileService;


    /*Viene estratto l'employer che ha creato l'annuncio attraverso
    * il JWT creo l'oggetto a partire dalla richiesta,
    * inserisco i valori di referencing dell'employer
    * e viene salvato a mappato come DTO in modo da poter
    * essere inserito nel response */
    public AnnuncioDTO saveAnnuncio(String authorization, AnnuncioRequest requestBody) {
        Employer employer = employerService.extractEmployer(authorization);
        Annuncio annuncio = annuncioMapper.requestToAnnuncio(requestBody);
        annuncio.setPublicationDate(LocalDate.now());
        annuncio.setEmployerId(employer.getId());
        annuncio.setEmployerName(employer.getNomeAzienda());
        return annuncioMapper.annuncioToDTO(annuncioRepository.save(annuncio));
    }

    /*La cancellazione dell'annuncio richiede una serie di operazioni
    * cancellare dal DB tutte le appliances relative, oltre che ai pdf
    * allegati con la richiesta */
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
            return fileService.fileCleanUp(appliances) && favouriteCleanUp(idAnnuncio);

        }
        return false;
    }

    /*Prima di effettuare l'aggiornamento viene controllato che chi sta effettuando
    * la richiesta e chi ha creato l'annuncio combacino */
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

    public Page<AnnuncioDTOSimple> findAnnunciByEmployer(String authorization, int pageNumber, int pageSize) {
        Employer employer = employerService.extractEmployer(authorization);
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize, Sort.by("publicationDate"));
        Page<Annuncio> page = annuncioRepository.findAllByEmployerId(employer.getId(), pageable);
        return page.map(annuncioMapper::annuncioToDTOSimple);
    }

    //TODO AGGIUNGERE VARI FILTRI
    //In questo caso c'è una logica di query se presente il searchterm o è semplicemente un findAll
    public Page<AnnuncioDTOSimple> findAllAnnunci(int pageNumber, int pageSize, String searchTerm) {
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize, Sort.by("publicationDate"));
        Page<Annuncio> page;
        if (StringUtils.hasText(searchTerm)) {
            page = annuncioRepository.findAllByText(searchTerm, pageable);
        } else {
            page = annuncioRepository.findAll(pageable);
        }
        return page.map(annuncioMapper::annuncioToDTOSimple);
    }

    public AnnuncioDTO findAnnuncioById(String id) {
        return annuncioRepository.findById(id)
                .map(annuncioMapper::annuncioToDTO)
                .orElseThrow(() -> new ResourceNotFoundException(Resource.ANNUNCIO, id));
    }

    public Page<AnnuncioDTOSimple> findFavourites(String authorization, int page, int pageSize){
        User user = userService.extractUser(authorization);
        Pageable pageable = PageRequest.of(page-1, pageSize, Sort.by("publicationDate"));
        Page<Annuncio> annunci = annuncioRepository.findUserFavourites(user.getFavouriteAnnouncements(), pageable);
        return annunci.map(annuncioMapper::annuncioToDTOSimple);
    }

    /*Questo metodo è pensato per rimuovere dalla lista dei preferiti
    *dell'utente l'id di referencing dell'annuncio nel momento in cui
    * viene eliminato */
    @Transactional
    public boolean favouriteCleanUp(String annuncioId){
        Query query = new Query();
        Update update = new Update().pull("favouriteAnnouncements", annuncioId);
        return mongoTemplate.updateMulti(query, update, User.class).wasAcknowledged();

    }


}
