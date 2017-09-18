package com.aiyolo.service.api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.stereotype.Component;

@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetPositionRequest extends Request {

    private String imei;

    public GetPositionRequest() {
    }

    @Override
    public String toString() {
        return "GetPositionRequest{" +
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
