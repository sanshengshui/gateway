package com.aiyolo.service.api.response;

import com.aiyolo.constant.ApiResponseStateEnum;
import com.aiyolo.service.api.request.RequestObject;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;
import java.util.Map;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GatewayDetailResponse extends Response {

    private String imei = "";
    private List<DeviceObject> devs;
    private Integer err = 0;
    private Integer online = 0;
    private Integer temp = 0;
    private Integer hum = 0;
    private Integer atm = 0;
    private String ver = "";

    public GatewayDetailResponse(RequestObject request, Map<String, Object> gatewayDetail) {
        super(request.getAction(), ApiResponseStateEnum.SUCCESS);

        this.imei = (String) gatewayDetail.get("imei");
        this.devs = (List<DeviceObject>) gatewayDetail.get("devs");
        this.err = (Integer) gatewayDetail.get("err");
        this.online = (Integer) gatewayDetail.get("online");
        this.temp = (Integer) gatewayDetail.get("temp");
        this.hum = (Integer) gatewayDetail.get("hum");
        this.atm = (Integer) gatewayDetail.get("atm");
        this.ver = (String) gatewayDetail.get("ver");
    }

    @Override
    public String toString() {
        return super.toString() + ", GatewayDetailResponse{" +
                "imei='" + imei + '\'' +
                "devs='" + devs.toString() + '\'' +
                '}';
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public List<DeviceObject> getDevs() {
        return devs;
    }

    public void setDevs(List<DeviceObject> devs) {
        this.devs = devs;
    }

    public Integer getErr() {
        return err;
    }

    public void setErr(Integer err) {
        this.err = err;
    }

    public Integer getOnline() {
        return online;
    }

    public void setOnline(Integer online) {
        this.online = online;
    }

    public Integer getTemp() {
        return temp;
    }

    public void setTemp(Integer temp) {
        this.temp = temp;
    }

    public Integer getHum() {
        return hum;
    }

    public void setHum(Integer hum) {
        this.hum = hum;
    }

    public Integer getAtm() {
        return atm;
    }

    public void setAtm(Integer atm) {
        this.atm = atm;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

}
