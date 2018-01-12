package com.aiyolo.entity;

import javax.persistence.Entity;

@Entity
public class DeviceAlarmStat extends BaseEntity {

    private String date;
    private String glImei = "";
    private String areaCode = "";
    private String village = "";
    private String type = "";
    private Integer num = 0;

    protected DeviceAlarmStat() {}

    public DeviceAlarmStat(String date, String glImei, String areaCode, String village, String type, Integer num) {
        this.date = date;
        this.glImei = glImei;
        this.areaCode = areaCode;
        this.village = village;
        this.type = type;
        this.num = num;
    }

    @Override
    public String toString() {
        return String.format(
                "DeviceAlarmStat[id=%d, date='%s', gl_imei='%s', area_code='%s', village='%s', type='%s', num=%d]",
                id, date, glImei, areaCode, village, type, num);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGlImei() {
        return glImei;
    }

    public void setGlImei(String glImei) {
        this.glImei = glImei;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

}
