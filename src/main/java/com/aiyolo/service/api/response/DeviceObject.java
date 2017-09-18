package com.aiyolo.service.api.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DeviceObject {

    private String dev = "";
    private String pid = "";
    private String location = "";
    private String name = "";
    private Integer online = 0;
    private String imei = "";
    private Integer rssi = 0;
    private Integer err = 0;
    private Integer val = 0;
    private Integer bat = 0;

    public DeviceObject() {
    }

    public DeviceObject(String dev, String pid, String location, String name, Integer online, String imei,
                        Integer rssi, Integer err, Integer val, Integer bat) {
        this.dev = dev;
        this.pid = pid;
        this.location = location;
        this.name = name;
        this.online = online;
        this.imei = imei;
        this.rssi = rssi;
        this.err = err;
        this.val = val;
        this.bat = bat;
    }

    @Override
    public String toString() {
        return "DeviceObject{" +
                "dev='" + dev + '\'' +
                ", pid='" + pid + '\'' +
                ", imei='" + imei + '\'' +
                '}';
    }

    public String getDev() {
        return dev;
    }

    public void setDev(String dev) {
        this.dev = dev;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOnline() {
        return online;
    }

    public void setOnline(Integer online) {
        this.online = online;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public Integer getRssi() {
        return rssi;
    }

    public void setRssi(Integer rssi) {
        this.rssi = rssi;
    }

    public Integer getErr() {
        return err;
    }

    public void setErr(Integer err) {
        this.err = err;
    }

    public Integer getVal() {
        return val;
    }

    public void setVal(Integer val) {
        this.val = val;
    }

    public Integer getBat() {
        return bat;
    }

    public void setBat(Integer bat) {
        this.bat = bat;
    }

}
