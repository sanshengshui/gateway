package com.aiyolo.constant;

public enum DeviceNetStatusEnum {

    WIFI(1, "wifi"),
    NET(2, "2G"),
    OFFLINE(0, "离线");

    private Integer value;
    private String name;

    private DeviceNetStatusEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format(
                "DeviceNetStatus[value=%d, name='%s']",
                value, name);
    }

}
