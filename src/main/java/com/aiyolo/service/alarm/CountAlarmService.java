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

    @Autowired
    NowAlarmService nowAlarmService;

    @Override
    public void run(DeviceAlarm deviceAlarm, Object params) {
        Gateway gateway = gatewayRepository.findFirstByGlImeiOrderByIdDesc(deviceAlarm.getGlImei());
        if (gateway == null) {
            return;
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String date = format.format(deviceAlarm.getTimestamp() * 1000L);
        String areaCode = gateway.getAreaCode();
        String alarmType = String.valueOf(params);

        DeviceAlarmStat deviceAlarmStat = deviceAlarmStatRepository.findOneByDateAndAreaCodeAndType(date, areaCode, alarmType);
        if (deviceAlarmStat != null) {
            deviceAlarmStat.setNum(deviceAlarmStat.getNum() + 1);
            deviceAlarmStatRepository.save(deviceAlarmStat);
        } else {
            deviceAlarmStatRepository.save(new DeviceAlarmStat(
                    date,
                    areaCode,
                    alarmType,
                    1));
        }

        nowAlarmService.run(deviceAlarm, params);
    }

}
