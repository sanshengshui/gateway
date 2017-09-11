package com.aiyolo.constant;

public enum RoleEnum {

    MANAGER("超级管理员"),
    AGENT("渠道/代理用户"),
    EMPLOYEE("普通用户");

    private String name;

    private RoleEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format(
                "Role[name='%s']",
                name);
    }

}
