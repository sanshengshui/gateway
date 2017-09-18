package com.aiyolo.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity
public class DeviceStatus extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String type = "";
    private String imei = "";
    private String glImei = "";
    private Integer timestamp = 0;
    private Integer online = 0;
    private Integer status = 0;
    private Integer rssi = 0;
    private Integer bat = 0;
    private String info = "";

    @ManyToOne
    @JoinColumn(name = "imei", referencedColumnName = "imei", insertable = false, updatable = false)
    private Device device;

    protected DeviceStatus() {}

    public DeviceStatus(String type, String imei, String glImei, Integer timestamp, Integer online, Integer status, Integer rssi,
                        Integer bat, String info) {
        this.type = type;
        this.imei = imei;
        this.glImei = glImei;
        this.timestamp = timestamp;
        this.online = online;
        this.status = status;
        this.rssi = rssi;
        this.bat = bat;
        this.info = info;
    }

    @Override
    public String toString() {
        return String.format(
                "DeviceStatus[id=%d, type='%s', imei='%s', gl_imei='%s']",
                id, type, imei, glImei);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
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

    public Integer getOnline() {
        return online;
    }

    public void setOnline(Integer online) {
        this.online = online;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getRssi() {
        return rssi;
    }

    public void setRssi(Integer rssi) {
        this.rssi = rssi;
    }

    public Integer getBat() {
        return bat;
    }

    public void setBat(Integer bat) {
        this.bat = bat;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

}
