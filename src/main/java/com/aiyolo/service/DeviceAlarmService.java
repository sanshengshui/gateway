package com.aiyolo.service;

import com.aiyolo.channel.data.request.AppNoticeWarningRequest;
import com.aiyolo.common.SpringUtil;
import com.aiyolo.common.StringHelper;
import com.aiyolo.constant.PushConsts;
import com.aiyolo.constant.SmsConsts;
import com.aiyolo.data.SimplePageRequest;
import com.aiyolo.entity.DeviceAlarm;
import com.aiyolo.entity.DeviceCategory;
import com.aiyolo.entity.Gateway;
import com.aiyolo.queue.Sender;
import com.aiyolo.repository.DeviceAlarmRepository;
import com.aiyolo.repository.DeviceAlarmSpecification;
import com.aiyolo.repository.DeviceCategoryRepository;
import com.aiyolo.repository.GatewayRepository;
import com.aiyolo.service.alarm.AlarmService;
import com.aiyolo.service.alarm.NowAlarmService;
import com.aiyolo.vo.DeviceAlarmSearchVo;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import java.util.*;

@Service
public class DeviceAlarmService {

    private static final Log errorLogger = LogFactory.getLog("errorLog");

    @Autowired
    Sender sender;

    @Autowired
    GatewayRepository gatewayRepository;
    @Autowired
    DeviceAlarmRepository deviceAlarmRepository;
    @Autowired
    DeviceCategoryRepository deviceCategoryRepository;

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

    public Page<DeviceAlarm> getPageDeviceAlarm(DeviceAlarmSearchVo deviceAlarmSearchVo) {
        Page<DeviceAlarm> alarms = new PageImpl<DeviceAlarm>(new ArrayList<DeviceAlarm>());

        if (StringUtils.isNotEmpty(deviceAlarmSearchVo.getGlImei())) {
            // 权限判断
            Gateway gateway = gatewayService.getGatewayByGlImei(deviceAlarmSearchVo.getGlImei());
            if (gateway != null) {
                alarms = deviceAlarmRepository.findPageByGlImei(
                        SimplePageRequest.getPageable(deviceAlarmSearchVo),
                        deviceAlarmSearchVo.getGlImei());
            }
        } else {
            Map<String, List<String>> authorities = customUserDetailsService.getAuthorities();
            String areaCodeSearchPat = areaCodeService.getAreaCodeSearchPat(authorities, deviceAlarmSearchVo.getAreaCode());
            if (StringUtils.isNotEmpty(areaCodeSearchPat)) {
                deviceAlarmSearchVo.setAreaCode(areaCodeSearchPat);
                alarms = deviceAlarmRepository.findAll(
                        DeviceAlarmSpecification.getSpec(deviceAlarmSearchVo),
                        SimplePageRequest.getPageable(deviceAlarmSearchVo));
            }
        }

        return alarms;
    }

    public void pushDeviceAlarm(DeviceAlarm deviceAlarm) {
        if (deviceAlarm == null) {
            return;
        }

        if (deviceAlarm.getGateway() == null) {
            Gateway gateway = gatewayRepository.findFirstByGlImeiOrderByIdDesc(deviceAlarm.getGlImei());
            if (gateway == null) {
                return;
            }

            deviceAlarm.setGateway(gateway);
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

                String deviceType = "";
                //根据不同报警器设备显示不同的报警标题
                deviceType = DeviceStatusService.deviceTypeToName(deviceType);

                if (StringUtils.isNotEmpty(deviceType)) {
                    msgTitle = msgTitle.replace("智能报警器", deviceType);
                    msgTitle = msgTitle.replace("警报解除通知", deviceType + "解除警报");
                }
                //根据不同报警器设备显示不同的报警标题

                msgContent = SmsConsts.SMS_SIGN + pushSetting.get("app").get("content");
                smsContent = SmsConsts.SMS_SIGN + pushSetting.get("sms").get("content");

                // 推送给APP
                Map<String, Object> headerMap = AppNoticeWarningRequest.getInstance().requestHeader(mobileIds);
                headerMap.put("cache_time", PushConsts.CACHE_TIME);

                Map<String, Object> queryParamMap = new HashMap<String, Object>();
                queryParamMap.put("imeiGateway", deviceAlarm.getGlImei());
                queryParamMap.put("imei", deviceAlarm.getImei());
                queryParamMap.put("title", msgTitle);
                queryParamMap.put("text", msgContent);
                queryParamMap.put("timestamp", deviceAlarm.getTimestamp() * 1000L);

                Map<String, Object> bodyMap = AppNoticeWarningRequest.getInstance().requestBody(queryParamMap);

                sender.sendMessage(headerMap, bodyMap);

                // 推送给个推
                messagePushService.pushMessage(mobileIds, msgTitle, msgContent);

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

    @Async
    public <S extends AlarmService> void addDispose(DeviceAlarm deviceAlarm) {
        try {
            DeviceCategory deviceCategory = deviceCategoryRepository.findOneByCode(deviceAlarm.getType());
            if (deviceCategory != null && StringUtils.isNotEmpty(deviceCategory.getExtra())) {
                JSONObject extraJson = JSONObject.fromObject(deviceCategory.getExtra());
                if (!extraJson.isNullObject()) {
                    ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
                    ScriptEngine nashorn = scriptEngineManager.getEngineByName("nashorn");

                    Iterator conditions = extraJson.keys();
                    while (conditions.hasNext()) {
                        String condition = (String) conditions.next();
                        JSONObject content = extraJson.getJSONObject(condition);

                        nashorn.put("val", deviceAlarm.getValue());
                        Boolean result = (Boolean) nashorn.eval(condition);
                        if (result && !content.isNullObject()) {
                            Iterator content_cls = content.keys();
                            while (content_cls.hasNext()) {
                                String cls = (String) content_cls.next();
                                Object params = content.get(cls);

                                // String packageName = this.getClass().getPackage().getName() + ".alarm";
                                // String className = cls.replace(cls.substring(0, 1), cls.substring(0, 1).toUpperCase()) + "AlarmService";
                                // S service = (S) Class.forName(packageName + "." + className).newInstance();
                                String serviceName = StringHelper.underline2Camel(cls) + "AlarmService";
                                S service = (S) SpringUtil.getBean(serviceName);
                                service.run(deviceAlarm, params);
                            }
                        }
                    }

                    if (deviceAlarm.getValue() == 0) {
                        NowAlarmService nowAlarmService = (NowAlarmService) SpringUtil.getBean("nowAlarmService");
                        nowAlarmService.run(deviceAlarm, "C");
                    }
                }
            }
        } catch (Exception e) {
            errorLogger.error("addDispose异常！deviceAlarm:" + deviceAlarm.toString(), e);
        }
    }

}
