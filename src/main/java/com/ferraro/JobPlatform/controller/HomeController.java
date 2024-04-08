package com.ferraro.JobPlatform.controller;

import com.ferraro.JobPlatform.dto.AnnuncioDTO;
import com.ferraro.JobPlatform.dto.EmployerDTO;
import com.ferraro.JobPlatform.dto.UserDTO;
import com.ferraro.JobPlatform.dto.request.EmployerSignUpRequest;
import com.ferraro.JobPlatform.dto.request.LoginRequest;
import com.ferraro.JobPlatform.dto.request.UserSignUpRequest;
import com.ferraro.JobPlatform.service.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${app.homeAPI}")
@Slf4j
public class HomeController {


    @Autowired
    private UserDetailsServiceImpl userDetails;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AnnuncioService annuncioService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmployerService employerService;

    @Autowired
    private AccountService accountService;


    @PostMapping("/signup")
    public ResponseEntity<UserDTO> userSignUp(@NonNull @Valid @RequestBody UserSignUpRequest requestBody) {
        log.info("REGISTRA<IONE");
        return ResponseEntity.ok(userService.saveUser(requestBody));
    }

    @PostMapping("employer-signup")
    //TODO CREARE CLASSE SEPARATA PER GLI EMPLOYER CON P.IVA, NOME Azienda e vari dati relativi DA EMBEDDARE UN USER
    public ResponseEntity<EmployerDTO> employerSignUp(@NonNull @RequestBody @Valid EmployerSignUpRequest requestBody) {
        log.info("PARTITA IVA",requestBody.getPartitaIva());
        return ResponseEntity.ok(employerService.saveEmployer(requestBody));
    }

    @PutMapping("/confirmation")
    public ResponseEntity<String> confirmUser(@NonNull @RequestParam("token") String token) {
        if (!accountService.confirmToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Si è verificato un errore, impossibile verificare l'account");
        }
        return ResponseEntity.ok("Account verificato correttamente!");
    }


    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@NonNull @RequestBody @Valid LoginRequest loginRequest) {
        UserDetails user = userDetails.loadUserByUsername(loginRequest.getEmail());
        try {
            Authentication authentication = authManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Email o password non valide");
        }
        return ResponseEntity.ok(jwtService.generateToken(user));
    }

    @GetMapping("/annunci/{page}")
    public ResponseEntity<Page<AnnuncioDTO>> getAllAnnunci(@RequestParam(value = "searchTerm", required = false) String searchTerm,
                                                           @PathVariable("page") int currentPage) {
        int pageSize = 10;
        return ResponseEntity.ok(annuncioService.findAllAnnunci(currentPage, pageSize, searchTerm));
    }

    @GetMapping("annuncio/{id}")
    public ResponseEntity<AnnuncioDTO> findById(@PathVariable("id") String annuncioId) {
        return ResponseEntity.ok(annuncioService.findAnnuncioById(annuncioId));
    }


}
