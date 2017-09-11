package com.aiyolo.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class DeviceAlarmCancel extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String glId = "";
    private String glImei = "";
    private Integer timestamp = 0;
    private Integer temperature = 0;
    private Integer humidity = 0;

    @ManyToOne
    @JoinColumn(name = "glId", referencedColumnName = "glId", insertable = false, updatable = false)
    private Device device;

    protected DeviceAlarmCancel() {}

    public DeviceAlarmCancel(String glId, String glImei, Integer timestamp, Integer temperature, Integer humidity) {
        this.glId = glId;
        this.glImei = glImei;
        this.timestamp = timestamp;
        this.temperature = temperature;
        this.humidity = humidity;
    }

    @Override
    public String toString() {
        return String.format(
                "DeviceAlarmCancel[id=%d, gl_id='%s', gl_imei='%s']",
                id, glId, glImei);
    }

    public String getGlId() {
        return glId;
    }

    public void setGlId(String glId) {
        this.glId = glId;
    }

    public String getGlImei() {
        return glImei;
    }

    public void setGlImei(String glImei) {
        this.glImei = glImei;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
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

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

}
