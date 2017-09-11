package com.aiyolo.service.api.response;

import java.util.List;

import com.aiyolo.constant.ApiResponseStateEnum;
import com.aiyolo.service.api.request.RequestObject;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class NoticeListResponse extends Response {

    private List<DeviceAlarmObject> notices;

//    public NoticeListResponse() {
//    }

    public NoticeListResponse(RequestObject request, List<DeviceAlarmObject> notices) {
        super(request.getAction(), notices.size() == 0 ? ApiResponseStateEnum.ERROR_NONE_DATA : ApiResponseStateEnum.SUCCESS);

        this.notices = notices;
    }

    @Override
    public String toString() {
        return super.toString() + ", NoticeListResponse{" +
                "notices='" + notices.toString() + '\'' +
                '}';
    }

    public List<DeviceAlarmObject> getNotices() {
        return notices;
    }

    public void setNotices(List<DeviceAlarmObject> notices) {
        this.notices = notices;
    }

}
