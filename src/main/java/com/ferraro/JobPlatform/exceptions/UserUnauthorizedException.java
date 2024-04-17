package com.ferraro.JobPlatform.exceptions;

import com.ferraro.JobPlatform.enums.Role;

public class UserUnauthorizedException extends RuntimeException{
    public UserUnauthorizedException(Role role){
        super("Spiacente l'utente non ha i permessi per accedere a questo contenuto".concat(role.toString()));
    }
}
