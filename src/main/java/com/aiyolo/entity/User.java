package com.aiyolo.entity;

import com.aiyolo.constant.RoleEnum;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
public class User extends BaseEntity {

    public static final Integer STATUS_ENABLE = 1;
    public static final Integer STATUS_DISABLE = 0;

    private Long channelId = 0L;

    @Size(min=3, max=20)
    private String username;

    private String realname = "";
    private String phone = "";
    private String password = "";

    @Enumerated(EnumType.STRING)
    private RoleEnum role = RoleEnum.EMPLOYEE;

    private String productIds = "";
    private String deviceAreaCodes = "";
    private Integer status = STATUS_ENABLE;

    @ManyToOne
    @JoinColumn(name = "channelId", referencedColumnName = "id", insertable = false, updatable = false)
    private Channel channel;

    public User() {}

    public User(Long channelId, String username, String realname, String phone, String password, RoleEnum role, String productIds, String deviceAreaCodes) {
        this.channelId = channelId;
        this.username = username;
        this.realname = realname;
        this.phone = phone;
        this.password = password;
        this.role = role;
        this.productIds = productIds;
        this.deviceAreaCodes = deviceAreaCodes;
    }

    @Override
    public String toString() {
        return String.format(
                "User[id=%d, channel_id=%d, username='%s', realname='%s', phone='%s', password='%s', role='%s', product_ids='%s', device_area_codes='%s', status=%d]",
                id, channelId, username, realname, phone, password, role.getName(), productIds, deviceAreaCodes, status);
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    public String getProductIds() {
        return productIds;
    }

    public void setProductIds(String productIds) {
        this.productIds = productIds;
    }

    public String getDeviceAreaCodes() {
        return deviceAreaCodes;
    }

    public void setDeviceAreaCodes(String deviceAreaCodes) {
        this.deviceAreaCodes = deviceAreaCodes;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

}
