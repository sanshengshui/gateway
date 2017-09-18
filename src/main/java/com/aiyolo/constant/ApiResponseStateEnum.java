package com.aiyolo.constant;

public enum ApiResponseStateEnum {

    SUCCESS(0, ""),
    ERROR_SERVER(10500, "服务器内部错误"),
    ERROR_BUSINESS(10400, "业务处理错误"),
    ERROR_REQUEST_PARAMETER(10300, "请求参数错误"),
    ERROR_NONE_DATA(0, "无数据"),

    ERROR_AUTHENTICATE(10304, "认证错误"),
    ERROR_AUTHORITY(10305, "无权限"),

    ERROR_GATEWAY_OFFLINE(10601, "网关离线"),
    ERROR_GATEWAY_NONE_RESPONSE(10602, "网关未响应"),
    
    ERROR_UNKOWN(10900, "未知错误");

    private Integer result;
    private String des;

    private ApiResponseStateEnum(Integer result, String des) {
        this.result = result;
        this.des = des;
    }

    public Integer getResult() {
        return result;
    }

    public String getDes() {
        return des;
    }

    @Override
    public String toString() {
        return String.format(
                "ApiResponseState[result=%d, des='%s']",
                result, des);
    }

}
