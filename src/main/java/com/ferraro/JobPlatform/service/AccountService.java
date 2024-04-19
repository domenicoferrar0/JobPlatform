package com.ferraro.JobPlatform.service;

import com.ferraro.JobPlatform.enums.Resource;
import com.ferraro.JobPlatform.enums.Role;
import com.ferraro.JobPlatform.exceptions.ResourceNotFoundException;
import com.ferraro.JobPlatform.model.Account;
import com.ferraro.JobPlatform.model.document.ConfirmationToken;
import com.ferraro.JobPlatform.repository.ConfirmationTokenRepository;
import com.ferraro.JobPlatform.repository.EmployerRepository;
import com.ferraro.JobPlatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ConfirmationTokenRepository tokenRepository;

    /*Account è la superclasse di User ed Employer,
     non essendoci relazioni tra trabelle
     ho optato per una classe di utility che mi
     permettesse di effettuare operazioni comuni alle due classi
     e a mantenere consistency nelle email come chiave unica comune*/


    //booleano che controlla se la mail esiste in entrambi i repo
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email) ||
                employerRepository.existsByEmail(email);
    }

    /*In fase di autenticazione avevo bisogno di un
    * Optional che potesse cercare in entrambi i DB */
    public Optional<Account> findByEmail(String email) {
        Optional<Account> account;
        account = userRepository.findByEmail(email)
                .map(user -> (Account) user);
        if (account.isEmpty()) {
            account = employerRepository.findByEmail(email)
                    .map(user -> (Account) user);
        }
        return account;
    }

    /*Il token quando generato porta in riferimento il ruolo dell'utente
    * in questo modo so in che repository cercare l'utente in fase di abilitazione
    * dell'account  */
    @Transactional
    public boolean confirmToken(String token) {
        ConfirmationToken confirmationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException(Resource.TOKEN));
        Role role = confirmationToken.getAccountRole();
        String id = confirmationToken.getAccountId();
        Account account;
        if (role == Role.ROLE_USER) {
            account = userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(Resource.USER, id));
        }
        else {
            account = employerRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(Resource.EMPLOYER, id));
        }
        account.setEnabled(true);
        mongoTemplate.save(account);
        Query query = new Query(Criteria.where("_id").is(confirmationToken.getId()));
        return mongoTemplate.remove(query, ConfirmationToken.class).getDeletedCount() > 0;
    }

    /*Il token viene salvato e ritorno il UUID di riferimento
    * in modo da passarlo all'utente via mail */
    @Transactional
    public String createConfirmationToken(String userId, Role role) {
        ConfirmationToken token = new ConfirmationToken(userId, role);
        tokenRepository.save(token);
        return token.getToken();
    }


}
