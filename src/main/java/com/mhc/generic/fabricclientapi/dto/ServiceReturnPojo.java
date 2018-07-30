package com.mhc.generic.fabricclientapi.dto;

import java.util.Map;

public class ServiceReturnPojo {
    private int duration;

    private Map<String, String> results;



    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Map<String, String> getResults() {
        return results;
    }

    public void setResults(Map<String, String> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "ServiceReturnPojo{" +
                "duration=" + duration +
                ", results=" + results +
                '}';
    }
}
