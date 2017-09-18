package com.aiyolo.service.api.response;

import com.aiyolo.constant.Role4GatewayEnum;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AppUserObject {

    private String phone = "";
    private Integer admin = Role4GatewayEnum.USER.getValue();

    public AppUserObject() {
    }

    public AppUserObject(String phone, Integer admin) {
        this.phone = phone;
        this.admin = admin;
    }

    @Override
    public String toString() {
        return "AppUserObject{" +
                ", phone='" + phone + '\'' +
                ", admin=" + admin +
                '}';
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getAdmin() {
        return admin;
    }

    public void setAdmin(Integer admin) {
        this.admin = admin;
    }

}
