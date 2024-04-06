package com.ferraro.JobPlatform.controller;

import com.ferraro.JobPlatform.dto.LoginDTO;
import com.ferraro.JobPlatform.dto.UserDTO;
import com.ferraro.JobPlatform.dto.request.SignUpRequest;
import com.ferraro.JobPlatform.service.JwtService;
import com.ferraro.JobPlatform.service.UserDetailsServiceImpl;
import com.ferraro.JobPlatform.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    private UserService userService;

    @Autowired
    private UserDetailsServiceImpl userDetails;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> userSignUp(@NonNull @Valid @RequestBody SignUpRequest request) {
        log.info("REGISTRA<IONE");
        return ResponseEntity.ok(userService.saveUser(request));
    }

    @PutMapping("/confirmation")
    public ResponseEntity<String> confirmUser(@NonNull @RequestParam("token") String token) {
        if (!userService.confirmToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Si è verificato un errore, impossibile verificare l'account");
        }
        return ResponseEntity.ok("Account verificato correttamente!");
    }


    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@NonNull @RequestBody @Valid LoginDTO loginDTO) {
        UserDetails user = userDetails.loadUserByUsername(loginDTO.getEmail());
        try {
            Authentication authentication = authManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Email o password non valide");
        }
        return ResponseEntity.ok(jwtService.generateToken(user));
    }
}
