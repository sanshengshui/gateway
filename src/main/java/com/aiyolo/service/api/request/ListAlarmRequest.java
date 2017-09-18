package com.aiyolo.service.api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.stereotype.Component;

@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListAlarmRequest extends Request {

    private String imei;

    public ListAlarmRequest() {
    }

    @Override
    public String toString() {
        return "ListAlarmRequest{" +
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
