package com.aiyolo.vo;

import com.aiyolo.constant.RoleEnum;
import org.springframework.stereotype.Component;

@Component
public class ChannelSearchVo extends PageSearchVo {

    private String channelName = "";
    private String username = "";
    private String realname = "";
    private String phone = "";
    private RoleEnum role = RoleEnum.AGENT;

    @Override
    public String toString() {
        return String.format(
                "ChannelSearchVo[channelName='%s', username='%s', realname='%s', phone='%s', role='%s']",
                channelName, username, realname, phone, role.getName());
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
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
