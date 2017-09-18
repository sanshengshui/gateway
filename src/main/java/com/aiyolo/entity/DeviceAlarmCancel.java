package com.aiyolo.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity
public class DeviceAlarmCancel extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String type = "";
    private String imei = "";
    private String glImei = "";
    private Integer timestamp = 0;

    @ManyToOne
    @JoinColumn(name = "imei", referencedColumnName = "imei", insertable = false, updatable = false)
    private Device device;

    @ManyToOne
    @JoinColumn(name = "glImei", referencedColumnName = "glImei", insertable = false, updatable = false)
    private Gateway gateway;

    protected DeviceAlarmCancel() {}

    public DeviceAlarmCancel(String type, String imei, String glImei, Integer timestamp) {
        this.type = type;
        this.imei = imei;
        this.glImei = glImei;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return String.format(
                "DeviceAlarmCancel[id=%d, type='%s', imei='%s', gl_imei='%s', timestamp=%d]",
                id, type, imei, glImei, timestamp);
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

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

}
