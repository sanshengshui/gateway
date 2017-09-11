package com.aiyolo.service.api.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DeviceAlarmObject {

    private String imei = "";
    private Integer alarmType = 0;
    private Boolean solve = false;
    private String time = "";
    private String glName = "";

    public DeviceAlarmObject() {
    }

    public DeviceAlarmObject(String imei, Integer alarmType, Boolean solve, String time, String glName) {
        this.imei = imei;
        this.alarmType = alarmType;
        this.solve = solve;
        this.time = time;
        this.glName = glName;
    }

    @Override
    public String toString() {
        return "DeviceAlarmObject{" +
                "imei='" + imei + '\'' +
                ", alarm_type=" + alarmType +
                ", solve=" + solve +
                ", time='" + time + '\'' +
                ", gl_name='" + glName + '\'' +
                '}';
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public Integer getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(Integer alarmType) {
        this.alarmType = alarmType;
    }

    public Boolean getSolve() {
        return solve;
    }

    public void setSolve(Boolean solve) {
        this.solve = solve;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGlName() {
        return glName;
    }

    public void setGlName(String glName) {
        this.glName = glName;
    }

}
