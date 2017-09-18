package com.aiyolo.service.api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.stereotype.Component;

@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModifyPositionRequest extends Request {

    private String imei;
    private String position;

    public ModifyPositionRequest() {
    }

    @Override
    public String toString() {
        return "ModifyPositionRequest{" +
                "imei='" + imei + '\'' +
                "position='" + position + '\'' +
                '}';
    }


    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

}
