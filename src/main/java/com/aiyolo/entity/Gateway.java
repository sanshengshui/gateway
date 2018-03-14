package com.aiyolo.entity;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import java.io.Serializable;
import java.util.List;

@Entity
public class Gateway extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String glName = "";
    private String glImei = "";
    private String glPin = "";
    private String glLogintime = "";
    private String glLongitude = "";
    private String glLatitude = "";
    private String userName = "";
    private String userPhones = "";
    private String areaCode = "";
    private String address = "";
    private String village = "";
    private String locationAreaCode = "";
    private String locationAddress = "";
    private String addressLocation = "";
    private Integer probe_status = 0;
    private String report_url = "";
    private Integer report_interval = 540;

    @OneToMany(mappedBy = "gateway")
    private List<Device> devices;

    @OneToMany(mappedBy = "gateway")
    private List<DeviceAlarm> deviceAlarms;

    @OneToMany(mappedBy = "gateway")
    private List<AppUserGateway> gatewayAppUsers;

    protected Gateway() {}

    public Gateway(String glImei, String glPin) {
        this.glImei = glImei;
        this.glPin = glPin;
    }

    @Override
    public String toString() {
        return String.format(
                "Gateway[id=%d, gl_name='%s', gl_imei='%s', gl_pin='%s', user_name='%s', user_phones='%s', area_code='%s', village='%s', address='%s',probe_status=%d,report_url='%s',report_interval=%d]",
                id, glName, glImei, glPin, userName, userPhones, areaCode, village, address, probe_status, report_url, report_interval);
    }


    public String getGlName() {
        return glName;
    }

    public void setGlName(String glName) {
        this.glName = glName;
    }

    public String getGlImei() {
        return glImei;
    }

    public void setGlImei(String glImei) {
        this.glImei = glImei;
    }

    public String getGlPin() {
        return glPin;
    }

    public void setGlPin(String glPin) {
        this.glPin = glPin;
    }

    public String getGlLogintime() {
        return glLogintime;
    }

    public void setGlLogintime(String glLogintime) {
        this.glLogintime = glLogintime;
    }

    public String getGlLongitude() {
        return glLongitude;
    }

    public void setGlLongitude(String glLongitude) {
        if (glLongitude == null || !glLongitude.equals(this.glLongitude)) {
            locationAreaCode = "";
            locationAddress = "";
        }

        this.glLongitude = glLongitude;
    }

    public String getGlLatitude() {
        return glLatitude;
    }

    public void setGlLatitude(String glLatitude) {
        this.glLatitude = glLatitude;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhones() {
        return userPhones;
    }

    public void setUserPhones(String userPhones) {
        this.userPhones = userPhones;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (address == null || !address.equals(this.address)) {
            addressLocation = ""; // 置空地址经纬度信息，进入首页触发重新转换
        }

        this.address = address;
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

    public String getAddressLocation() {
        return addressLocation;
    }

    public void setAddressLocation(String addressLocation) {
        this.addressLocation = addressLocation;
    }

    public Integer getProbe_status() {
        return probe_status;
    }

    public void setProbe_status(Integer probe_status) {
        this.probe_status = probe_status;
    }

    public String getReport_url() {
        return report_url;
    }

    public void setReport_url(String report_url) {
        this.report_url = report_url;
    }

    public Integer getReport_interval() {
        return report_interval;
    }

    public void setReport_interval(Integer report_interval) {
        this.report_interval = report_interval;
    }
}
