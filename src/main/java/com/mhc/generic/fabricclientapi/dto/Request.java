package com.mhc.generic.fabricclientapi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

@ApiModel
public class Request {


    @ApiModelProperty(example = "{\"transactions\":[{\"FCN\":\"invoke\", \"arg0\":\"a\", \"arg1\":\"b\"},{\"FCN\":\"invoke\", \"arg0\":\"b\", \"arg1\":\"a\"}]}")
    List<Map<String, Object>> transactions;


    public List<Map<String, Object>> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Map<String, Object>> transactions) {
        this.transactions = transactions;
    }
}
