package com.aiyolo.constant;

public class DeviceTypeConsts {


    public static final String TYPE_SMOKE = "0x00020001";//烟雾
    public static final String TYPE_SMOKE_OLD = "0x0005530D";//烟雾旧设备

    public static final String TYPE_CH4 = "0x00020002";//天然气
    public static final String TYPE_CH4_OLD = "0x06100000";//天然气旧设备
    public static final String TYPE_CH4_VALUE = "0x00020004";//天然气带水阀
    public static final String TYPE_CH4_SWITCH = "0x00020009";//天然气带电磁阀
    public static final String TYPE_CH4_SWITCH_BAT = "0x00020006";//天然气带电磁阀和电池

    public static final String TYPE_SOS = "0x00020003";//SOS
    public static final String TYPE_SOS_OLD = "0x03000000";//SOS旧设备


    public static final String TYPE_VALVE = "0x00020020";//个联水流开关

    public static final String TYPE_CO_SWITCH = "0x00020008";//CO 带电磁阀
    public static final String TYPE_CO_SWITCH_BAT = "0x00020005";//CO 带电磁阀和电池

    public static final String TYPE_CO_CH4_SWITCH_BAT = "0x00020007";//一氧化碳+天然气带电磁阀和电池
    public static final String TYPE_CO_CH4_SWITCH = "0x0002000A";//一氧化碳+天然气带电磁阀

    public static final String TYPE_ELECTRIC_METER = "0x00030001";//


    //    public static final String DEV_ = "XF-YW";
    //    public static final String DEV_ = "XF-YW";

    public static final String NAME_SMOKE = "烟雾报警器";
    public static final String NAME_CH4 = "天然气报警器";
    public static final String NAME_SOS = "SOS紧急按钮";
    public static final String NAME_VALVE = "水流开关";
    public static final String NAME_CO = "一氧化碳报警器";
    //    public static final String DEVICE_NAME_CG = "燃气报警器";
    public static final String NAME_CO_CH4 = "一氧化碳+天然气报警器";
    public static final String NAME_ELECTRIC_METER = "电气报警电表";
}
