package com.aiyolo.constant;

public enum InputDataTypeEnum {

    DEVICE_BEAT(new String[] {}), // 设备心跳
    DEVICE_PUSH(new String[] {"status", "alarm", "sta", "ubd"}),
    DEVICE_REQUEST(new String[] {}),
    DEVICE_RESPONSE(new String[] {"ioset", "ioget", "wifi_set"}),

    APP_PUSH(new String[] {}), // 改http协议直接发送至后台接口
    APP_REQUEST(new String[] {}), // 改http协议直接发送至后台接口
    APP_RESPONSE(new String[] {"notice_warning"}),

    CHANNEL_REQUEST(new String[] {}),
    CHANNEL_RESPONSE(new String[] {});

    private String[] value;

    private InputDataTypeEnum(String[] value) {
        this.value = value;
    }

    public String[] getValue() {
        return value;
    }

}
