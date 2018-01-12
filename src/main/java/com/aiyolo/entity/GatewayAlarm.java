package com.aiyolo.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class GatewayAlarm extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String type = "";
    private String imei = "";
    private String glImei = "";
    private Integer timestamp = 0;
    private Integer value = 0;
    private Integer status = 0;

    @ManyToOne
    @JoinColumn(name = "glImei", referencedColumnName = "glImei", insertable = false, updatable = false)
    private Gateway gateway;

    protected GatewayAlarm() {}

    public GatewayAlarm(String type, String imei, String glImei, Integer timestamp, Integer value, Integer status) {
        this.type = type;
        this.imei = imei;
        this.glImei = glImei;
        this.timestamp = timestamp;
        this.value = value;
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format(
                "GatewayAlarm[id=%d, type='%s', imei='%s', gl_imei='%s', timestamp=%d, value=%d, status=%d]",
                id, type, imei, glImei, timestamp, value, status);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getGlImei() {
        return glImei;
    }

    public void setGlImei(String glImei) {
        this.glImei = glImei;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public Gateway getGateway() {
        return gateway;
    }

    public void setGateway(Gateway gateway) {
        this.gateway = gateway;
    }

}
