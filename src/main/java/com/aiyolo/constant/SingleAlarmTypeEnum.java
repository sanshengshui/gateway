package com.aiyolo.constant;

public enum SingleAlarmTypeEnum {

    CLEAR(0, "警报解除"),
    ONE_LEVEL_HIGH_TEMPERATURE(1, "一级预报警"),
    TWO_LEVEL_HIGH_TEMPERATURE(2, "二级预报警"),
    FIRE(4, "火灾报警"),
    GAS_LEAK(8, "燃气泄漏"),
    CO_LEAK(16, "CO气体泄漏"),
    SMOKE(32, "烟雾");

    private Integer value;
    private String name;

    private SingleAlarmTypeEnum(Integer value, String name) {
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
                "SingleAlarmType[value=%d, name='%s']",
                value, name);
    }

}
