package com.aiyolo.vo;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class DeviceAlarmSearchVo extends PageSearchVo {

    private String glImei = "";
    private String areaCode = "0";
    private String village = "";
    private String address = "";
    private Integer status = -1;

    @Override
    public String toString() {
        return String.format(
                "DeviceAlarmSearchVo[glImei='%s', areaCode='%s', village='%s', address='%s', status=%d]",
                glImei, areaCode, village, address, status);
    }

    public String getGlImei() {
        return glImei;
    }

    public void setGlImei(String glImei) {
        this.glImei = glImei;
    }

    public String getAreaCode() {
        return StringUtils.isNotEmpty(areaCode) ? areaCode : "0";
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
