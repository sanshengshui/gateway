package com.aiyolo.service.api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.stereotype.Component;

@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class WifiConfigureRequest extends Request {

    private String imei;
    private String ssid = "";
    private String pass = "";

    public WifiConfigureRequest() {
    }

    @Override
    public String toString() {
        return "WifiConfigureRequest{" +
                "imei='" + imei + '\'' +
                ", ssid='" + ssid + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

}
