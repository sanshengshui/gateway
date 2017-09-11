package com.aiyolo.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class DeviceStatus extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String glId = "";
    private String glImei = "";
    private Integer timestamp = 0;
    private String date = "";
    private String hour = "";
    private String version = "";
    private Integer temperature = 0;
    private Integer humidity = 0;
    private Integer netStatus = 0;
    private Integer devStatus = 0;

    @ManyToOne
    @JoinColumn(name = "glId", referencedColumnName = "glId", insertable = false, updatable = false)
    private Device device;

    protected DeviceStatus() {}

    public DeviceStatus(String glId, String glImei, Integer timestamp, String date, String hour, String version, Integer temperature, Integer humidity,
            Integer netStatus, Integer devStatus) {
        this.glId = glId;
        this.glImei = glImei;
        this.timestamp = timestamp;
        this.date = date;
        this.hour = hour;
        this.version = version;
        this.temperature = temperature;
        this.humidity = humidity;
        this.netStatus = netStatus;
        this.devStatus = devStatus;
    }

    @Override
    public String toString() {
        return String.format(
                "DeviceStatus[id=%d, gl_id='%s', gl_imei='%s', date='%s', hour='%s']",
                id, glId, glImei, date, hour);
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
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

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

}
