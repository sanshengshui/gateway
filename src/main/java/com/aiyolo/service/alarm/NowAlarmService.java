package com.aiyolo.service.alarm;

import com.aiyolo.entity.DeviceAlarm;
import com.aiyolo.entity.DeviceAlarmNow;
import com.aiyolo.entity.Gateway;
import com.aiyolo.repository.DeviceAlarmNowRepository;
import com.aiyolo.repository.GatewayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NowAlarmService extends AlarmService {

    @Autowired
    GatewayRepository gatewayRepository;
    @Autowired
    DeviceAlarmNowRepository deviceAlarmNowRepository;

    @Override
    public void run(DeviceAlarm deviceAlarm, Object params) {
        Gateway gateway = gatewayRepository.findFirstByGlImeiOrderByIdDesc(deviceAlarm.getGlImei());
        if (gateway == null) {
            return;
        }

        String areaCode = gateway.getAreaCode();
        String imei = deviceAlarm.getImei();
        String glImei = deviceAlarm.getGlImei();
        String alarmType = String.valueOf(params).substring(0, 1);

        if ("C".equals(alarmType)) {
            List<DeviceAlarmNow> records = deviceAlarmNowRepository.findByAreaCodeAndImei(areaCode, imei);
            deviceAlarmNowRepository.delete(records);
        } else {
            DeviceAlarmNow record = deviceAlarmNowRepository.findOneByAreaCodeAndImeiAndType(areaCode, imei, alarmType);
            if (record == null) {
                deviceAlarmNowRepository.save(new DeviceAlarmNow(
                        areaCode,
                        imei,
                        glImei,
                        alarmType));
            }
        }
    }

}
