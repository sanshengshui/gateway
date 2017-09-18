package com.aiyolo.constant;

public enum PushSettingLevelEnum {

    CLEAR(0, "警报解除"),
    ALARM(1, "警报");

    private Integer value;
    private String name;

    private PushSettingLevelEnum(Integer value, String name) {
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
                "PushSettingLevel[value=%d, name='%s']",
                value, name);
    }

}
