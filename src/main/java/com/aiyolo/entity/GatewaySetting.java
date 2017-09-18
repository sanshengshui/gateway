package com.aiyolo.entity;

import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
public class GatewaySetting extends BaseEntity {

    private String glImei = "";
    private String areaCode = "";

    @Min(0)
    @Max(80)
    private Integer alarmTemperature = 55;

    protected GatewaySetting() {}

    public GatewaySetting(String glImei, String areaCode, Integer alarmTemperature) {
        this.glImei = glImei;
        this.areaCode = areaCode;
        this.alarmTemperature = alarmTemperature;
    }

    @Override
    public String toString() {
        return String.format(
                "GatewaySetting[id=%d, gl_imei='%s', area_code='%s', alarm_temperature=%d]",
                id, glImei, areaCode, alarmTemperature);
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

    public Integer getAlarmTemperature() {
        return alarmTemperature;
    }

    public void setAlarmTemperature(Integer alarmTemperature) {
        this.alarmTemperature = alarmTemperature;
    }

}
