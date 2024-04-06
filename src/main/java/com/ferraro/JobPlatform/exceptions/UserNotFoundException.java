package com.ferraro.JobPlatform.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(){
        super("Si è verificato un problema, utente non trovato");
    }
}
