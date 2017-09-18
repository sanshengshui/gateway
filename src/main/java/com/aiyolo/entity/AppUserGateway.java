package com.aiyolo.entity;

import com.aiyolo.constant.Role4GatewayEnum;

import javax.persistence.*;

@Entity
public class AppUserGateway extends BaseEntity {

    private String userId;
    private String glImei;

    @Enumerated(EnumType.STRING)
    private Role4GatewayEnum role;

    @ManyToOne
    @JoinColumn(name = "glImei", referencedColumnName = "glImei", insertable = false, updatable = false)
    private Gateway gateway;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId", insertable = false, updatable = false)
    private AppUser appUser;

    public AppUserGateway() {}

    public AppUserGateway(String userId, String glImei, Role4GatewayEnum role) {
        this.userId = userId;
        this.glImei = glImei;
        this.role = role;
    }

    @Override
    public String toString() {
        return String.format(
                "AppUserGateway[id=%d, user_id='%s', gl_imei='%s', role='%s']",
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

    public Role4GatewayEnum getRole() {
        return role;
    }

    public void setRole(Role4GatewayEnum role) {
        this.role = role;
    }

    public Gateway getGateway() {
        return gateway;
    }

    public void setGateway(Gateway gateway) {
        this.gateway = gateway;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

}
