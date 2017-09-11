package com.aiyolo.cache.entity;

import com.aiyolo.constant.DeviceNetStatusEnum;

public class DeviceLatestStatus implements Cloneable {

    private String glId = "";
    private String version = "";
    private Integer temperature = -4000;
    private Integer humidity = 0;
    private Integer netStatus = DeviceNetStatusEnum.NET.getValue(); // 默认给2G
    private Integer devStatus = 0;
    private Integer alarmType = 0;

    public DeviceLatestStatus() {}

    public DeviceLatestStatus(String glId, String version, Integer temperature, Integer humidity,
            Integer netStatus, Integer devStatus, Integer alarmType) {
        this.glId = glId;
        this.version = version;
        this.temperature = temperature;
        this.humidity = humidity;
        this.netStatus = netStatus;
        this.devStatus = devStatus;
        this.alarmType = alarmType;
    }

    @Override
    public Object clone() {
        DeviceLatestStatus deviceLatestStatus = null;
        try {
            deviceLatestStatus = (DeviceLatestStatus) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return deviceLatestStatus;
    }

    public String getGlId() {
        return glId;
    }

    public void setGlId(String glId) {
        this.glId = glId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
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

}
