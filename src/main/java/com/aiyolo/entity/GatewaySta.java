package com.aiyolo.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import java.io.Serializable;

@Entity
public class GatewaySta extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String glImei = "";
    private Integer mcc = 0;
    private Integer mnc = 0;
    private Integer lac = 0;
    private Integer cell = 0;
    private String staLocation = "";
    private String locationAreaCode = "";
    private String locationAddress = "";

    @ManyToOne
    @JoinColumn(name = "glImei", referencedColumnName = "glImei", insertable = false, updatable = false)
    private Gateway gateway;

    protected GatewaySta() {}

    public GatewaySta(String glImei, Integer mcc, Integer mnc, Integer lac, Integer cell) {
        this.glImei = glImei;
        this.mcc = mcc;
        this.mnc = mnc;
        this.lac = lac;
        this.cell = cell;
    }

    @Override
    public String toString() {
        return String.format(
                "GatewaySta[id=%d, gl_imei='%s', mcc=%d, mnc=%d, lac=%d, cell=%d]",
                id, glImei, mcc, mnc, lac, cell);
    }


    public String getGlImei() {
        return glImei;
    }

    public void setGlImei(String glImei) {
        this.glImei = glImei;
    }

    public Integer getMcc() {
        return mcc;
    }

    public void setMcc(Integer mcc) {
        this.mcc = mcc;
    }

    public Integer getMnc() {
        return mnc;
    }

    public void setMnc(Integer mnc) {
        this.mnc = mnc;
    }

    public Integer getLac() {
        return lac;
    }

    public void setLac(Integer lac) {
        this.lac = lac;
    }

    public Integer getCell() {
        return cell;
    }

    public void setCell(Integer cell) {
        this.cell = cell;
    }

    public String getStaLocation() {
        return staLocation;
    }

    public void setStaLocation(String staLocation) {
        this.staLocation = staLocation;
    }

    public String getLocationAreaCode() {
        return locationAreaCode;
    }

    public void setLocationAreaCode(String locationAreaCode) {
        this.locationAreaCode = locationAreaCode;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public Gateway getGateway() {
        return gateway;
    }

    public void setGateway(Gateway gateway) {
        this.gateway = gateway;
    }

}
