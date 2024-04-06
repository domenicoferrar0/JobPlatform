package com.ferraro.JobPlatform.exceptions;

public class MailNotSentException extends RuntimeException{
    public MailNotSentException(){
        super("Si è verificato un errore, registrazione fallita, se il problema sussiste contattare gli admin");
    }
}
