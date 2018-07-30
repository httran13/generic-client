package com.mhc.generic.fabricclientapi.controllers;


import com.mhc.generic.fabricclientapi.dto.Request;
import com.mhc.generic.fabricclientapi.dto.Response;
import com.mhc.generic.fabricclientapi.dto.ServicePojo;
import com.mhc.generic.fabricclientapi.dto.UserRegister;
import com.mhc.generic.fabricclientapi.services.FabricService;
import com.mhc.generic.fabricclientapi.services.ServiceMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.mhc.generic.fabricclientapi.Constants.INVOKE;
import static com.mhc.generic.fabricclientapi.Constants.QUERY;

@Api(tags = "Generic API")
@RestController
@RequestMapping(path = "/api/visa")
public class GenericController {


    private Logger logger = Logger.getLogger(GenericController.class);

    private FabricService fabricService;
    private ServiceMapper serviceMapper;

    @Autowired
    public GenericController(FabricService fabricService, ServiceMapper serviceMapper){
        this.serviceMapper = serviceMapper;
        this.fabricService = fabricService;
    }

    @RequestMapping(value ="/invoke", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Invoke a transaction on Chaincode")
    @ApiResponses({ @ApiResponse(code = 201, message = "Invoked"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal Server Error") })
    public Response invokeCC(@RequestBody Request request, @RequestHeader("caller") String caller){

        List<ServicePojo> servicePojos = serviceMapper.mapRequestToService(request, INVOKE,caller);
        logger.debug(servicePojos);
        return serviceMapper.mapServiceToResponse(fabricService.invoke(servicePojos, caller, INVOKE));
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Query for a state on the chaincode")
    @ApiResponses({ @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal Server Error") })
    public String queryCC(@RequestBody Request request, @RequestHeader("caller") String caller){

        List<ServicePojo> servicePojos = serviceMapper.mapRequestToService(request, QUERY, caller);

        return fabricService.query(servicePojos.get(0), caller, QUERY);
    }


    @RequestMapping(value="/users", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Register a user for fabric network in order for invoke/query")
    @ApiResponses({ @ApiResponse(code = 201, message = "Invoked"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 500, message = "Internal Server Error") })
    public UserRegister registerUser(@RequestBody UserRegister userRegister){
        return fabricService.createUser(userRegister);
    }
}
