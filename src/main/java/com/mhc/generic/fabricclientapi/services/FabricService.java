package com.mhc.generic.fabricclientapi.services;


import com.mhc.generic.fabricclientapi.dto.ServicePojo;
import com.mhc.generic.fabricclientapi.dto.ServiceReturnPojo;
import com.mhc.generic.fabricclientapi.dto.UserRegister;
import com.mhc.generic.fabricclientapi.exceptions.BadRequestException;
import com.mhc.generic.fabricclientapi.exceptions.InternalServerException;
import com.mhc.fabric.client.FabricClient;
import com.mhc.fabric.client.config.FabricConfig;
import com.mhc.fabric.client.models.ChaincodeInfo;
import org.apache.log4j.Logger;
import org.hyperledger.fabric.sdk.NetworkConfig;
import org.hyperledger.fabric.sdk.exception.*;
import org.hyperledger.fabric_ca.sdk.exception.EnrollmentException;
import org.hyperledger.fabric_ca.sdk.exception.RegistrationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.mhc.fabric.client.config.FabricConfigParams.MHC_FABRIC_CCNAME;
import static com.mhc.fabric.client.config.FabricConfigParams.MHC_FABRIC_CCVER;

@Service
public class FabricService {

    private Logger logger = Logger.getLogger(FabricService.class);

    private FabricClient fabricClient;
    private FabricConfig fabricConfig;


    @Autowired
    public FabricService(FabricClient fabricClient, FabricConfig fabricConfig){
        this.fabricClient = fabricClient;
        this.fabricConfig = fabricConfig;
    }

    public String query(ServicePojo servicePojo, String caller, String type){

        String payload = null;
        CompletableFuture<String> fut;

        fut = callTransaction(type, servicePojo.getCaller(), servicePojo.getFunctionName(),servicePojo.getArgs());


        try {
            payload = fut.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.error(e);
            throw new InternalServerException(e.getLocalizedMessage());
        } catch (ExecutionException e) {
            e.printStackTrace();
            logger.error(e);
            throw new InternalServerException(e.getLocalizedMessage());
        }


        logger.debug("Returning Payload:");
        logger.debug(payload);
        return payload;

    }


    public ServiceReturnPojo invoke(List<ServicePojo> transactions, String caller, String type){

        Map<String, CompletableFuture<String>> futList = new HashMap<>();
        Map<String, String> results = new HashMap<>();

        Integer i = 0;
        final long time = System.currentTimeMillis();
        logger.debug("Size of transactions"+transactions.size());

        for(ServicePojo servicePojo: transactions){
            futList.put(i.toString(), callTransaction(type, caller, servicePojo.getFunctionName(), servicePojo.getArgs()) );
            i++;
        }

        while(!futList.isEmpty()){
            futList.forEach((k,v) ->{
                if(v.isDone()){
                    try {
                        results.put(k, v.get());
                        logger.debug("adding to results "+k+ ":"+results.get(k));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        logger.error(e);
                        throw new InternalServerException(e.getLocalizedMessage());
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                        logger.error(e);
                        throw new InternalServerException(e.getLocalizedMessage());
                    }
                }
            });

            futList.keySet().removeAll(results.keySet());
        }
        logger.debug("Size of results"+results.size());

        final int duration = (int)(System.currentTimeMillis() - time);
        logger.debug("Completed in "+duration+"ms");

        ServiceReturnPojo serviceReturnPojo = new ServiceReturnPojo();
        serviceReturnPojo.setDuration(duration);
        serviceReturnPojo.setResults(results);

        logger.debug("Returning Invoke response");
        logger.debug(serviceReturnPojo.toString());
      return serviceReturnPojo;
    }

    @Async
    public CompletableFuture<String> callTransaction(String type, String caller, String fcn, String[] args){
        logger.debug(String.format("%s by %s, calling %s with %s", type, caller, fcn, Arrays.toString(args)));
        try{
            if(type.equals("INVOKE")){
                return fabricClient.invoke(caller, fcn, args, getCCinfo());
            }else{
                return CompletableFuture.completedFuture(fabricClient.query(caller, fcn, args, getCCinfo()));
            }
            }catch(ExecutionException| InstantiationException| InvocationTargetException| NoSuchMethodException| InterruptedException| IllegalAccessException| InvalidArgumentException| ProposalException| NetworkConfigurationException|CryptoException| TransactionException| ClassNotFoundException e ){
                logger.error(e);
                throw new InternalServerException(e.getMessage());
        }
    }

    public UserRegister createUser(UserRegister userRegister){

        logger.debug("Adding user "+userRegister.getUsername());
        String secret = null;

        UserRegister newUserRegister = new UserRegister();
        try {
            secret = fabricClient.addMemberToNetwork(userRegister.getUsername(), userRegister.getSecret()).getEnrollmentSecret();
            newUserRegister.setSecret(secret);
            newUserRegister.setUsername(userRegister.getUsername());
            return newUserRegister;

        } catch (org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException e) {
            e.printStackTrace();
            logger.error(e);
            throw new BadRequestException(e.getMessage());
        } catch (MalformedURLException|EnrollmentException|RegistrationException e) {
            e.printStackTrace();
            logger.error(e);
            throw new BadRequestException(e.getMessage());
        }

    }

    private ChaincodeInfo getCCinfo(){
        NetworkConfig networkConfig = fabricClient.getNetworkConfig();
        String channel = networkConfig.getChannelNames().iterator().next();
        ChaincodeInfo chaincodeInfo = new ChaincodeInfo(fabricConfig.getProperty(MHC_FABRIC_CCNAME), fabricConfig.getProperty(MHC_FABRIC_CCVER),channel);
        return chaincodeInfo;
    }

}
