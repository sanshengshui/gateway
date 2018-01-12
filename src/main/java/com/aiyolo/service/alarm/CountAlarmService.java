package com.aiyolo.service.alarm;

import com.aiyolo.entity.DeviceAlarm;
import com.aiyolo.entity.DeviceAlarmStat;
import com.aiyolo.entity.Gateway;
import com.aiyolo.repository.DeviceAlarmStatRepository;
import com.aiyolo.repository.GatewayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

@Service
public class CountAlarmService extends AlarmService {

    @Autowired
    GatewayRepository gatewayRepository;

    @Autowired
    DeviceAlarmStatRepository deviceAlarmStatRepository;

    @Override
    public void run(DeviceAlarm deviceAlarm, Object params) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String date = format.format(deviceAlarm.getTimestamp() * 1000L);
        String glImei = deviceAlarm.getGlImei();
        String alarmType = String.valueOf(params);

        DeviceAlarmStat deviceAlarmStat = deviceAlarmStatRepository.findOneByDateAndGlImeiAndType(date, glImei, alarmType);
        if (deviceAlarmStat != null) {
            deviceAlarmStat.setNum(deviceAlarmStat.getNum() + 1);
            deviceAlarmStatRepository.save(deviceAlarmStat);
        } else {
            Gateway gateway = gatewayRepository.findFirstByGlImeiOrderByIdDesc(deviceAlarm.getGlImei());
            if (gateway != null) {
                deviceAlarmStatRepository.save(new DeviceAlarmStat(
                        date,
                        glImei,
                        gateway.getAreaCode(),
                        gateway.getVillage(),
                        alarmType,
                        1));
            }
        }
    }

}
