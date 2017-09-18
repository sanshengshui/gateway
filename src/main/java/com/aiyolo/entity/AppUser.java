package com.aiyolo.entity;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.List;

@Entity
public class AppUser extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId = "";
    private String name = "";
    private String phone = "";
    private String avatar = "";
    private String mailAddress = "";

    @OneToMany(mappedBy = "appUser")
    private List<AppUserGateway> appUserGateways;

    protected AppUser() {}

    public AppUser(String userId, String avatar) {
        this.userId = userId;
        this.avatar = avatar;

    }

    public AppUser(String userId, String name, String phone, String avatar, String mailAddress) {
        this.userId = userId;
        this.name = name;
        this.phone = phone;
        this.avatar = avatar;
        this.mailAddress = mailAddress;
    }

    @Override
    public String toString() {
        return String.format(
                "AppUser[id=%d, user_id='%s', name='%s', phone='%s', avatar='%s', mail_address='%s']",
                id, userId, name, phone, avatar, mailAddress);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

}
