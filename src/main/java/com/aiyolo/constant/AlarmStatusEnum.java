package com.aiyolo.constant;

public enum AlarmStatusEnum {

    CLEAR(0, "已解除"),
    LIFE(1, "未解除");

    private Integer value;
    private String name;

    private AlarmStatusEnum(Integer value, String name) {
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
                "AlarmStatus[value=%d, name='%s']",
                value, name);
    }

}
