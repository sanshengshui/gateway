package com.aiyolo.service;

import com.aiyolo.channel.data.request.AppNoticeWarningRequest;
import com.aiyolo.constant.SingleAlarmTypeEnum;
import com.aiyolo.constant.SmsConsts;
import com.aiyolo.entity.DeviceAlarm;
import com.aiyolo.queue.Sender;
import com.aiyolo.repository.DeviceAlarmRepository;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DeviceAlarmService {

    private static final Log errorLogger = LogFactory.getLog("errorLog");

    @Autowired
    Sender sender;

    @Autowired
    DeviceAlarmRepository deviceAlarmRepository;

    @Autowired
    GatewayService gatewayService;
    @Autowired
    PushSettingService pushSettingService;
    @Autowired
    MessagePushService messagePushService;
    @Autowired
    SmsPushService smsPushService;
    @Autowired
    AreaCodeService areaCodeService;
    @Autowired
    CustomUserDetailsService customUserDetailsService;

    public Page<DeviceAlarm> getPageDeviceAlarmByAreaCode(Pageable pageable, String areaCode) {
        Page<DeviceAlarm> alarms = new PageImpl<DeviceAlarm>(new ArrayList<DeviceAlarm>());

        // 取有权限的区域
        Map<String, List<String>> authorities = customUserDetailsService.getAuthorities();
        if (authorities.get("areas").contains("0") && "0".equals(areaCode)) {
            alarms = deviceAlarmRepository.findAll(pageable);
        } else {
            String areaCodeSearchPat = areaCodeService.getAreaCodeSearchPat(authorities, areaCode);
            if (StringUtils.isNotEmpty(areaCodeSearchPat)) {
                alarms = deviceAlarmRepository.findPageByGatewayAreaCodeMatch(pageable, areaCodeSearchPat);
            }
        }

        return alarms;
    }

    public Map<Integer, String> getAllDeviceAlarmType() {
        return getAllDeviceAlarmType(",");
    }

    public Map<Integer, String> getAllDeviceAlarmType(String separator) {
        Integer maxTypeValue = 0;
        for (SingleAlarmTypeEnum singleType : SingleAlarmTypeEnum.values()) {
            maxTypeValue += singleType.getValue();
        }

        Map<Integer, String> alarmTypeMap = new HashMap<Integer, String>();
        alarmTypeMap.put(0, SingleAlarmTypeEnum.CLEAR.getName());
        for (int i = 1; i <= maxTypeValue; i++) {
            String typeName = getDeviceAlarmTypeName(i, separator);
            if (StringUtils.isNotEmpty(typeName)) {
                alarmTypeMap.put(i, typeName);
            }
        }

        return alarmTypeMap;
    }

    public String[] getDeviceAlarmTypeNameArray(int type) {
        List<String> typeNameList = new ArrayList<String>();

        if (type == 0) {
            typeNameList.add(SingleAlarmTypeEnum.CLEAR.getName());
        } else {
            for (SingleAlarmTypeEnum singleType : SingleAlarmTypeEnum.values()) {
                if ((singleType.getValue() & type) > 0) {
                    typeNameList.add(singleType.getName());
                }
            }
        }

        return typeNameList.toArray(new String[0]);
    }

    public String getDeviceAlarmTypeName(int type) {
        return getDeviceAlarmTypeName(type, ",");
    }

    public String getDeviceAlarmTypeName(int type, String separator) {
        return StringUtils.join(getDeviceAlarmTypeNameArray(type), separator);
    }

    public void pushDeviceAlarm(DeviceAlarm deviceAlarm) {
        if (deviceAlarm == null) {
            return;
        }

        try {
            String[] mobileIds = gatewayService.getGatewayUserMobileIds(deviceAlarm.getGlImei());
            if (mobileIds != null && mobileIds.length > 0) {
                String msgTitle = "";
                String msgContent = "";
                String smsContent = "";

                Map<String, String> placeholderValues = pushSettingService.buildPlaceholderValues(deviceAlarm);
                Map<String, Map<String, String>> pushSetting = pushSettingService.getPushSettingByType(
                        pushSettingService.getPushSettingType(deviceAlarm.getValue()),
                        placeholderValues);
                msgTitle = pushSetting.get("app").get("title");
                msgContent = SmsConsts.SMS_SIGN + pushSetting.get("app").get("content");
                smsContent = SmsConsts.SMS_SIGN + pushSetting.get("sms").get("content");

                // 推送给APP
                Map<String, Object> headerMap = AppNoticeWarningRequest.getInstance().requestHeader(mobileIds);
                headerMap.put("cache_time", 24 * 60 * 60 * 1000L);

                Map<String, Object> queryParamMap = new HashMap<String, Object>();
                queryParamMap.put("imeiGateway", deviceAlarm.getGlImei());
                queryParamMap.put("imei", deviceAlarm.getImei());
                queryParamMap.put("title", msgTitle);
                queryParamMap.put("text", msgContent);
                queryParamMap.put("timestamp", deviceAlarm.getTimestamp() * 1000L);

                Map<String, Object> bodyMap = AppNoticeWarningRequest.getInstance().requestBody(queryParamMap);

                sender.sendMessage(headerMap, bodyMap);

                // 推送给个推
                messagePushService.pushMessage(mobileIds, deviceAlarm.getGateway().getGlId(), msgTitle, msgContent);

                // 发送短信
                smsPushService.pushSms(deviceAlarm.getGlImei(), smsContent);
            }
        } catch (Exception e) {
            errorLogger.error("pushDeviceAlarm异常！deviceAlarm:" + deviceAlarm.toString(), e);
        }
    }

    public void pushDeviceAlarm(String imei) {
        DeviceAlarm deviceAlarm = deviceAlarmRepository.findFirstByImeiOrderByIdDesc(imei);
        if (deviceAlarm == null) {
            return;
        }

        pushDeviceAlarm(deviceAlarm);
    }

}
