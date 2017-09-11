package com.aiyolo.service.api.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DeviceObject {

    private String imei = "";
    private Integer netStatus = 0;
    private Integer devStatus = 0;
    private Integer alarmType = 0;
    private String glName = "";
    private String version = "";
    private Integer admin = 0;

    public DeviceObject() {
    }

    public DeviceObject(String imei, Integer netStatus, Integer devStatus, Integer alarmType, String glName, String version, Integer admin) {
        this.imei = imei;
        this.netStatus = netStatus;
        this.devStatus = devStatus;
        this.alarmType = alarmType;
        this.glName = glName;
        this.version = version;
        this.admin = admin;
    }

    @Override
    public String toString() {
        return "DeviceObject{" +
                "imei='" + imei + '\'' +
                ", net_status=" + netStatus +
                ", dev_status=" + devStatus +
                ", alarm_type=" + alarmType +
                ", gl_name='" + glName + '\'' +
                ", version='" + version + '\'' +
                ", admin=" + admin +
                '}';
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public Integer getNetStatus() {
        return netStatus;
    }

    public void setNetStatus(Integer netStatus) {
        this.netStatus = netStatus;
    }

    public Integer getDevStatus() {
        return devStatus;
    }

    public void setDevStatus(Integer devStatus) {
        this.devStatus = devStatus;
    }

    public Integer getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(Integer alarmType) {
        this.alarmType = alarmType;
    }

    public String getGlName() {
        return glName;
    }

    public void setGlName(String glName) {
        this.glName = glName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getAdmin() {
        return admin;
    }

    public void setAdmin(Integer admin) {
        this.admin = admin;
    }

}
