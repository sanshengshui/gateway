package com.aiyolo.entity;


import javax.persistence.Entity;

@Entity
public class Checked extends BaseEntity {

    private String imei = "";

    private Integer mid = 0;

    public Checked(String imei, Integer mid) {
        this.imei = imei;
        this.mid = mid;
    }

    @Override
    public String toString() {
        return "Checked{" +
                "imei='" + imei + '\'' +
                ", mid=" + mid +
                "} " + super.toString();
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
}
