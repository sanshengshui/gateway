package com.aiyolo.service;

import com.aiyolo.channel.data.request.AppNoticeDeviceRequest;
import com.aiyolo.constant.AlarmStatusEnum;
import com.aiyolo.constant.AppNoticeTypeConsts;
import com.aiyolo.entity.Device;
import com.aiyolo.entity.DeviceAlarm;
import com.aiyolo.entity.DeviceStatus;
import com.aiyolo.queue.Sender;
import com.aiyolo.repository.DeviceAlarmRepository;
import com.aiyolo.repository.DeviceRepository;
import com.aiyolo.repository.DeviceStatusRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void pushDeviceStatus(Device device) {
        pushDeviceStatus(device, AppNoticeTypeConsts.MODIFY);
    }

    public void pushDeviceStatus(Device device, Integer noticeType) {
        if (device == null) {
            return;
        }

        try {
            String[] mobileIds = gatewayService.getGatewayUserMobileIds(device.getGlImei());
            if (mobileIds != null && mobileIds.length > 0) {
                // 推送给APP
                Map<String, Object> headerMap = AppNoticeDeviceRequest.getInstance().requestHeader(mobileIds);
                headerMap.put("cache_time", 24 * 60 * 60 * 1000L);

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

}
