package com.ferraro.JobPlatform.exceptions;

public class FileHandlingException extends RuntimeException{
    public FileHandlingException(String message){
        super(message);
    }

    public FileHandlingException(){
        super("Spiacenti, si è verificato un errore con la gestione del file, riprova più tardi");
    }

}
