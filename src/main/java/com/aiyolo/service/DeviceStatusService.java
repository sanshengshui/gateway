package com.aiyolo.service;

import com.aiyolo.cache.DeviceLatestStatusCache;
import com.aiyolo.cache.DeviceLiveStatusCache;
import com.aiyolo.cache.entity.DeviceLatestStatus;
import com.aiyolo.constant.DeviceNetStatusEnum;
import com.aiyolo.entity.DeviceAlarm;
import com.aiyolo.entity.DeviceStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceStatusService {

    @Autowired DeviceLatestStatusCache deviceLatestStatusCache;
    @Autowired DeviceLiveStatusCache deviceLiveStatusCache;

    public DeviceLatestStatus getLatestDeviceStatusByGlImei(String glImei) {
        DeviceLatestStatus latestDeviceStatus = deviceLatestStatusCache.getByGlImei(glImei);
        latestDeviceStatus = (DeviceLatestStatus) latestDeviceStatus.clone(); // 克隆对象，以避免后续程序set操作修改到原始缓存

        // 判断设备是否离线
        int deviceLiveStatus = deviceLiveStatusCache.getByGlId(latestDeviceStatus.getGlId());
        if (deviceLiveStatus != DeviceLiveStatusCache.LIVE) {
            latestDeviceStatus.setNetStatus(DeviceNetStatusEnum.OFFLINE.getValue());
        }

        return latestDeviceStatus;
    }

    public Integer getDeviceNetStatus(String glImei) {
        DeviceLatestStatus latestDeviceStatus = getLatestDeviceStatusByGlImei(glImei);
        return latestDeviceStatus.getNetStatus();
    }

    public void updateLatestDeviceStatus(DeviceStatus deviceStatus) {
        if (deviceStatus == null) {
            return;
        }

        DeviceLatestStatus latestDeviceStatus = deviceLatestStatusCache.getByGlImei(deviceStatus.getGlImei());

        latestDeviceStatus.setVersion(deviceStatus.getVersion());
        latestDeviceStatus.setTemperature(deviceStatus.getTemperature());
        latestDeviceStatus.setHumidity(deviceStatus.getHumidity());
        latestDeviceStatus.setNetStatus(deviceStatus.getNetStatus());
        latestDeviceStatus.setDevStatus(deviceStatus.getDevStatus());

        deviceLatestStatusCache.save(deviceStatus.getGlImei(), latestDeviceStatus);
    }

    public void updateLatestDeviceStatus(DeviceAlarm deviceAlarm) {
        if (deviceAlarm == null) {
            return;
        }

        DeviceLatestStatus latestDeviceStatus = deviceLatestStatusCache.getByGlImei(deviceAlarm.getGlImei());

        latestDeviceStatus.setTemperature(deviceAlarm.getTemperature());
        latestDeviceStatus.setHumidity(deviceAlarm.getHumidity());
        latestDeviceStatus.setAlarmType(deviceAlarm.getType());

        deviceLatestStatusCache.save(deviceAlarm.getGlImei(), latestDeviceStatus);
    }

}
