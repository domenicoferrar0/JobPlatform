package com.ferraro.JobPlatform.service;

import com.ferraro.JobPlatform.dto.UserDTO;
import com.ferraro.JobPlatform.dto.request.UserSignUpRequest;
import com.ferraro.JobPlatform.enums.Resource;
import com.ferraro.JobPlatform.enums.Role;
import com.ferraro.JobPlatform.exceptions.DuplicateRegistrationException;
import com.ferraro.JobPlatform.exceptions.MailNotSentException;
import com.ferraro.JobPlatform.exceptions.ResourceNotFoundException;
import com.ferraro.JobPlatform.exceptions.UserUnauthorizedException;
import com.ferraro.JobPlatform.mappers.UserMapper;
import com.ferraro.JobPlatform.model.document.User;
import com.ferraro.JobPlatform.repository.AnnuncioRepository;
import com.ferraro.JobPlatform.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class UserService {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private MailService mailService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AnnuncioRepository annuncioRepository;

    public User extractUser(String authorization) {
        String jwt = authorization.substring(7);
        String email = jwtService.extractSubject(jwt);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(Resource.USER));
        if (user.getRole() != Role.ROLE_USER) {
            throw new UserUnauthorizedException(user.getRole());
        }
        return user;
    }


    @Transactional
    public UserDTO saveUser(UserSignUpRequest request) {
        if (accountService.existsByEmail(request.getEmail())) {
            throw new DuplicateRegistrationException(request.getEmail());
        }
        User user = userMapper.requestToUser(request);
        user.setRole(Role.ROLE_USER);
        user.setPassword(encoder.encode(user.getPassword()));

        UserDTO newUser = userMapper.userToDto(userRepository.save(user));
        String token = accountService.createConfirmationToken(newUser.getId(), Role.ROLE_USER);
        try {
            mailService.sendRegistrationMail(user, token);
        } catch (MessagingException e) {
            throw new MailNotSentException();
        }
        return newUser;
    }

    //TODO TEST GESTIONE PREFERITI
    /*Come vengono gestiti gli annunci preferiti:
     * User ha un Set di String che rappresenta gli id degli annunci preferiti
     * eseguo la fetch dei preferiti andando a usare operatore $in e passando la lista degli id
     * quando un annuncio viene eliminato viene effettuato un pull sulla collection degli utenti
     * andando a rimuovere l'id eliminato dalle varie liste */
    public boolean addFavouriteAnnuncio(String authorization, String annuncioId) {
        User user = extractUser(authorization);
        if (!annuncioRepository.existsById(annuncioId)) {
            throw new ResourceNotFoundException(Resource.ANNUNCIO, annuncioId);
        }

        //Necessario perché se l'utente non ha ancora salvato nulla dal DB gli arriva come null
        if (user.getFavouriteAnnouncements() == null) {
            user.setFavouriteAnnouncements(new HashSet<>());
        }
        if(!user.getFavouriteAnnouncements().add(annuncioId)){
            return false;
        }
        userRepository.save(user);
        return true;
    }

    public boolean removeFavourite(String authorization, String annuncioId) {
        User user = extractUser(authorization);
        Set<String> preferiti = user.getFavouriteAnnouncements();
        if(preferiti == null){
            return false;
        }
       if(!user.getFavouriteAnnouncements().remove(annuncioId)){
           return false;
       }
        userRepository.save(user);
        return true;
    }
}
