package com.aiyolo.service.api.response;

public class AgingTestResponse
{


    private String lastSta;
    private String lastStatus;
    private String maxStatusContinuous;
    private boolean pass;

    public AgingTestResponse() {
    }

    public AgingTestResponse(String lastSta, String lastStatus, String maxStatusContinuous, boolean pass) {
        this.lastSta = lastSta;
        this.lastStatus = lastStatus;
        this.maxStatusContinuous = maxStatusContinuous;
        this.pass = pass;
    }

    public boolean isPass() {
        return pass;
    }

    public void setPass(boolean pass) {
        this.pass = pass;
    }

    public String getLastSta() {
        return lastSta;
    }

    public void setLastSta(String lastSta) {
        this.lastSta = lastSta;
    }

    public String getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(String lastStatus) {
        this.lastStatus = lastStatus;
    }

    public String getMaxStatusContinuous() {
        return maxStatusContinuous;
    }

    public void setMaxStatusContinuous(String maxStatusContinuous) {
        this.maxStatusContinuous = maxStatusContinuous;
    }
}
