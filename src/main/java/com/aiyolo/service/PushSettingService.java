package com.aiyolo.service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aiyolo.constant.AlarmMessageTemplateConsts;
import com.aiyolo.constant.PushSettingLevelEnum;
import com.aiyolo.constant.PushSettingPlaceholderEnum;
import com.aiyolo.constant.SingleAlarmTypeEnum;
import com.aiyolo.entity.Device;
import com.aiyolo.entity.DeviceAlarm;
import com.aiyolo.entity.PushSetting;
import com.aiyolo.repository.PushSettingRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PushSettingService {

    @Autowired PushSettingRepository pushSettingRepository;

    @Autowired DeviceService deviceService;
    @Autowired DeviceAlarmService deviceAlarmService;

    public Map<String, Map<String, String>> getDefaultPushSetting(int level) {
        Map<String, Map<String, String>> defaultPushSetting = new HashMap<String, Map<String, String>>();

        Map<String, String> appPushSetting = new HashMap<String, String>();
        appPushSetting.put("title", level == SingleAlarmTypeEnum.CLEAR.getValue() ? AlarmMessageTemplateConsts.TITLE_CLEAR : AlarmMessageTemplateConsts.TITLE_ALARM);
        appPushSetting.put("content", level == SingleAlarmTypeEnum.CLEAR.getValue() ? AlarmMessageTemplateConsts.CONTENT_CLEAR : AlarmMessageTemplateConsts.CONTENT_ALARM);

        Map<String, String> smsPushSetting = new HashMap<String, String>();
        smsPushSetting.put("title", level == SingleAlarmTypeEnum.CLEAR.getValue() ? AlarmMessageTemplateConsts.TITLE_CLEAR : AlarmMessageTemplateConsts.TITLE_ALARM);
        smsPushSetting.put("content", level == SingleAlarmTypeEnum.CLEAR.getValue() ? AlarmMessageTemplateConsts.CONTENT_CLEAR : AlarmMessageTemplateConsts.CONTENT_ALARM);

        defaultPushSetting.put("app", appPushSetting);
        defaultPushSetting.put("sms", smsPushSetting);

        return defaultPushSetting;
    }

    public Map<String, Map<String, String>> getPushSettingByLevel(int level) {
        return getPushSettingByLevel(level, new HashMap<String, String>());
    }

    public Map<String, Map<String, String>> getPushSettingByLevel(int level, Map<String, String> placeholderValues) {
        Map<String, Map<String, String>> result = getDefaultPushSetting(level);

        List<PushSetting> pushSettings = pushSettingRepository.findByLevel(level);
        for (int i = 0; i < pushSettings.size(); i++) {
            if (result.keySet().contains(pushSettings.get(i).getType())) {
                String title = pushSettings.get(i).getTitle();
                if (StringUtils.isNotEmpty(title)) {
                    result.get(pushSettings.get(i).getType()).put("title", title);
                }

                String content = pushSettings.get(i).getContent();
                if (StringUtils.isNotEmpty(content)) {
                    if (!placeholderValues.isEmpty()) {
                        content = formatPlaceholder(content, placeholderValues);
                    }
                    result.get(pushSettings.get(i).getType()).put("content", content);
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

    public Map<String, String> buildPlaceholderValues(Device device, DeviceAlarm deviceAlarm) {
        Map<String, String> placeholderValues = new HashMap<String, String>();
        placeholderValues.put("glName", device.getGlName());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datetime = format.format(deviceAlarm.getTimestamp() * 1000L);
        placeholderValues.put("datetime", datetime);

        placeholderValues.put("address", deviceService.getFullAddress(device.getAddress(), device.getAreaCode()));
        placeholderValues.put("alarmType", deviceAlarmService.getDeviceAlarmTypeName(deviceAlarm.getType()));

        return placeholderValues;
    }

    public int getPushSettingLevel(int alarmType) {
        if (SingleAlarmTypeEnum.CLEAR.getValue().equals(alarmType)) {
            return PushSettingLevelEnum.CLEAR.getValue();
        } else {
            return PushSettingLevelEnum.ALARM.getValue();
        }
    }

}
