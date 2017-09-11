package com.aiyolo.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class DeviceSta extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String glId = "";
    private String glImei = "";
    private Integer mcc = 0;
    private Integer mnc = 0;
    private Integer lac = 0;
    private Integer cell = 0;
    private String staLocation = "";
    private String locationAreaCode = "";
    private String locationAddress = "";

    @ManyToOne
    @JoinColumn(name = "glId", referencedColumnName = "glId", insertable = false, updatable = false)
    private Device device;

    protected DeviceSta() {}

    public DeviceSta(String glId, String glImei, Integer mcc, Integer mnc, Integer lac, Integer cell) {
        this.glId = glId;
        this.glImei = glImei;
        this.mcc = mcc;
        this.mnc = mnc;
        this.lac = lac;
        this.cell = cell;
    }

    @Override
    public String toString() {
        return String.format(
                "DeviceSta[id=%d, gl_id='%s', gl_imei='%s', mcc=%d, mnc=%d, lac=%d, cell=%d]",
                id, glId, glImei, mcc, mnc, lac, cell);
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

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

}
