package com.aiyolo.service.api.response;

import com.aiyolo.constant.ApiResponseStateEnum;
import com.aiyolo.entity.Device;
import com.aiyolo.service.api.request.RequestObject;

public class GetPositionResponse extends Response {

    private String position;

    public GetPositionResponse() {
    }

    public GetPositionResponse(RequestObject request, Device device) {
        super(request.getAction(), ApiResponseStateEnum.SUCCESS);

        this.position = device.getPosition();
    }

    @Override
    public String toString() {
        return super.toString() + ", GetPositionResponse{" +
                "position='" + position + '\'' +
                '}';
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

}
