package com.aiyolo.service.api.request;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminTransferRequest extends Request {

    private String imei;
    private String phone;

    public AdminTransferRequest() {
    }

    @Override
    public String toString() {
        return "AdminTransferRequest{" +
                "imei='" + imei + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
