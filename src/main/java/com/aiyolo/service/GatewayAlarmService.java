package com.aiyolo.service;

import com.aiyolo.channel.data.request.AppNoticeWarningRequest;
import com.aiyolo.constant.AlarmStatusEnum;
import com.aiyolo.constant.SingleAlarmTypeEnum;
import com.aiyolo.constant.SmsConsts;
import com.aiyolo.entity.DeviceAlarm;
import com.aiyolo.entity.Gateway;
import com.aiyolo.queue.Sender;
//import com.aiyolo.repository.DeviceAlarmCancelRepository;
import com.aiyolo.repository.DeviceAlarmRepository;
import com.aiyolo.repository.GatewayRepository;

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
public class GatewayAlarmService {

    private static final Log errorLogger = LogFactory.getLog("errorLog");

    @Autowired
    Sender sender;


    @Autowired
    DeviceAlarmRepository deviceAlarmRepository;
    //    @Autowired
    //    DeviceAlarmCancelRepository deviceAlarmCancelRepository;


    public void gatewayAlarm(String imei, int val, int mid) {
//        DeviceAlarm deviceAlarm = new DeviceAlarm(
//                "智能网关",
//                imei,
//                imei,
//                mid,
//                1,
//                AlarmStatusEnum.LIFE.getValue());
//
//        if (AlarmStatusEnum.CLEAR.getValue().equals(val)) {
//            //解除
//            // 先查询是否有未解除的报警
//            DeviceAlarm _deviceAlarm = deviceAlarmRepository.findFirstByImeiAndStatusOrderByIdDesc(
//                    imei, AlarmStatusEnum.LIFE.getValue());
//            if (_deviceAlarm == null) {
//                // 如果没有未解除的报警，直接返回
//                return;
//            }
//            // 报警解除
//            deviceAlarmRepository.updateStatusByImei(imei, val);
//
//        } else {
//            // 添加记录
//            deviceAlarmRepository.save(deviceAlarm);
//
//        }
//
//        // 推送预报警通知给APP&个推&发送短信
//        pushDeviceAlarm(deviceAlarm);

    }

    private void pushDeviceAlarm(DeviceAlarm deviceAlarm) {


    }


    //
    //    @Autowired
    //    GatewayRepository gatewayRepository;
    //    @Autowired
    //    DeviceAlarmRepository deviceAlarmRepository;
    //
    //    @Autowired
    //    GatewayService gatewayService;
    //    @Autowired
    //    PushSettingService pushSettingService;
    //    @Autowired
    //    MessagePushService messagePushService;
    //    @Autowired
    //    SmsPushService smsPushService;
    //    @Autowired
    //    AreaCodeService areaCodeService;
    //    @Autowired
    //    CustomUserDetailsService customUserDetailsService;
    //
    //    public Page<DeviceAlarm> getPageDeviceAlarmByAreaCode(Pageable pageable, String areaCode) {
    //        Page<DeviceAlarm> alarms = new PageImpl<DeviceAlarm>(new ArrayList<DeviceAlarm>());
    //
    //        // 取有权限的区域
    //        Map<String, List<String>> authorities = customUserDetailsService.getAuthorities();
    //        if (authorities.get("areas").contains("0") && "0".equals(areaCode)) {
    //            alarms = deviceAlarmRepository.findAll(pageable);
    //        } else {
    //            String areaCodeSearchPat = areaCodeService.getAreaCodeSearchPat(authorities, areaCode);
    //            if (StringUtils.isNotEmpty(areaCodeSearchPat)) {
    //                alarms = deviceAlarmRepository.findPageByGatewayAreaCodeMatch(pageable, areaCodeSearchPat);
    //            }
    //        }
    //
    //        return alarms;
    //    }
    //
    //    public Map<Integer, String> getAllDeviceAlarmType() {
    //        return getAllDeviceAlarmType(",");
    //    }
    //
    //    public Map<Integer, String> getAllDeviceAlarmType(String separator) {
    //        Integer maxTypeValue = 0;
    //        for (SingleAlarmTypeEnum singleType : SingleAlarmTypeEnum.values()) {
    //            maxTypeValue += singleType.getValue();
    //        }
    //
    //        Map<Integer, String> alarmTypeMap = new HashMap<Integer, String>();
    //        alarmTypeMap.put(0, SingleAlarmTypeEnum.CLEAR.getName());
    //        for (int i = 1; i <= maxTypeValue; i++) {
    //            String typeName = getDeviceAlarmTypeName(i, separator);
    //            if (StringUtils.isNotEmpty(typeName)) {
    //                alarmTypeMap.put(i, typeName);
    //            }
    //        }
    //
    //        return alarmTypeMap;
    //    }
    //
    //    public String[] getDeviceAlarmTypeNameArray(int type) {
    //        List<String> typeNameList = new ArrayList<String>();
    //
    //        if (type == 0) {
    //            typeNameList.add(SingleAlarmTypeEnum.CLEAR.getName());
    //        } else {
    //            for (SingleAlarmTypeEnum singleType : SingleAlarmTypeEnum.values()) {
    //                if ((singleType.getValue() & type) > 0) {
    //                    typeNameList.add(singleType.getName());
    //                }
    //            }
    //        }
    //
    //        return typeNameList.toArray(new String[0]);
    //    }
    //
    //    public String getDeviceAlarmTypeName(int type) {
    //        return getDeviceAlarmTypeName(type, ",");
    //    }
    //
    //    public String getDeviceAlarmTypeName(int type, String separator) {
    //        return StringUtils.join(getDeviceAlarmTypeNameArray(type), separator);
    //    }
    //
    //    public void pushDeviceAlarm(DeviceAlarm deviceAlarm) {
    //        if (deviceAlarm == null) {
    //            return;
    //        }
    //
    //        if (deviceAlarm.getGateway() == null) {
    //            Gateway gateway = gatewayRepository.findFirstByGlImeiOrderByIdDesc(deviceAlarm.getGlImei());
    //            if (gateway == null) {
    //                return;
    //            }
    //
    //            deviceAlarm.setGateway(gateway);
    //        }
    //
    //        try {
    //            String[] mobileIds = gatewayService.getGatewayUserMobileIds(deviceAlarm.getGlImei());
    //            if (mobileIds != null && mobileIds.length > 0) {
    //                String msgTitle = "";
    //                String msgContent = "";
    //                String smsContent = "";
    //
    //                Map<String, String> placeholderValues = pushSettingService.buildPlaceholderValues(deviceAlarm);
    //                Map<String, Map<String, String>> pushSetting = pushSettingService.getPushSettingByType(
    //                        pushSettingService.getPushSettingType(deviceAlarm.getValue()),
    //                        placeholderValues);
    //                msgTitle = pushSetting.get("app").get("title");
    //
    //                String deviceType = "";
    //                //根据不同报警器设备显示不同的报警标题
    //                switch (deviceAlarm.getType()) {
    //                    case DeviceTypeConsts.DEV_TYPE_CG:
    //                        deviceType = DeviceTypeConsts.DEVICE_NAME_CG;
    //                        break;
    //                    case DeviceTypeConsts.DEV_TYPE_CO:
    //                        deviceType = DeviceTypeConsts.DEVICE_NAME_CO;
    //                        break;
    //                    case DeviceTypeConsts.DEV_TYPE_GAS:
    //                        deviceType = DeviceTypeConsts.DEVICE_NAME_GAS;
    //                        break;
    //                    case DeviceTypeConsts.DEV_TYPE_SMOKE:
    //                        deviceType = DeviceTypeConsts.DEVICE_NAME_SMOKE;
    //                        break;
    //                    case DeviceTypeConsts.DEV_TYPE_SOS:
    //                        deviceType = DeviceTypeConsts.DEVICE_NAME_SOS;
    //                        break;
    //                    case DeviceTypeConsts.DEV_TYPE_VALVE:
    //                        deviceType = DeviceTypeConsts.DEVICE_NAME_VALVE;
    //                        break;
    //                }
    //                if (StringUtils.isNotEmpty(deviceType)) {
    //                    msgTitle = msgTitle.replace("智能报警器", deviceType);
    //                    msgTitle = msgTitle.replace("警报解除通知", deviceType + "解除警报");
    //                }
    //                //根据不同报警器设备显示不同的报警标题
    //
    //                msgContent = SmsConsts.SMS_SIGN + pushSetting.get("app").get("content");
    //                smsContent = SmsConsts.SMS_SIGN + pushSetting.get("sms").get("content");
    //
    //                // 推送给APP
    //                Map<String, Object> headerMap = AppNoticeWarningRequest.getInstance().requestHeader(mobileIds);
    //                headerMap.put("cache_time", 24 * 60 * 60 * 1000L);
    //
    //                Map<String, Object> queryParamMap = new HashMap<String, Object>();
    //                queryParamMap.put("imeiGateway", deviceAlarm.getGlImei());
    //                queryParamMap.put("imei", deviceAlarm.getImei());
    //                queryParamMap.put("title", msgTitle);
    //                queryParamMap.put("text", msgContent);
    //                queryParamMap.put("timestamp", deviceAlarm.getTimestamp() * 1000L);
    //
    //                Map<String, Object> bodyMap = AppNoticeWarningRequest.getInstance().requestBody(queryParamMap);
    //
    //                sender.sendMessage(headerMap, bodyMap);
    //
    //                // 推送给个推
    //                messagePushService.pushMessage(mobileIds, deviceAlarm.getGateway().getGlId(), msgTitle, msgContent);
    //
    //                // 发送短信
    //                smsPushService.pushSms(deviceAlarm.getGlImei(), smsContent);
    //            }
    //        } catch (Exception e) {
    //            errorLogger.error("pushDeviceAlarm异常！deviceAlarm:" + deviceAlarm.toString(), e);
    //        }
    //    }
    //
    //    public void pushDeviceAlarm(String imei) {
    //        DeviceAlarm deviceAlarm = deviceAlarmRepository.findFirstByImeiOrderByIdDesc(imei);
    //        if (deviceAlarm == null) {
    //            return;
    //        }
    //
    //        pushDeviceAlarm(deviceAlarm);
    //    }

}
