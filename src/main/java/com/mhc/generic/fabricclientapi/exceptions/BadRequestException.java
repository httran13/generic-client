package com.mhc.generic.fabricclientapi.exceptions;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {


    private static final long serialVersionUID = -5434971176583714608L;
    private Logger logger = Logger.getLogger(BadRequestException.class);


    public BadRequestException(String msg){

        super(msg);
        logger.error(msg);
    }
}
