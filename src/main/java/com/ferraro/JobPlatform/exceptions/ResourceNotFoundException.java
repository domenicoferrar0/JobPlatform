package com.ferraro.JobPlatform.exceptions;

import com.ferraro.JobPlatform.enums.Resource;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(Resource resource, String id){
        super("Impossibile trovare "+resource.getNome()+" con il seguente id: "+id);
    }

    public ResourceNotFoundException(Resource resource){
        super("Impossibile trovare "+resource.getNome());
    }
}
