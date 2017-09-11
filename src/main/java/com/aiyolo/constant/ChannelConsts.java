package com.aiyolo.constant;

public class ChannelConsts {

    public static final String PRODUCT_ID = ProductEnum.ALARM.getId();
    public static final String MESSAGE_ID = "GL-%06d-%010d";

    public static final int DEVICE_RESPONSE_TIMEOUT = 15000; // 设备响应超时时间（毫秒）
    public static final int DEVICE_RESPONSE_READ_INTERVAL = 500; // 设备响应检测间隔（毫秒）

}
