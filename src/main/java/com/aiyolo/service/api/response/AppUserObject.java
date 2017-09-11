package com.aiyolo.service.api.response;

import com.aiyolo.constant.Role4DeviceEnum;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AppUserObject {

    private String name = "";
    private String phone = "";
    private String avatar = "";
    private Integer admin = Role4DeviceEnum.USER.getValue();

    public AppUserObject() {
    }

    public AppUserObject(String name, String phone, String avatar, Integer admin) {
        this.name = name;
        this.phone = phone;
        this.avatar = avatar;
        this.admin = admin;
    }

    @Override
    public String toString() {
        return "AppUserObject{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", avatar='" + avatar + '\'' +
                ", admin=" + admin +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getAdmin() {
        return admin;
    }

    public void setAdmin(Integer admin) {
        this.admin = admin;
    }

}
