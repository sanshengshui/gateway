package com.aiyolo.service.api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.stereotype.Component;

@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeleteGatewayRequest extends Request {

    private String imei;
    private String phone = "";

    public DeleteGatewayRequest() {
    }

    @Override
    public String toString() {
        return "DeleteGatewayRequest{" +
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
