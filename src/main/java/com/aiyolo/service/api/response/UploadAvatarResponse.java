package com.aiyolo.service.api.response;

import com.aiyolo.constant.ApiResponseStateEnum;
import com.aiyolo.entity.AppUser;
import com.aiyolo.service.api.request.RequestObject;

public class UploadAvatarResponse extends Response {

    private String avatar;

    public UploadAvatarResponse() {
    }

    public UploadAvatarResponse(RequestObject request, AppUser appUser) {
        super(request.getAction(), ApiResponseStateEnum.SUCCESS);

        this.avatar = appUser.getAvatar();
    }

    @Override
    public String toString() {
        return super.toString() + ", UploadAvatarResponse{" +
                "avatar='" + avatar + '\'' +
                '}';
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}
