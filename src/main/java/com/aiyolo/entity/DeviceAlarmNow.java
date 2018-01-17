package com.aiyolo.entity;

import javax.persistence.Entity;

@Entity
public class DeviceAlarmNow extends BaseEntity {

    private String areaCode = "";
    private String imei = "";
    private String glImei = "";
    private String type = "";

    protected DeviceAlarmNow() {}

    public DeviceAlarmNow(String areaCode, String imei, String glImei, String type) {
        this.areaCode = areaCode;
        this.imei = imei;
        this.glImei = glImei;
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format(
                "DeviceAlarmNow[id=%d, areaCode='%s', imei='%s', glImei='%s', type='%s']",
                id, areaCode, imei, glImei, type);
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
