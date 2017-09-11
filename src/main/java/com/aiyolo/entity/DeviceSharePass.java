package com.aiyolo.entity;

import javax.persistence.Entity;

@Entity
public class DeviceSharePass extends BaseEntity {

    private String glImei;
    private String pass;

    public DeviceSharePass() {}

    public DeviceSharePass(String glImei, String pass) {
        this.glImei = glImei;
        this.pass = pass;
    }

    @Override
    public String toString() {
        return String.format(
                "DeviceSharePass[id=%d, gl_imei='%s', pass='%s']",
                id, glImei, pass);
    }

    public String getGlImei() {
        return glImei;
    }

    public void setGlImei(String glImei) {
        this.glImei = glImei;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

}
