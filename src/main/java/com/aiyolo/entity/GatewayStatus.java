package com.aiyolo.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity
public class GatewayStatus extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String glId = "";
    private String glImei = "";
    private Integer timestamp = 0;
    private String date = "";
    private String hour = "";
    private Integer rssi = 0;
    private Integer temperature = 0;
    private Integer humidity = 0;
    private Integer atmosphere = 0;
    private String version = "";
    private Integer status = 0;

    @ManyToOne
    @JoinColumn(name = "glId", referencedColumnName = "glId", insertable = false, updatable = false)
    private Gateway gateway;

    protected GatewayStatus() {}

    public GatewayStatus(String glId, String glImei, Integer timestamp, String date, String hour, Integer rssi, Integer temperature, Integer humidity,
            Integer atmosphere, String version, Integer status) {
        this.glId = glId;
        this.glImei = glImei;
        this.timestamp = timestamp;
        this.date = date;
        this.hour = hour;
        this.rssi = rssi;
        this.temperature = temperature;
        this.humidity = humidity;
        this.atmosphere = atmosphere;
        this.version = version;
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format(
                "GatewayStatus[id=%d, gl_id='%s', gl_imei='%s', date='%s', hour='%s']",
                id, glId, glImei, date, hour);
    }

    public String getGlId() {
        return glId;
    }

    public void setGlId(String glId) {
        this.glId = glId;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public Integer getRssi() {
        return rssi;
    }

    public void setRssi(Integer rssi) {
        this.rssi = rssi;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public Integer getAtmosphere() {
        return atmosphere;
    }

    public void setAtmosphere(Integer atmosphere) {
        this.atmosphere = atmosphere;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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
