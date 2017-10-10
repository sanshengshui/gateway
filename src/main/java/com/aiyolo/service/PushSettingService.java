package com.aiyolo.service;

import com.aiyolo.constant.AlarmMessageTemplateConsts;
import com.aiyolo.constant.AlarmStatusEnum;
import com.aiyolo.constant.PushSettingPlaceholderEnum;
import com.aiyolo.entity.DeviceAlarm;
import com.aiyolo.entity.PushSetting;
import com.aiyolo.repository.PushSettingRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PushSettingService {

    @Autowired
    PushSettingRepository pushSettingRepository;

    @Autowired
    GatewayService gatewayService;
    @Autowired
    DeviceCategoryService deviceCategoryService;

    public Map<String, Map<String, String>> getDefaultPushSetting(int type) {
        Map<String, Map<String, String>> defaultPushSetting = new HashMap<String, Map<String, String>>();

        Map<String, String> appPushSetting = new HashMap<String, String>();
        appPushSetting.put("title", type == AlarmStatusEnum.CLEAR.getValue() ? AlarmMessageTemplateConsts.TITLE_CLEAR : AlarmMessageTemplateConsts.TITLE_ALARM);
        appPushSetting.put("content", type == AlarmStatusEnum.CLEAR.getValue() ? AlarmMessageTemplateConsts.CONTENT_CLEAR : AlarmMessageTemplateConsts.CONTENT_ALARM);

        Map<String, String> smsPushSetting = new HashMap<String, String>();
        smsPushSetting.put("title", type == AlarmStatusEnum.CLEAR.getValue() ? AlarmMessageTemplateConsts.TITLE_CLEAR : AlarmMessageTemplateConsts.TITLE_ALARM);
        smsPushSetting.put("content", type == AlarmStatusEnum.CLEAR.getValue() ? AlarmMessageTemplateConsts.CONTENT_CLEAR : AlarmMessageTemplateConsts.CONTENT_ALARM);

        defaultPushSetting.put("app", appPushSetting);
        defaultPushSetting.put("sms", smsPushSetting);

        return defaultPushSetting;
    }

    public Map<String, Map<String, String>> getPushSettingByType(int type) {
        return getPushSettingByType(type, new HashMap<String, String>());
    }

    public Map<String, Map<String, String>> getPushSettingByType(int type, Map<String, String> placeholderValues) {
        Map<String, Map<String, String>> result = getDefaultPushSetting(type);

        List<PushSetting> pushSettings = pushSettingRepository.findByType(type);
        for (int i = 0; i < pushSettings.size(); i++) {
            if (result.keySet().contains(pushSettings.get(i).getTarget())) {
                String title = pushSettings.get(i).getTitle();
                if (StringUtils.isNotEmpty(title)) {
                    result.get(pushSettings.get(i).getTarget()).put("title", title);
                }

                String content = pushSettings.get(i).getContent();
                if (StringUtils.isNotEmpty(content)) {
                    if (!placeholderValues.isEmpty()) {
                        content = formatPlaceholder(content, placeholderValues);
                    }
                    result.get(pushSettings.get(i).getTarget()).put("content", content);
                }
            }
        }

        return result;
    }

    private String formatPlaceholder(String message, Map<String, String> placeholderValues) {
        for (PushSettingPlaceholderEnum placeholder : PushSettingPlaceholderEnum.values()) {
            if (placeholderValues.keySet().contains(placeholder.getName())) {
                message = message.replace(placeholder.getPlaceholder(), placeholderValues.get(placeholder.getName()));
            }
        }

        return message;
    }

    public Map<String, String> buildPlaceholderValues(DeviceAlarm deviceAlarm) {
        Map<String, String> placeholderValues = new HashMap<String, String>();
        placeholderValues.put("glName", deviceAlarm.getGateway().getGlName());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datetime = format.format(deviceAlarm.getTimestamp() * 1000L);
        placeholderValues.put("datetime", datetime);

        placeholderValues.put("address", gatewayService.getFullAddress(deviceAlarm.getGateway().getAddress(), deviceAlarm.getGateway().getAreaCode()));
        placeholderValues.put("alarmType", deviceCategoryService.getDeviceValueDesc(deviceAlarm.getType(), deviceAlarm.getValue()));

        return placeholderValues;
    }

    public int getPushSettingType(int alarmValue) {
        if (AlarmStatusEnum.CLEAR.getValue().equals(alarmValue)) {
            return AlarmStatusEnum.CLEAR.getValue();
        } else {
            return AlarmStatusEnum.LIFE.getValue();
        }
    }

}
