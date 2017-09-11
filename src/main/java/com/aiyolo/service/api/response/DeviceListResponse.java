package com.aiyolo.service.api.response;

import java.util.List;

import com.aiyolo.constant.ApiResponseStateEnum;
import com.aiyolo.service.api.request.RequestObject;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DeviceListResponse extends Response {

    private List<DeviceObject> devices;

//    public DeviceListResponse() {
//    }

    public DeviceListResponse(RequestObject request, List<DeviceObject> devices) {
        super(request.getAction(), devices.size() == 0 ? ApiResponseStateEnum.ERROR_NONE_DATA : ApiResponseStateEnum.SUCCESS);

        this.devices = devices;
    }

    @Override
    public String toString() {
        return super.toString() + ", DeviceListResponse{" +
                "devices='" + devices.toString() + '\'' +
                '}';
    }

    public List<DeviceObject> getDevices() {
        return devices;
    }

    public void setDevices(List<DeviceObject> devices) {
        this.devices = devices;
    }

}
