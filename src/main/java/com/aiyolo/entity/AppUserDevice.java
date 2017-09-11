package com.aiyolo.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.aiyolo.constant.Role4DeviceEnum;

@Entity
public class AppUserDevice extends BaseEntity {

    private String userId;
    private String glImei;

    @Enumerated(EnumType.STRING)
    private Role4DeviceEnum role;

    @ManyToOne
    @JoinColumn(name = "glImei", referencedColumnName = "glImei", insertable = false, updatable = false)
    private Device device;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId", insertable = false, updatable = false)
    private AppUser appUser;

    public AppUserDevice() {}

    public AppUserDevice(String userId, String glImei, Role4DeviceEnum role) {
        this.userId = userId;
        this.glImei = glImei;
        this.role = role;
    }

    @Override
    public String toString() {
        return String.format(
                "AppUserDevice[id=%d, user_id='%s', gl_imei='%s', role='%s']",
                id, userId, glImei, role.getName());
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGlImei() {
        return glImei;
    }

    public void setGlImei(String glImei) {
        this.glImei = glImei;
    }

    public Role4DeviceEnum getRole() {
        return role;
    }

    public void setRole(Role4DeviceEnum role) {
        this.role = role;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

}
