package com.aiyolo.entity;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.List;

@Entity
public class Gateway extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String glId = "";
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
    private String locationAreaCode = "";
    private String locationAddress = "";
    private String addressLocation = "";

    @OneToMany(mappedBy = "gateway")
    private List<Device> devices;

    @OneToMany(mappedBy = "gateway")
    private List<DeviceAlarm> deviceAlarms;

    @OneToMany(mappedBy = "gateway")
    private List<AppUserGateway> gatewayAppUsers;

    protected Gateway() {}

    public Gateway(String glId, String glImei, String glPin) {
        this.glId = glId;
        this.glImei = glImei;
        this.glPin = glPin;
    }

    @Override
    public String toString() {
        return String.format(
                "Gateway[id=%d, gl_id='%s', gl_name='%s', gl_imei='%s', gl_pin='%s', user_name='%s', user_phones='%s', area_code='%s', address='%s']",
                id, glId, glName, glImei, glPin, userName, userPhones, areaCode, address);
    }

    public String getGlId() {
        return glId;
    }

    public void setGlId(String glId) {
        this.glId = glId;
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

}
