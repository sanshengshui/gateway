package com.aiyolo.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class DeviceAlarm extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String glId = "";
    private String glImei = "";
    private Integer timestamp = 0;
    private Integer type = 0;
    private Integer status = 0;
    private Integer temperature = 0;
    private Integer humidity = 0;

    @ManyToOne
    @JoinColumn(name = "glId", referencedColumnName = "glId", insertable = false, updatable = false)
    private Device device;

    protected DeviceAlarm() {}

    public DeviceAlarm(String glId, String glImei, Integer timestamp, Integer type, Integer status, Integer temperature, Integer humidity) {
        this.glId = glId;
        this.glImei = glImei;
        this.timestamp = timestamp;
        this.type = type;
        this.status = status;
        this.temperature = temperature;
        this.humidity = humidity;
    }

    @Override
    public String toString() {
        return String.format(
                "DeviceAlarm[id=%d, gl_id='%s', gl_imei='%s', type=%d, status=%d]",
                id, glId, glImei, type, status);
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
