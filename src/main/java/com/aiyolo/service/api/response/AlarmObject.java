package com.aiyolo.service.api.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AlarmObject {

    private Long timeAlarm = 0L;
    private Long timeCut = 0L;
    private Long timeRemove = 0L;
    private Integer val = 0;

    public AlarmObject() {
    }

    public AlarmObject(Long timeAlarm, Long timeCut, Long timeRemove, Integer val) {
        this.timeAlarm = timeAlarm;
        this.timeCut = timeCut;
        this.timeRemove = timeRemove;
        this.val = val;
    }

    @Override
    public String toString() {
        return "AlarmObject{" +
                "time_alarm=" + timeAlarm +
                ", time_cut=" + timeCut +
                ", time_remove=" + timeRemove +
                ", val=" + val +
                '}';
    }

    public Long getTimeAlarm() {
        return timeAlarm;
    }

    public void setTimeAlarm(Long timeAlarm) {
        this.timeAlarm = timeAlarm;
    }

    public Long getTimeCut() {
        return timeCut;
    }

    public void setTimeCut(Long timeCut) {
        this.timeCut = timeCut;
    }

    public Long getTimeRemove() {
        return timeRemove;
    }

    public void setTimeRemove(Long timeRemove) {
        this.timeRemove = timeRemove;
    }

    public Integer getVal() {
        return val;
    }

    public void setVal(Integer val) {
        this.val = val;
    }

}
