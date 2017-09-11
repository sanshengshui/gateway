package com.aiyolo.service.api.response;

import java.util.Map;

import com.aiyolo.constant.ApiResponseStateEnum;
import com.aiyolo.service.api.request.RequestObject;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GetDataResponse extends Response {

    private String imei;
    private Integer[] temps;
    private Integer[] hums;
    private Integer alarmType;
    private Integer netStatus;
    private Integer devStatus;
    private Integer temp;
    private Integer hum;

//    public GetDataResponse() {
//    }

    public GetDataResponse(RequestObject request, Map<String, Object> deviceData) {
        super(request.getAction(), ApiResponseStateEnum.SUCCESS);

        this.imei = (String) deviceData.get("imei");
        this.temps = (Integer[]) deviceData.get("temps");
        this.hums = (Integer[]) deviceData.get("hums");
        this.alarmType = (Integer) deviceData.get("alarmType");
        this.netStatus = (Integer) deviceData.get("netStatus");
        this.devStatus = (Integer) deviceData.get("devStatus");
        this.temp = (Integer) deviceData.get("temp");
        this.hum = (Integer) deviceData.get("hum");
    }

    @Override
    public String toString() {
        return super.toString() + ", GetDataResponse{" +
                "imei='" + imei + '\'' +
                ", temps=" + temps +
                ", hums=" + hums +
                ", alarm_type=" + alarmType +
                ", net_status=" + netStatus +
                ", dev_status=" + devStatus +
                ", temp=" + temp +
                ", hum=" + hum +
                '}';
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public Integer[] getTemps() {
        return temps;
    }

    public void setTemps(Integer[] temps) {
        this.temps = temps;
    }

    public Integer[] getHums() {
        return hums;
    }

    public void setHums(Integer[] hums) {
        this.hums = hums;
    }

    public Integer getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(Integer alarmType) {
        this.alarmType = alarmType;
    }

    public Integer getNetStatus() {
        return netStatus;
    }

    public void setNetStatus(Integer netStatus) {
        this.netStatus = netStatus;
    }

    public Integer getDevStatus() {
        return devStatus;
    }

    public void setDevStatus(Integer devStatus) {
        this.devStatus = devStatus;
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

}
