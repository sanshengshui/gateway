package com.aiyolo.service.api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.stereotype.Component;

@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeleteDeviceRequest extends Request {

    private String imeiGateway;
    private String imeiDev = "";

    public DeleteDeviceRequest() {
    }

    @Override
    public String toString() {
        return "DeleteDeviceRequest{" +
                "imeiGateway='" + imeiGateway + '\'' +
                ", imeiDev='" + imeiDev + '\'' +
                '}';
    }

    public String getImeiGateway() {
        return imeiGateway;
    }

    public void setImeiGateway(String imeiGateway) {
        this.imeiGateway = imeiGateway;
    }

    public String getImeiDev() {
        return imeiDev;
    }

    public void setImeiDev(String imeiDev) {
        this.imeiDev = imeiDev;
    }

}
