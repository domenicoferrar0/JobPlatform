package com.ferraro.JobPlatform.service;

import com.ferraro.JobPlatform.dto.EmployerDTO;
import com.ferraro.JobPlatform.dto.request.EmployerSignUpRequest;
import com.ferraro.JobPlatform.enums.Resource;
import com.ferraro.JobPlatform.enums.Role;
import com.ferraro.JobPlatform.exceptions.DuplicateRegistrationException;
import com.ferraro.JobPlatform.exceptions.MailNotSentException;
import com.ferraro.JobPlatform.exceptions.ResourceNotFoundException;
import com.ferraro.JobPlatform.exceptions.UserUnauthorizedException;
import com.ferraro.JobPlatform.mappers.EmployerMapper;
import com.ferraro.JobPlatform.model.document.Employer;
import com.ferraro.JobPlatform.repository.EmployerRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployerService {

    @Autowired
    private AccountService accountService;

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private EmployerMapper employerMapper;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private MailService mailService;

    @Autowired
    private JwtService jwtService;

    public Employer extractEmployer(String authorization) {
        String jwt = authorization.substring(7);
        String email = jwtService.extractSubject(jwt);
        Employer employer = employerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(Resource.EMPLOYER));
        if (employer.getRole() != Role.ROLE_EMPLOYER) {
            throw new UserUnauthorizedException(employer.getRole());
        }
        return employer;
    }


    @Transactional
    public EmployerDTO saveEmployer(EmployerSignUpRequest request) {
        if (accountService.existsByEmail(request.getEmail())) {
            throw new DuplicateRegistrationException(request.getEmail());
        }
        Employer employer = employerMapper.requestToEmployer(request);
        employer.setRole(Role.ROLE_EMPLOYER);
        employer.setPassword(encoder.encode(employer.getPassword()));

        //Viene salvato, il repository ritorna l'entità salvata che a sua volta viene mappata in DTO
        EmployerDTO newEmployer = employerMapper.employerToDto(employerRepository.save(employer));
        String token = accountService.createConfirmationToken(newEmployer.getId(), Role.ROLE_EMPLOYER);
        try {
            mailService.sendRegistrationMail(employer, token);
        } catch (MessagingException e) {
            throw new MailNotSentException();
        }
        //Viene salvato, il repository ritorna l'entità salvata che a sua volta viene mappata in DTO
        return newEmployer;

    }
}
