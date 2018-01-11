package com.aiyolo.service.api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.stereotype.Component;

@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListAlarmRequest extends Request {

    private String imei;

    private int type;

    public ListAlarmRequest() {
    }

    @Override
    public String toString() {
        return "ListAlarmRequest{" +
                "imei='" + imei + '\'' +
                "type='" + type + '\'' +
                '}';
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

}
