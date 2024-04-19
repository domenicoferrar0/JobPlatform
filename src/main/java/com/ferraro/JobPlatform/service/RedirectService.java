package com.ferraro.JobPlatform.service;

import com.ferraro.JobPlatform.exceptions.UserNotEnabledException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collection;

@Service
public class RedirectService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /*Avevo bisogno di un filtro per prevenire l'accesso alla pagina di login
     * da parte di chi aveva già fatto il login e aveva quindi un token
     * valido salvato nei cookie. */
    public String redirectIfLogged(String authorization) {
        final String loginPage = "/login";
        final String jwt = authorization.substring(7);
        final String email = jwtService.extractSubject(jwt);
        if (!StringUtils.hasText(email)) {
            return loginPage;
        }
        UserDetails user;
        try {
            user = userDetailsService.loadUserByUsername(email);
        } catch (UserNotEnabledException ex) {
            return loginPage;
        }
        if (!jwtService.isTokenValid(jwt, user)) {
            return loginPage;
        }
        return determineUrl(user.getAuthorities());
    }

    public static String determineUrl(Collection<? extends GrantedAuthority> authority) {
        String url = "/error";
        for (GrantedAuthority a : authority) {
            switch (a.getAuthority()) {

                case "ROLE_USER":
                    url = "/user/home";
                    break;

                case "ROLE_EMPLOYER":
                    url = "/employer/home";
                    break;

                case "ROLE_ADMIN":
                    url = "/admin/home";
                    break;
            }
        }
        return url;
    }
}