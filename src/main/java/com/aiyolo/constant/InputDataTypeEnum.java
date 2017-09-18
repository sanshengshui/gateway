package com.aiyolo.constant;

public enum InputDataTypeEnum {

    GATEWAY_BEAT(new String[] {}), // 网关心跳
    GATEWAY_PUSH(new String[] {"upsta", "alarm", "adddev", "devsta", "sta"}),
    GATEWAY_REQUEST(new String[] {"devlist"}),
    GATEWAY_RESPONSE(new String[] {"deldev", "pair", "broa", "io"}),

    APP_PUSH(new String[] {}), // 改http协议直接发送至后台接口
    APP_REQUEST(new String[] {}), // 改http协议直接发送至后台接口
    APP_RESPONSE(new String[] {"notice_gateway", "notice_dev"}),

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
