package com.aiyolo.service.api.response;

import com.aiyolo.constant.ApiResponseStateEnum;
import com.aiyolo.entity.GatewaySharePass;
import com.aiyolo.service.api.request.RequestObject;

public class ShareGatewayResponse extends Response {

    private String pass;

    public ShareGatewayResponse() {
    }

    public ShareGatewayResponse(RequestObject request, GatewaySharePass gatewaySharePass) {
        super(request.getAction(), ApiResponseStateEnum.SUCCESS);

        this.pass = gatewaySharePass.getPass();
    }

    @Override
    public String toString() {
        return super.toString() + ", ShareGatewayResponse{" +
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
