package com.mhc.generic.fabricclientapi.dto;

import java.util.Arrays;
import java.util.Map;

public class ServicePojo {

    private String caller;

    private String functionName;

    private String[] args;

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCaller() {
        return caller;
    }

    public void setCaller(String caller) {
        this.caller = caller;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    @Override
    public String toString() {
        return "ServicePojo{" +
                "caller='" + caller + '\'' +
                ", functionName='" + functionName + '\'' +
                ", args=" + Arrays.toString(args) +
                ", type='" + type + '\'' +
                '}';
    }
}
