package com.ferraro.JobPlatform.exceptions;

public class ApplianceSavingFailureException extends RuntimeException{

    public ApplianceSavingFailureException(){
        super("Si è verificato un errore con l'invio della domanda, se il problema persiste non esitare a contattarci");
    }
}
