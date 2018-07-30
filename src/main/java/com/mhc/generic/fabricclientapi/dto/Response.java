package com.mhc.generic.fabricclientapi.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

@Api
public class Response {

    @ApiModelProperty
    Map<String, String> txIds;

    @ApiModelProperty
    String duration;

    public Map<String, String> getTxIds() {
        return txIds;
    }

    public void setTxIds(Map<String, String> txIds) {
        this.txIds = txIds;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
