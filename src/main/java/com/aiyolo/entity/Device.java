package com.aiyolo.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.List;

@Entity
public class Device extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String type = "";
    private String pid = "";
    private String imei = "";
    private String glImei = "";
    private String name = "";
    private String position = "";

    @ManyToOne
    @JoinColumn(name = "glImei", referencedColumnName = "glImei", insertable = false, updatable = false)
    private Gateway gateway;

    @OneToMany(mappedBy = "device")
    private List<DeviceAlarm> deviceAlarms;

    protected Device() {}

    public Device(String type, String pid, String imei, String glImei) {
        this.type = type;
        this.pid = pid;
        this.imei = imei;
        this.glImei = glImei;
    }

    @Override
    public String toString() {
        return String.format(
                "Device[id=%d, type='%s', pid='%s', imei='%s', gl_imei='%s', name='%s', position='%s']",
                id, type, pid, imei, glImei, name, position);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Gateway getGateway() {
        return gateway;
    }

    public void setGateway(Gateway gateway) {
        this.gateway = gateway;
    }

}
