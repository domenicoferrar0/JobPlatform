package com.ferraro.JobPlatform.service;

import com.ferraro.JobPlatform.dto.AnnuncioDTO;
import com.ferraro.JobPlatform.dto.request.AnnuncioRequest;
import com.ferraro.JobPlatform.enums.Role;
import com.ferraro.JobPlatform.exceptions.AnnuncioNotFoundException;
import com.ferraro.JobPlatform.exceptions.UserNotFoundException;
import com.ferraro.JobPlatform.exceptions.UserUnauthorizedException;
import com.ferraro.JobPlatform.exceptions.UsersDontMatchException;
import com.ferraro.JobPlatform.mappers.AnnuncioMapper;
import com.ferraro.JobPlatform.model.document.Annuncio;
import com.ferraro.JobPlatform.model.document.User;
import com.ferraro.JobPlatform.repository.AnnuncioRepository;
import com.ferraro.JobPlatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

@Service
public class AnnuncioService {
    @Autowired
    private AnnuncioRepository annuncioRepository;

    @Autowired
    private AnnuncioMapper annuncioMapper;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private User extractEmployer(String authorization) {
        String jwt = authorization.substring(7);
        String email = jwtService.extractSubject(jwt);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException());
        if (user.getRole() != Role.ROLE_EMPLOYER) {
            throw new UserUnauthorizedException(user.getRole());
        }
        return user;
    }

    public AnnuncioDTO saveAnnuncio(String authorization, AnnuncioRequest requestBody) {
        User employer = extractEmployer(authorization);
        Annuncio annuncio = annuncioMapper.requestToAnnuncio(requestBody);
        annuncio.setPublicationDate(LocalDate.now());
        annuncio.setRecruiterId(employer.getId());
        return annuncioMapper.annuncioToDTO(annuncioRepository.save(annuncio));
    }

    public boolean deleteAnnuncio(String idAnnuncio, String authorization) {
        Annuncio annuncio = annuncioRepository.findById(idAnnuncio)
                .orElseThrow(() -> new AnnuncioNotFoundException(idAnnuncio));
        User employer = extractEmployer(authorization);
        if (annuncio.getRecruiterId() != employer.getId()) {
            throw new UsersDontMatchException();
        }
        Query query = new Query(Criteria.where("_id").is(idAnnuncio));
        return mongoTemplate.remove(query, Annuncio.class).getDeletedCount() > 0;
    }

    public AnnuncioDTO updateAnnuncio(String idAnnuncio, String authorization, AnnuncioRequest requestBody) {
        Annuncio annuncio = annuncioRepository.findById(idAnnuncio)
                .orElseThrow(() -> new AnnuncioNotFoundException(idAnnuncio));
        User employer = extractEmployer(authorization);
        if (annuncio.getRecruiterId() != employer.getId()) {
            throw new UsersDontMatchException();
        }
        Annuncio annuncioAggiornato = annuncioMapper.updateAnnuncio(annuncio, requestBody);
        return annuncioMapper.annuncioToDTO(annuncioRepository.save(annuncioAggiornato));
    }

    public Page<AnnuncioDTO> findAnnunciByRecruiter(String authorization, int pageNumber, int pageSize) {
        User employer = extractEmployer(authorization);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("publicationDate"));
        Page<Annuncio> page = annuncioRepository.findAllByRecruiterId(employer.getId(), pageable);
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

    public AnnuncioDTO findAnnuncioById(String id){
        return annuncioRepository.findById(id)
                .map(annuncioMapper::annuncioToDTO)
                .orElseThrow(()-> new AnnuncioNotFoundException(id));
    }
}
