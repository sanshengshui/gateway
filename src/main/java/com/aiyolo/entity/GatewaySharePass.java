package com.aiyolo.entity;

import javax.persistence.Entity;

@Entity
public class GatewaySharePass extends BaseEntity {

    private String glImei;
    private String pass;

    public GatewaySharePass() {}

    public GatewaySharePass(String glImei, String pass) {
        this.glImei = glImei;
        this.pass = pass;
    }

    @Override
    public String toString() {
        return String.format(
                "GatewaySharePass[id=%d, gl_imei='%s', pass='%s']",
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
