package com.aiyolo.service.api.response;

import com.aiyolo.constant.ApiResponseStateEnum;
import com.aiyolo.entity.DeviceSharePass;
import com.aiyolo.service.api.request.RequestObject;

public class ShareDeviceResponse extends Response {

    private String pass;

    public ShareDeviceResponse() {
    }

    public ShareDeviceResponse(RequestObject request, DeviceSharePass deviceSharePass) {
        super(request.getAction(), ApiResponseStateEnum.SUCCESS);

        this.pass = deviceSharePass.getPass();
    }

    @Override
    public String toString() {
        return super.toString() + ", ShareDeviceResponse{" +
                "pass='" + pass + '\'' +
                '}';
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

}
