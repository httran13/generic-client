package com.mhc.generic.fabricclientapi.exceptions;



import org.apache.log4j.Logger;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;



public class AsyncException implements AsyncUncaughtExceptionHandler{

    private Logger logger = Logger.getLogger(AsyncException.class);


    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {

        logger.error(throwable.getMessage());
        try {
            throw throwable;
        } catch (Throwable throwable1) {
            throwable1.printStackTrace();
        }

    }
}
