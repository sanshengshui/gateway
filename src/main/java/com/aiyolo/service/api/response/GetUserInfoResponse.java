package com.aiyolo.service.api.response;

import com.aiyolo.constant.ApiResponseStateEnum;
import com.aiyolo.entity.AppUser;
import com.aiyolo.service.api.request.RequestObject;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GetUserInfoResponse extends Response {

    private String name = "";
    private String phone = "";
    private String avatar = "";
    private String mailAddress = "";

    public GetUserInfoResponse() {
    }

    public GetUserInfoResponse(RequestObject request, AppUser appUser) {
        super(request.getAction(), appUser == null ? ApiResponseStateEnum.ERROR_NONE_DATA : ApiResponseStateEnum.SUCCESS);

        if (appUser != null) {
            this.name = appUser.getName();
            this.phone = appUser.getPhone();
            this.avatar = appUser.getAvatar();
            this.mailAddress = appUser.getMailAddress();
        }
    }

    @Override
    public String toString() {
        return super.toString() + ", GetUserInfoResponse{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", avatar='" + avatar + '\'' +
                ", mail_address='" + mailAddress + '\'' +
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

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

}
