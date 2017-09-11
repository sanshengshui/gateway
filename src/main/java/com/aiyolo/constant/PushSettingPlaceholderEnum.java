package com.aiyolo.constant;

public enum PushSettingPlaceholderEnum {

    GL_NAME("glName", "【报警器名称】"),
    DATETIME("datetime", "【时间】"),
    ADDRESS("address", "【地址】"),
    ALARM_TYPE("alarmType", "【警报内容】");

    private String name;
    private String placeholder;

    private PushSettingPlaceholderEnum(String name, String placeholder) {
        this.name = name;
        this.placeholder = placeholder;
    }

    public String getName() {
        return name;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    @Override
    public String toString() {
        return String.format(
                "PushSettingPlaceholder[name='%s', placeholder='%s']",
                name, placeholder);
    }

}
