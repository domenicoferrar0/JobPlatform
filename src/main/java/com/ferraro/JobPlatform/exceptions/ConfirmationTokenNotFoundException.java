package com.ferraro.JobPlatform.exceptions;

public class ConfirmationTokenNotFoundException extends RuntimeException{

    public ConfirmationTokenNotFoundException(){
        super("Si è verificato un errore, impossibile verificare l'account");
    }
}
