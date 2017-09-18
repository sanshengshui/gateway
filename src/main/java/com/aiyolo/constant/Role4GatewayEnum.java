package com.aiyolo.constant;

public enum Role4GatewayEnum {

    MANAGER(1, "管理员"),
    USER(0, "普通用户");

    private Integer value;
    private String name;

    private Role4GatewayEnum(Integer value, String name) {
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
                "Role4Gateway[value=%d, name='%s']",
                value, name);
    }

}
