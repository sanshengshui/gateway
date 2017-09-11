package com.aiyolo.service.api.request;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserListRequest extends Request {

    private String imei;

    public UserListRequest() {
    }

    @Override
    public String toString() {
        return "UserListRequest{" +
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
