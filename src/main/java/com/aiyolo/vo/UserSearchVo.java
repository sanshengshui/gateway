package com.aiyolo.vo;

import com.aiyolo.constant.RoleEnum;
import org.springframework.stereotype.Component;

@Component
public class UserSearchVo extends PageSearchVo {

    private Long channelId = null;
    private String username = "";
    private String realname = "";
    private String phone = "";
    private RoleEnum role = RoleEnum.EMPLOYEE;

    @Override
    public String toString() {
        return String.format(
                "UserSearchVo[channelId=%d, username='%s', realname='%s', phone='%s', role='%s']",
                channelId, username, realname, phone, role.getName());
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

}
