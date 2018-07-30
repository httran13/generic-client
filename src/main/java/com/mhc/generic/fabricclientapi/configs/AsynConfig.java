package com.mhc.generic.fabricclientapi.configs;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;

@Configuration
@ComponentScan("com.mhc.generic.fabricclientapi")
@EnableAsync
public class AsynConfig implements AsyncConfigurer{


    @Override
    public Executor getAsyncExecutor(){
        return new SimpleAsyncTaskExecutor();
    }
}
