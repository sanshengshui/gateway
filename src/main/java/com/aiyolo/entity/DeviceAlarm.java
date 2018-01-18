package com.aiyolo.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity
public class DeviceAlarm extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String type = "";
    private String imei = "";
    private String glImei = "";
    private Integer timestamp = 0;
    private Integer value = 0;
    private Integer value1 = 0;
    private Integer value2 = 0;
    private Integer status = 0;

    @ManyToOne
    @JoinColumn(name = "imei", referencedColumnName = "imei", insertable = false, updatable = false)
    private Device device;

    @ManyToOne
    @JoinColumn(name = "glImei", referencedColumnName = "glImei", insertable = false, updatable = false)
    private Gateway gateway;

    protected DeviceAlarm() {}

    public DeviceAlarm(String type, String imei, String glImei, Integer timestamp, Integer value
            , Integer value1, Integer value2, Integer status) {
        this.type = type;
        this.imei = imei;
        this.glImei = glImei;
        this.timestamp = timestamp;
        this.value = value;
        this.value1 = value1;
        this.value2 = value2;
        this.status = status;
    }

//    public DeviceAlarm(String type, String imei, String glImei, Integer timestamp, Integer value, Integer status) {
//        this.type = type;
//        this.imei = imei;
//        this.glImei = glImei;
//        this.timestamp = timestamp;
//        this.value = value;
//        this.status = status;
//    }

    @Override
    public String toString() {
        return String.format(
                "DeviceAlarm[id=%d, type='%s', imei='%s', gl_imei='%s', timestamp=%d, value=%d" +
                        ", value1=%d, value2=%d, status=%d]",
                id, type, imei, glImei, timestamp, value,value1,value2, status);
    }

    public Integer getValue1() {
        return value1;
    }

    public void setValue1(Integer value1) {
        this.value1 = value1;
    }

    public Integer getValue2() {
        return value2;
    }

    public void setValue2(Integer value2) {
        this.value2 = value2;
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

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Gateway getGateway() {
        return gateway;
    }

    public void setGateway(Gateway gateway) {
        this.gateway = gateway;
    }

}
