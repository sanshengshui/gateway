package com.aiyolo.entity;

import javax.persistence.Entity;

@Entity
public class DeviceAlarmStat extends BaseEntity {

    private String date;
    private String areaCode = "";
    private String type = "";
    private Integer num = 0;

    protected DeviceAlarmStat() {}

    public DeviceAlarmStat(String date, String areaCode, String type, Integer num) {
        this.date = date;
        this.areaCode = areaCode;
        this.type = type;
        this.num = num;
    }

    @Override
    public String toString() {
        return String.format(
                "DeviceAlarmStat[id=%d, date='%s', area_code='%s', type='%s', num=%d]",
                id, date, areaCode, type, num);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
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
