package com.aiyolo.service.api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.stereotype.Component;

@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class BindDeviceRequest extends Request {

    private String imei;
    private String pass = "";

    public BindDeviceRequest() {
    }

    @Override
    public String toString() {
        return "BindDeviceRequest{" +
                "imei='" + imei + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

}
