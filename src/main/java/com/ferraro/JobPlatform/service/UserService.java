package com.ferraro.JobPlatform.service;

import com.ferraro.JobPlatform.dto.UserDTO;
import com.ferraro.JobPlatform.dto.request.SignUpRequest;
import com.ferraro.JobPlatform.enums.Role;
import com.ferraro.JobPlatform.exceptions.ConfirmationTokenNotFoundException;
import com.ferraro.JobPlatform.exceptions.DuplicateRegistrationException;
import com.ferraro.JobPlatform.exceptions.MailNotSentException;
import com.ferraro.JobPlatform.exceptions.UserNotFoundException;
import com.ferraro.JobPlatform.mappers.UserMapper;
import com.ferraro.JobPlatform.model.document.ConfirmationToken;
import com.ferraro.JobPlatform.model.document.User;
import com.ferraro.JobPlatform.repository.ConfirmationTokenRepository;
import com.ferraro.JobPlatform.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConfirmationTokenRepository tokenRepository;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private MailService mailService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Transactional
    public boolean confirmToken(String token) {
        ConfirmationToken confirmationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new ConfirmationTokenNotFoundException());
        User user = userRepository.findById(confirmationToken.getUserId())
                .orElseThrow(() -> new UserNotFoundException());
        user.setEnabled(true);
        userRepository.save(user);
        Query query = new Query(Criteria.where("_id").is(confirmationToken.getId()));
        return mongoTemplate.remove(query, ConfirmationToken.class).getDeletedCount() > 0;

    }

    @Transactional
    public String createConfirmationToken(String userId) {
        ConfirmationToken token = new ConfirmationToken(userId);
        tokenRepository.save(token);
        return token.getToken();
    }

    @Transactional
    public UserDTO saveUser(SignUpRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateRegistrationException(request.getEmail());
        }
        if (userRepository.existsByCf(request.getCf())) {
            throw new DuplicateRegistrationException(request.getCf());
        }
        User user = userMapper.requestToUser(request);
        user.setRole(Role.ROLE_USER);
        user.setPassword(encoder.encode(user.getPassword()));

        //Viene salvato, il repository ritorna l'entità salvata che a sua volta viene mappata in DTO
        UserDTO newUser = userMapper.userToDto(userRepository.save(user));
        String token = createConfirmationToken(newUser.getId());

        try {
            mailService.sendRegistrationMail(user, token);
        } catch (MessagingException e) {
            log.error("MAIL NON MANDATA", e);
            throw new MailNotSentException();
        }
        //Viene salvato, il repository ritorna l'entità salvata che a sua volta viene mappata in DTO
        return newUser;
    }
}
