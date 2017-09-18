package com.aiyolo.controller;

import com.aiyolo.common.FileHelper;
import com.aiyolo.common.SpringUtil;
import com.aiyolo.constant.*;
import com.aiyolo.entity.Channel;
import com.aiyolo.repository.ChannelRepository;
import com.aiyolo.service.AreaCodeService;
import com.aiyolo.service.DeviceAlarmService;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dict")
public class DictController {

    @RequestMapping(value = "/product", produces = "text/javascript; charset=UTF-8")
    public String product() {
        Map<String, String> productMap = new HashMap<String, String>();
        for (int i = 0; i < ProductEnum.values().length; i++) {
            productMap.put(ProductEnum.values()[i].getId(), ProductEnum.values()[i].getName());
        }

        return "var dict_product = " + JSONObject.fromObject(productMap).toString();
    }

    @RequestMapping(value = "/channel", produces = "text/javascript; charset=UTF-8")
    public String channel() {
        ChannelRepository channelRepository = (ChannelRepository) SpringUtil.getBean("channelRepository");
        List<Channel> channels = (List<Channel>) channelRepository.findAll();

        Map<String, String> channelMap = new HashMap<String, String>();
        for (int i = 0; i < channels.size(); i++) {
            channelMap.put(String.valueOf(channels.get(i).getId()), channels.get(i).getName());
        }

        return "var dict_channel = " + JSONObject.fromObject(channelMap).toString();
    }

    @RequestMapping(value = "/alarm_type", produces = "text/javascript; charset=UTF-8")
    public String alarmType() {
        DeviceAlarmService deviceAlarmService = (DeviceAlarmService) SpringUtil.getBean("deviceAlarmService");
        Map<Integer, String> alarmTypeMap = deviceAlarmService.getAllDeviceAlarmType();

        Map<String, String> _alarmTypeMap = new HashMap<String, String>();
        for (int type : alarmTypeMap.keySet()) {
            _alarmTypeMap.put(String.valueOf(type), alarmTypeMap.get(type));
        }

        return "var dict_alarm_type = " + JSONObject.fromObject(_alarmTypeMap).toString();
    }

    @RequestMapping(value = "/alarm_status", produces = "text/javascript; charset=UTF-8")
    public String alarmStatus() {
        Map<String, String> alarmStatusMap = new HashMap<String, String>();
        for (int i = 0; i < AlarmStatusEnum.values().length; i++) {
            alarmStatusMap.put(String.valueOf(AlarmStatusEnum.values()[i].getValue()), AlarmStatusEnum.values()[i].getName());
        }

        return "var dict_alarm_status = " + JSONObject.fromObject(alarmStatusMap).toString();
    }

    @RequestMapping(value = "/role", produces = "text/javascript; charset=UTF-8")
    public String role() {
        Map<String, String> roleMap = new HashMap<String, String>();
        for (int i = 0; i < RoleEnum.values().length; i++) {
            roleMap.put(String.valueOf(RoleEnum.values()[i].name()), RoleEnum.values()[i].getName());
        }

        return "var dict_role = " + JSONObject.fromObject(roleMap).toString();
    }

    @RequestMapping(value = "/role_gateway", produces = "text/javascript; charset=UTF-8")
    public String role4Gateway() {
        Map<String, String> roleMap = new HashMap<String, String>();
        for (int i = 0; i < Role4GatewayEnum.values().length; i++) {
            roleMap.put(String.valueOf(Role4GatewayEnum.values()[i].name()), Role4GatewayEnum.values()[i].getName());
        }

        return "var dict_role_gateway = " + JSONObject.fromObject(roleMap).toString();
    }

    @RequestMapping(value = "/area_code", produces = "text/javascript; charset=UTF-8")
    public String areaCode() {
        AreaCodeService areaCodeService = (AreaCodeService) SpringUtil.getBean("areaCodeService");

        try {
            return "var dict_area_code = " + areaCodeService.getAreaCodeDict().toString();
        } catch (Exception e) {
            return "var dict_area_code = {}";
        }
    }

    @RequestMapping(value = "/log_map", produces = "text/javascript; charset=UTF-8")
    public String logMap() {
        try {
            String logMapJsonString = FileHelper.getResourceContent("classpath:logmap.json");
            JSONObject logMapJson = JSONObject.fromObject(logMapJsonString);
            return "var dict_log_map = " + logMapJson.toString();
        } catch (Exception e) {
            return "var dict_log_map = {}";
        }
    }

}
