package com.mhc.generic.fabricclientapi.exceptions;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerException extends RuntimeException{
    Logger logger = Logger.getLogger(InternalServerException.class);

    public InternalServerException(String msg){
        super(msg);
        logger.error(msg);
    }
}
