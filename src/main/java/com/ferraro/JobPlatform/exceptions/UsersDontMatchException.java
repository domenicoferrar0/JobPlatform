package com.ferraro.JobPlatform.exceptions;

public class UsersDontMatchException extends RuntimeException{

    public UsersDontMatchException(){
        super("Spiacente, non hai l'autorizzazione per effettuare questa operazione");
    }
}
