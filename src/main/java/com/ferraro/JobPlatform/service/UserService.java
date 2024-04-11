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
import com.ferraro.JobPlatform.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserService {

    /*TODO USARE USER E EMPLOYER REPOSITORY SEPARATI CON DIFFERENTI COLLECTIONS
     * CREARE ACCOUNT FACTORY, CON METODI:
     * PER CONTROLLARE SE ESISTE UNA MAIL IN ENTRAMBE LE COLLECTIONS
     * PER RITORNARE UN ACCOUNT ANDANDO A PESCARE DA ENTRAMBE LE COLLECTIONS
     *
     * */

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

        //Viene salvato, il repository ritorna l'entità salvata che a sua volta viene mappata in DTO
        UserDTO newUser = userMapper.userToDto(userRepository.save(user));
        String token = accountService.createConfirmationToken(newUser.getId(), Role.ROLE_USER);
        try {
            mailService.sendRegistrationMail(user, token);
        } catch (MessagingException e) {
            throw new MailNotSentException();
        }
        //Viene salvato, il repository ritorna l'entità salvata che a sua volta viene mappata in DTO
        return newUser;
    }
}
