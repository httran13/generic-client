package com.mhc.generic.fabricclientapi.services;


import com.mhc.generic.fabricclientapi.dto.Request;
import com.mhc.generic.fabricclientapi.dto.Response;
import com.mhc.generic.fabricclientapi.dto.ServicePojo;
import com.mhc.generic.fabricclientapi.dto.ServiceReturnPojo;
import com.mhc.generic.fabricclientapi.exceptions.BadRequestException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ServiceMapper {

    private Logger logger = Logger.getLogger(ServiceMapper.class);


    public List<ServicePojo> mapRequestToService(Request request, String type, String caller){


        List<ServicePojo> listOfTransaction = new ArrayList<>();

        for(Map<String, Object> object: request.getTransactions()){
            ServicePojo servicePojo = new ServicePojo();
            servicePojo.setType(type);
            servicePojo.setCaller(caller);

            //get fcn
            String fcn = object.get("FCN").toString();
            if (fcn == null){
                throw new BadRequestException("FCN Missing");
            }
            logger.debug("Setting FCN = "+fcn);

            servicePojo.setFunctionName(fcn);

            //build args
            String[] args = new String[object.size()-1];
            SortedSet<String> keys = new TreeSet<>(object.keySet());
            int i = 0;
            for(String key : keys){
                if(key.contains("arg")){
                    args[i] = object.get(key).toString();
                    logger.debug("Setting "+key);
                    i++;
                }
            }
            servicePojo.setArgs(args);


            //add to list
            listOfTransaction.add(servicePojo);

        }

        return listOfTransaction;
    }

    public Response mapServiceToResponse(ServiceReturnPojo serviceReturnPojo){
        Response response = new Response();
        response.setDuration(String.valueOf(serviceReturnPojo.getDuration())+"ms");
        response.setTxIds(serviceReturnPojo.getResults());
        return response;
    }



}
