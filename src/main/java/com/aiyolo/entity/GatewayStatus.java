package com.aiyolo.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import java.io.Serializable;

@Entity
public class GatewayStatus extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

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
    private Integer sos = 0;
    private Integer checked = 0;
    private Integer htmp = 0;
    private Integer needcfg = 0;

    @ManyToOne
    @JoinColumn(name = "glImei", referencedColumnName = "glImei", insertable = false, updatable = false)
    private Gateway gateway;

    protected GatewayStatus() {}

    public GatewayStatus(String glImei, Integer timestamp, String date, String hour
            , Integer rssi, Integer temperature, Integer humidity, Integer atmosphere, String version
            , Integer status, Integer sos, Integer checked, Integer htmp, Integer needcfg) {
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
        this.sos = sos;
        this.checked = checked;
        this.htmp = htmp;
        this.needcfg = needcfg;
    }

    public String toString1() {
        return "{" +
                "date='" + date + '\'' +
                ", hour='" + hour + '\'' +
                ", rssi=" + rssi +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", atmosphere=" + atmosphere +
                ", version='" + version + '\'' +
                ", status=" + status +
                ", sos=" + sos +
                ", checked=" + checked +
                ", htmp=" + htmp +
                "} ";
    }

    @Override
    public String toString() {
        return String.format(
                "GatewayStatus[id=%d,  gl_imei='%s', date='%s', hour='%s']",
                id, glImei, date, hour);
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

    public Integer getSos() {
        return sos;
    }

    public void setSos(Integer sos) {
        this.sos = sos;
    }

    public Integer getChecked() {
        return checked;
    }

    public void setChecked(Integer checked) {
        this.checked = checked;
    }

    public Integer getHtmp() {
        return htmp;
    }

    public void setHtmp(Integer htmp) {
        this.htmp = htmp;
    }

    public Gateway getGateway() {
        return gateway;
    }

    public void setGateway(Gateway gateway) {
        this.gateway = gateway;
    }

}
