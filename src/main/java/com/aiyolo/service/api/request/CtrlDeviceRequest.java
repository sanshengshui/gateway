package com.aiyolo.service.api.request;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@Component
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CtrlDeviceRequest extends Request {

    private String imei;
    private Integer warnVoice;
    private Integer gasSwitch;

    public CtrlDeviceRequest() {
    }

    @Override
    public String toString() {
        return "CtrlDeviceRequest{" +
                "imei='" + imei + '\'' +
                ", warn_voice=" + warnVoice +
                ", gas_switch=" + gasSwitch +
                '}';
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public Integer getWarnVoice() {
        return warnVoice;
    }

    public void setWarnVoice(Integer warnVoice) {
        this.warnVoice = warnVoice;
    }

    public Integer getGasSwitch() {
        return gasSwitch;
    }

    public void setGasSwitch(Integer gasSwitch) {
        this.gasSwitch = gasSwitch;
    }

}
