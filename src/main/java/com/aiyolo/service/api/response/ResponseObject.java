package com.aiyolo.service.api.response;

import com.aiyolo.constant.ApiResponseStateEnum;

public class ResponseObject {

    private String act;
    private Integer ret;
    private String error;

    public ResponseObject() {
    }

    public ResponseObject(String action, ApiResponseStateEnum state) {
        this.act = action;
        this.ret = state.getResult();
        this.error = state.getDes();
    }

    public ResponseObject(String action, Integer result, String error) {
        this.act = action;
        this.ret = result;
        this.error = error;
    }

    @Override
    public String toString() {
        return "ResponseObject{" +
                "act='" + act + '\'' +
                ", ret=" + ret +
                ", error='" + error + '\'' +
                '}';
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public Integer getRet() {
        return ret;
    }

    public void setRet(Integer ret) {
        this.ret = ret;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
