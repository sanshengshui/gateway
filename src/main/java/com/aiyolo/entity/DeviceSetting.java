package com.aiyolo.entity;

import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
public class DeviceSetting extends BaseEntity {

    private String glImei = "";
    private String areaCode = "";

    @Min(0)
    @Max(80)
    private Integer oneLevelAlarmTemperature = 45;

    @Min(0)
    @Max(80)
    private Integer twoLevelAlarmTemperature = 55;

    protected DeviceSetting() {}

    public DeviceSetting(String glImei, String areaCode, Integer oneLevelAlarmTemperature, Integer twoLevelAlarmTemperature) {
        this.glImei = glImei;
        this.areaCode = areaCode;
        this.oneLevelAlarmTemperature = oneLevelAlarmTemperature;
        this.twoLevelAlarmTemperature = twoLevelAlarmTemperature;
    }

    @Override
    public String toString() {
        return String.format(
                "DeviceSetting[id=%d, gl_imei='%s', area_code='%s', one_level_alarm_temperature=%d, two_level_alarm_temperature=%d]",
                id, glImei, areaCode, oneLevelAlarmTemperature, twoLevelAlarmTemperature);
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

    public Integer getOneLevelAlarmTemperature() {
        return oneLevelAlarmTemperature;
    }

    public void setOneLevelAlarmTemperature(Integer oneLevelAlarmTemperature) {
        this.oneLevelAlarmTemperature = oneLevelAlarmTemperature;
    }

    public Integer getTwoLevelAlarmTemperature() {
        return twoLevelAlarmTemperature;
    }

    public void setTwoLevelAlarmTemperature(Integer twoLevelAlarmTemperature) {
        this.twoLevelAlarmTemperature = twoLevelAlarmTemperature;
    }

}
