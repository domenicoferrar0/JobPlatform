package com.ferraro.JobPlatform.exceptions;

public class DuplicateRegistrationException extends RuntimeException{

    public DuplicateRegistrationException(String field){
        super("Attenzione, utente già registrato ".concat(field));
    }
}
