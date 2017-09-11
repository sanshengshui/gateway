package com.aiyolo.service.api.request;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShareDeviceRequest extends Request {

    private String imei;

    public ShareDeviceRequest() {
    }

    @Override
    public String toString() {
        return "ShareDeviceRequest{" +
                "imei='" + imei + '\'' +
                '}';
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

}
