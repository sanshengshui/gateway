package com.aiyolo.service;

import com.aiyolo.channel.data.request.AppNoticeDeviceRequest;
import com.aiyolo.constant.AlarmStatusEnum;
import com.aiyolo.constant.AppNoticeTypeConsts;
import com.aiyolo.constant.PushConsts;
import com.aiyolo.entity.Device;
import com.aiyolo.entity.DeviceAlarm;
import com.aiyolo.entity.DeviceStatus;
import com.aiyolo.entity.Gateway;
import com.aiyolo.queue.Sender;
import com.aiyolo.repository.DeviceAlarmRepository;
import com.aiyolo.repository.DeviceRepository;
import com.aiyolo.repository.DeviceStatusRepository;

import com.aiyolo.constant.DeviceTypeConsts;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Service
public class DeviceStatusService {

    private static final Log errorLogger = LogFactory.getLog("errorLog");

    @Autowired
    Sender sender;

    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    DeviceStatusRepository deviceStatusRepository;
    @Autowired
    DeviceAlarmRepository deviceAlarmRepository;

    @Autowired
    GatewayService gatewayService;
    @Autowired
    MessagePushService messagePushService;

    public void pushDeviceStatus(Device device) {
        pushDeviceStatus(device, AppNoticeTypeConsts.MODIFY, null);
    }

    public void pushDeviceStatus(Device device, Integer onlineStatus) {
        pushDeviceStatus(device, AppNoticeTypeConsts.MODIFY, onlineStatus);
    }

    public void pushDeviceStatus(Device device, Integer noticeType, Integer onlineStatus) {
        if (device == null) {
            return;
        }

        try {
            String[] mobileIds = gatewayService.getGatewayUserMobileIds(device.getGlImei());
            if (mobileIds != null && mobileIds.length > 0) {
                // 推送给APP
                Map<String, Object> headerMap = AppNoticeDeviceRequest.getInstance().requestHeader(mobileIds);
                headerMap.put("cache_time", PushConsts.CACHE_TIME);

                Map<String, Object> queryParamMap = new HashMap<String, Object>();
                queryParamMap.put("imeiGateway", device.getGlImei());
                queryParamMap.put("imei", device.getImei());
                queryParamMap.put("notice", noticeType);
                queryParamMap.put("dev", device.getType());
                queryParamMap.put("pid", device.getPid());

                DeviceStatus deviceStatus = deviceStatusRepository.findFirstByImeiOrderByIdDesc(device.getImei());
                if (deviceStatus != null) {
                    queryParamMap.put("online", deviceStatus.getOnline());
                    queryParamMap.put("position", device.getPosition());
                    queryParamMap.put("name", device.getName());
                    queryParamMap.put("rssi", deviceStatus.getRssi());
                    queryParamMap.put("bat", deviceStatus.getBat());
                    queryParamMap.put("check", deviceStatus.getChecked());
                }
                if (onlineStatus != null) {
                    queryParamMap.put("online", onlineStatus);
                }

                DeviceAlarm deviceAlarm = deviceAlarmRepository.findFirstByImeiOrderByIdDesc(device.getImei());
                if (deviceAlarm != null) {
                    queryParamMap.put("val", AlarmStatusEnum.CLEAR.getValue().equals(deviceAlarm.getStatus()) ? AlarmStatusEnum.CLEAR.getValue() : deviceAlarm.getValue());
                }

                Map<String, Object> bodyMap = AppNoticeDeviceRequest.getInstance().requestBody(queryParamMap);

                sender.sendMessage(headerMap, bodyMap);
            }
        } catch (Exception e) {
            errorLogger.error("pushDeviceStatus异常！device:" + device.toString(), e);
        }
    }

    public void pushDeviceStatus(String imei) {
        Device device = deviceRepository.findFirstByImeiOrderByIdDesc(imei);
        if (device == null) {
            return;
        }

        pushDeviceStatus(device);
    }

    public void pushDeviceStatus(String imei, Integer onlineStatus) {
        Device device = deviceRepository.findFirstByImeiOrderByIdDesc(imei);
        if (device == null) {
            return;
        }

        pushDeviceStatus(device, onlineStatus);
    }


    public void pushChecked(String imei, int mid) {


        Device device = deviceRepository.findFirstByImeiOrderByIdDesc(imei);
        if (device == null) {
            return;
        }
        Gateway gateway = device.getGateway();
        if (gateway == null) {
            return;
        }
        String[] mobileIds = gatewayService.getGatewayUserMobileIds(device.getGlImei());


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 推送给个推
        messagePushService.pushMessage(mobileIds, gateway.getGlId()
                , "巡检通知"
                , "智能网关" + gateway.getGlName() + "下的" + deviceTypeToName(device.getType())
                        + "在" + format.format(mid * 1000L) + "完成巡检");


    }


    public static String deviceTypeToName(String type) {
        String name = "";
        switch (type) {
            case DeviceTypeConsts.DEV_TYPE_CG:
                name = DeviceTypeConsts.DEVICE_NAME_CG;
                break;
            case DeviceTypeConsts.DEV_TYPE_CO:
                name = DeviceTypeConsts.DEVICE_NAME_CO;
                break;
            case DeviceTypeConsts.DEV_TYPE_GAS:
                name = DeviceTypeConsts.DEVICE_NAME_GAS;
                break;
            case DeviceTypeConsts.DEV_TYPE_SMOKE:
                name = DeviceTypeConsts.DEVICE_NAME_SMOKE;
                break;
            case DeviceTypeConsts.DEV_TYPE_SOS:
                name = DeviceTypeConsts.DEVICE_NAME_SOS;
                break;
            case DeviceTypeConsts.DEV_TYPE_VALVE:
                name = DeviceTypeConsts.DEVICE_NAME_VALVE;
                break;
        }
        return name;
    }
}
