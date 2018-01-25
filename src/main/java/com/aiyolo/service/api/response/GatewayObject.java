package com.aiyolo.service.api.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GatewayObject {

    private String imei = "";
    private Integer online = 0;
    private Integer devNum = 0;
    private String glName = "";
    private String ver = "";
    private Integer admin = 0;
    private Integer err = 0;

    public GatewayObject() {
    }

    public GatewayObject(String imei, Integer online, Integer devNum, String glName, String ver, Integer admin
            , Integer err) {
        this.imei = imei;
        this.online = online;
        this.devNum = devNum;
        this.glName = glName;
        this.ver = ver;
        this.admin = admin;
        this.err = err;
    }

    @Override
    public String toString() {
        return "GatewayObject{" +
                "imei='" + imei + '\'' +
                ", online=" + online +
                ", dev_num=" + devNum +
                ", gl_name='" + glName + '\'' +
                ", ver='" + ver + '\'' +
                ", admin=" + admin +
                ", err=" + err +
                '}';
    }

    public Integer getErr() {
        return err;
    }

    public void setErr(Integer err) {
        this.err = err;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public Integer getOnline() {
        return online;
    }

    public void setOnline(Integer online) {
        this.online = online;
    }

    public Integer getDevNum() {
        return devNum;
    }

    public void setDevNum(Integer devNum) {
        this.devNum = devNum;
    }

    public String getGlName() {
        return glName;
    }

    public void setGlName(String glName) {
        this.glName = glName;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public Integer getAdmin() {
        return admin;
    }

    public void setAdmin(Integer admin) {
        this.admin = admin;
    }

}
