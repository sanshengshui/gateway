package com.aiyolo.service.api.request;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Component
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocalInfoRequest extends Request {

    private Double latitude;
    private Double longitude;

    public LocalInfoRequest() {
    }

    @Override
    public String toString() {
        return "LocalInfoRequest{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

}
