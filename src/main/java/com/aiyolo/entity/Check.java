package com.aiyolo.entity;


import javax.persistence.Entity;

@Entity
public class Check extends BaseEntity {

    private String imei = "";

    private Integer timestamp = 0;

    public Check(String imei, Integer timestamp) {
        this.imei = imei;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Check{" +
                "imei='" + imei + '\'' +
                ", timestamp=" + timestamp +
                "} " + super.toString();
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }
}
