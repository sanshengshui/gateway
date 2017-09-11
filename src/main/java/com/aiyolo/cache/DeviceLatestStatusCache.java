package com.aiyolo.cache;

import com.aiyolo.cache.entity.DeviceLatestStatus;
import com.aiyolo.constant.AlarmStatusEnum;
import com.aiyolo.constant.SingleAlarmTypeEnum;
import com.aiyolo.entity.DeviceAlarm;
import com.aiyolo.entity.DeviceStatus;
import com.aiyolo.repository.DeviceAlarmRepository;
import com.aiyolo.repository.DeviceStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class DeviceLatestStatusCache {

    @Autowired DeviceStatusRepository deviceStatusRepository;
    @Autowired DeviceAlarmRepository deviceAlarmRepository;

    @Cacheable("deviceLatestStatus")
    public DeviceLatestStatus getByGlImei(String glImei) {
        DeviceLatestStatus deviceLatestStatus = new DeviceLatestStatus();

        DeviceStatus lastDeviceStatus = deviceStatusRepository.findFirstByGlImeiOrderByIdDesc(glImei);
        if (lastDeviceStatus != null) {
            deviceLatestStatus.setGlId(lastDeviceStatus.getGlId());
            deviceLatestStatus.setVersion(lastDeviceStatus.getVersion());
            deviceLatestStatus.setTemperature(lastDeviceStatus.getTemperature());
            deviceLatestStatus.setHumidity(lastDeviceStatus.getHumidity());
            deviceLatestStatus.setNetStatus(lastDeviceStatus.getNetStatus());
            deviceLatestStatus.setDevStatus(lastDeviceStatus.getDevStatus());
        }

        DeviceAlarm lastDeviceAlarm = deviceAlarmRepository.findFirstByGlImeiOrderByIdDesc(glImei);
        if (lastDeviceAlarm != null) {
            int alarmType = lastDeviceAlarm.getStatus().equals(AlarmStatusEnum.LIFE.getValue()) ?
                    lastDeviceAlarm.getType() : SingleAlarmTypeEnum.CLEAR.getValue();
            deviceLatestStatus.setAlarmType(alarmType);
        }

        return deviceLatestStatus;
    }

    @CachePut(value = "deviceLatestStatus", key = "#p0")
    public DeviceLatestStatus save(String glImei, DeviceLatestStatus deviceLatestStatus) {
        return deviceLatestStatus;
    }

}
