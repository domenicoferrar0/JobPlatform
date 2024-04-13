package com.ferraro.JobPlatform.exceptions;

public class DuplicateApplianceException extends RuntimeException{

    public DuplicateApplianceException(){
        super("Spiacente, hai già inviato la tua candidatura per questo annuncio");
    }
}
