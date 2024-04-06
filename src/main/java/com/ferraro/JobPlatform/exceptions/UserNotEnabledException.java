package com.ferraro.JobPlatform.exceptions;

public class UserNotEnabledException extends RuntimeException{

    public UserNotEnabledException(){
        super("Utente non abilitato, assicurati di aver verificato l'account!");
    }
}
