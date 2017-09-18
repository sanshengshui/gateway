package com.aiyolo.service.api.response;

import com.aiyolo.constant.ApiResponseStateEnum;
import com.aiyolo.service.api.request.RequestObject;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ListAlarmResponse extends Response {

    private List<AlarmObject> alarms;

    public ListAlarmResponse(RequestObject request, List<AlarmObject> alarms) {
        super(request.getAction(), alarms.size() == 0 ? ApiResponseStateEnum.ERROR_NONE_DATA : ApiResponseStateEnum.SUCCESS);

        this.alarms = alarms;
    }

    @Override
    public String toString() {
        return super.toString() + ", ListAlarmResponse{" +
                "alarms='" + alarms.toString() + '\'' +
                '}';
    }

    public List<AlarmObject> getAlarms() {
        return alarms;
    }

    public void setAlarms(List<AlarmObject> alarms) {
        this.alarms = alarms;
    }

}
