package com.ferraro.JobPlatform.exceptions;

public class AnnuncioNotFoundException extends RuntimeException{

    public AnnuncioNotFoundException(String id){
        super("Spiacente, non è stato possibile trovare l'annuncio corrispondente".concat(id));
    }
}
