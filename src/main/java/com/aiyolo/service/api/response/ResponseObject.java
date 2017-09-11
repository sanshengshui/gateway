package com.aiyolo.service.api.response;

import com.aiyolo.constant.ApiResponseStateEnum;

public class ResponseObject {

    private String action;
    private Integer result;
    private String error;

    public ResponseObject() {
    }

    public ResponseObject(String action, ApiResponseStateEnum state) {
        this.action = action;
        this.result = state.getResult();
        this.error = state.getDes();
    }

    public ResponseObject(String action, Integer result, String error) {
        this.action = action;
        this.result = result;
        this.error = error;
    }

    @Override
    public String toString() {
        return "ResponseObject{" +
                "action='" + action + '\'' +
                ", result=" + result +
                ", error='" + error + '\'' +
                '}';
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
